# SafeInternet Project Builder
# ---------------------------

# CONFIGURATION
$MAIN_CLASS = "App.Main"  # The main class to run
$SQLITE_VERSION = "3.50.1.0"  # SQLite JDBC version
$JAR_FILES = @(
    "sqlite-jdbc-$SQLITE_VERSION.jar",
    "jbcrypt-0.4.jar",
    "gson-2.10.1.jar"
)
$DB_FILE = "SafeInternet.db"  # Path to the database file

try {
    # 1. SETUP AND VALIDATION --------------------------------------------
    Write-Host "`n[1/4] Validating project structure..." -ForegroundColor Cyan
    
    # Define project paths
    $PROJECT_ROOT = (Get-Item -Path ".").FullName
    $SRC_PATH = Join-Path $PROJECT_ROOT "src"
    $BIN_PATH = Join-Path $PROJECT_ROOT "bin"
    $LIB_PATH = Join-Path $PROJECT_ROOT "lib"

    # Verify critical files (e.g., database file)
    if (-not (Test-Path (Join-Path $PROJECT_ROOT $DB_FILE))) {
        throw "Database file '$DB_FILE' not found in project root"
    }

    # 2. BUILD CLASSPATH -------------------------------------------------
    Write-Host "[2/4] Building classpath..." -ForegroundColor Cyan

    # Build classpath to include all JAR files in lib/
    $CLASSPATH = ($JAR_FILES | ForEach-Object { 
        $jar = Join-Path $LIB_PATH $_
        if (-not (Test-Path $jar)) {
            throw "Missing required JAR: $_ (should be in lib/)"
        }
        $jar
    }) -join ";"

    # Include bin/ directory in the classpath
    $CLASSPATH = "$BIN_PATH;$CLASSPATH"

    # 3. COMPILATION -----------------------------------------------------
    Write-Host "[3/4] Compiling with SQLite $SQLITE_VERSION..." -ForegroundColor Cyan
    
    # Clean build directory (if exists)
    Remove-Item -Recurse -Force $BIN_PATH -ErrorAction SilentlyContinue
    New-Item -ItemType Directory -Path $BIN_PATH | Out-Null

    # Compile all sources (preserving package structure)
    $JAVA_FILES = Get-ChildItem -Path $SRC_PATH -Filter "*.java" -Recurse | 
                  Select-Object -ExpandProperty FullName
    
    if ($JAVA_FILES.Count -eq 0) {
        throw "No Java files found in src/"
    }

    # Compile Java files into the bin directory
    javac -encoding UTF-8 -cp $CLASSPATH -d $BIN_PATH @($JAVA_FILES)
    if ($LASTEXITCODE -ne 0) {
        throw "Compilation failed - check errors above"
    }

    # 4. DRIVER VERIFICATION ---------------------------------------------
    Write-Host "[4/4] Testing database connection..." -ForegroundColor Cyan
    
    # Create a simple Java test class to verify SQLite connection
    $DRIVER_TEST = @"
    import java.sql.*;
    public class DriverTest {
        public static void main(String[] args) {
            try {
                Class.forName("org.sqlite.JDBC");
                Connection conn = DriverManager.getConnection("jdbc:sqlite:$DB_FILE");
                System.out.println("[SUCCESS] Database connection working");
                conn.close();
            } catch (Exception e) {
                System.err.println("[ERROR] Database connection failed:");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
"@
    
    # Write the DriverTest class to the bin directory
    $TestFilePath = "$BIN_PATH/DriverTest.java"
    [System.IO.File]::WriteAllText($TestFilePath, $DRIVER_TEST)

    # Compile DriverTest class
    javac -cp $CLASSPATH "$BIN_PATH/DriverTest.java"

    # Run DriverTest to check database connection
    java --enable-native-access=ALL-UNNAMED -cp $CLASSPATH DriverTest

    # 5. APPLICATION LAUNCH ----------------------------------------------
    Write-Host "[5/5] Starting $MAIN_CLASS..." -ForegroundColor Cyan
    
    # Run the application with explicit package path
    $JAVA_CMD = @"
    java --enable-native-access=ALL-UNNAMED -cp "$CLASSPATH" $MAIN_CLASS
"@
    Write-Host "Executing: $JAVA_CMD" -ForegroundColor DarkGray
    Invoke-Expression $JAVA_CMD

} catch {
    Write-Host "`n[ERROR] $($_.Exception.Message)" -ForegroundColor Red
    
    # Handle missing JAR files error
    if ($_ -match "Missing required JAR") {
        Write-Host "`nDownload these exact JAR versions:" -ForegroundColor Yellow
        Write-Host "1. SQLite JDBC: https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.50.1.0/sqlite-jdbc-3.50.1.0.jar"
        Write-Host "2. jBCrypt: https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar"
        Write-Host "3. Gson: https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar"
        Write-Host "`nPlace them in your lib/ folder"
    }
} finally {
    Write-Host "`nOperation complete. Press any key to exit..." -ForegroundColor Green
    $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown") | Out-Null
}

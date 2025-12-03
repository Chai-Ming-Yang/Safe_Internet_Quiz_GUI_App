    import java.sql.*;
    public class DriverTest {
        public static void main(String[] args) {
            try {
                Class.forName("org.sqlite.JDBC");
                Connection conn = DriverManager.getConnection("jdbc:sqlite:SafeInternet.db");
                System.out.println("[SUCCESS] Database connection working");
                conn.close();
            } catch (Exception e) {
                System.err.println("[ERROR] Database connection failed:");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
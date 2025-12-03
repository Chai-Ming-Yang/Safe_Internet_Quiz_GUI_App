package DataAccessObject;
import java.sql.*;
import javax.swing.JOptionPane;

/* DAO -- Data Access Object
 * --> it interacts with the database 
 * --> Abstract class prevents instantiation
 * The reason for choosing Abstract class over Interfaces 
 *  - not needed for mulitple inheritance
 *  - pre-implemented some functions [ e.g connect(), disconnect(), isConnected() ] 
 */
public abstract class AbstractDAO implements DAO {
    private final String URL = "jdbc:sqlite:SafeInternet.db";
    protected Connection conn = null;
    protected String lastError;

    protected void connect() {
        try {
            conn = DriverManager.getConnection(URL);
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR (DAO.java): "+e.getMessage(), "Database Error", 0);
            lastError = e.getMessage();
        }
    }

    protected boolean isConnected() {
        try {
            return conn != null && !conn.isClosed();
        }
        catch (SQLException e) {
            return false;
        }
    }

    protected void disconnect() {
        try {
            if (isConnected()) {
                conn.close();
                conn = null;
            }
            return;
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "ERROR (DAO.java): "+e.getMessage(), "Database Error", 0);
        }
    }

    protected String getLastError() {
        return lastError;
    }

    public abstract void createTable();
    public abstract void dropTable();

}

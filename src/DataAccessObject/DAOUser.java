package DataAccessObject;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DAOUser extends AbstractDAO {
    private final String TABLE = "User";
    private final String COL_ID = "id";
    private final String COL_NAME = "name";
    private final String COL_PASSWORD = "password_hash";
    private final String COL_STARS = "stars";

    @Override
    public void createTable() {
        connect();
        if (!isConnected()) return;
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE+" ("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_NAME+" VARCHAR(30), "+COL_PASSWORD+" VARCHAR(60) NOT NULL,"+COL_STARS+" SMALLINT DEFAULT 0);";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Error creating "+TABLE+" table. Error: " + lastError);
        }
        finally {
            disconnect();
        }
    }

    public void addUser(String username, String password) {
        connect();
        String sql = "INSERT INTO "+ TABLE + "(" + COL_NAME + ',' + COL_PASSWORD + ") VALUES (?, ?)";
        if (!isConnected()) return;
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Failed to add User. Error: " + lastError);
        }        
        finally {
            disconnect();
        }
    }

    public boolean findUser(int id) {
        connect();
        if (!isConnected()) return false;
        String sql = "SELECT * FROM "+TABLE+" WHERE "+COL_ID+"=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Failed to find User. Error: " + lastError);
        }
        finally {           // executes even if early return
            disconnect();
        }
        return false;
    }

    public boolean findUser(String username) {
        connect();
        if (!isConnected()) return false;
        String sql = "SELECT * FROM "+TABLE+" WHERE "+COL_NAME+"=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Failed to find User. Error: " + lastError);
        }
        finally {
            disconnect();
        }
        return false;
    }

    public String getId(String username) {
        connect();
        if (!isConnected()) return "";
        String sql = "SELECT * FROM "+TABLE+" WHERE "+COL_NAME+"=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(COL_ID);
            }
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Failed to find User. Error: " + lastError);
        }
        finally {
            disconnect();
        }
        return "";
    }

    public int getStars(String username) {
        connect();
        if (!isConnected()) return 0;
        String sql = "SELECT * FROM "+TABLE+" WHERE "+COL_NAME+"=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(COL_STARS);
            }
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Failed to find User. Error: " + lastError);
        }
        finally {
            disconnect();
        }
        return 0;
    }

    public boolean vaildateUser(String username, String password) {
        connect();
        if (!isConnected()) return false;
        String sql = "SELECT * FROM "+TABLE+" WHERE "+COL_NAME+"=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return false;
            }
            String password_hash = rs.getString(COL_PASSWORD);
            return BCrypt.checkpw(password, password_hash);
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Failed to find User. Error: " + lastError);
        }
        finally {
            disconnect();
        }
        return false;
    }

    public void updateUserStars(int id, int stars) {
        connect();
        if (!isConnected()) return;
        String sql = "UPDATE "+TABLE+" SET "+COL_STARS+" = ? WHERE "+COL_ID+" = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, stars);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User stars updated successfully.");
            } else {
                System.out.println("No user found with ID: " + id);
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Failed to update user's stars. Error: " + lastError);
        }
        finally {
            disconnect();
        }
    }

    public void listUsers() {
        connect();
        if (!isConnected()) return;
        String sql = "SELECT * FROM "+TABLE;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(COL_ID) + "\t\t" + rs.getString(COL_NAME) + "\t\t" + rs.getString(COL_PASSWORD));
            }
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Error to List all Users. Error: " + lastError);
        }
        finally {
            disconnect();
        }
    }

    public void deleteUsers() {
        connect();
        if (!isConnected()) return;
        String sql = "DELETE FROM "+TABLE;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Error to Delete all Users. Error: " + lastError);
        }
        finally {
            disconnect();
        }
    }

    @Override
    public void dropTable() {
        connect();
        if (!isConnected()) return;
        String sql = "DROP TABLE "+TABLE;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Error to Drop User Table. Error: " + lastError);
        }
        finally {
            disconnect();
        }
    }

    public static void main (String[] args) {
        DAOUser daoUser = new DAOUser();
        daoUser.dropTable();
        daoUser.createTable();
        daoUser.addUser("Adminn", "Admin@123");
        daoUser.updateUserStars(1, 1);
        daoUser.addUser("Benjamin", "Benjamin@123");
        daoUser.updateUserStars(2, 2);
        daoUser.addUser("Calvin", "Calvin@123");
        daoUser.updateUserStars(3, 3);
        daoUser.addUser("Daniel", "Daniel@123");
        daoUser.addUser("Eliass", "Eliass@123");
        daoUser.addUser("George", "George@123");
        // System.out.println((daoUser.findUser(4)));
        // daoUser.deleteUsers();
        daoUser.listUsers();
    }
}
package DataAccessObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DAOLeaderBoard extends AbstractDAO {
    private final String    TABLE = "leaderboards",
                            COL_ID = "id",
                            COL_USER_ID = "userId",
                            COL_SCORE = "score",
                            COL_DIFFICULTY = "difficulty",
                            COL_DATETIME = "dateTime";

    @Override
    public void createTable() {
        connect();
        if (!isConnected()) return;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_USER_ID + " INTEGER NOT NULL, " +
                        COL_SCORE + " INTEGER NOT NULL, " +
                        COL_DATETIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        COL_DIFFICULTY + " INTEGER);";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Error creating "+TABLE+" table. Error: " + lastError);
        }
        
        disconnect();
    }

    public void addScore(int userId, int score, int difficulty) {
        /* userID, score, difficulty
         * 
         */
        connect();
        if (!isConnected()) return;
        String sql = "INSERT INTO "+ TABLE + " (" + COL_USER_ID + ", " + COL_SCORE + ", " + COL_DIFFICULTY + ") VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, score);
            stmt.setInt(3, difficulty);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Failed to add Score. Error: " + lastError);
        }
        disconnect();
    }
    
    public List<Score> getTopScores(int difficulty) {
        // get Top10 for All User
        List<Score> topScores = new ArrayList<>();
        connect();
        if (!isConnected()) return null;

        String sql = "SELECT lb." + COL_ID + ", lb." + COL_USER_ID + ", u." + "name" + " AS username, lb." + COL_SCORE + ", lb." + COL_DATETIME + ", lb." + COL_DIFFICULTY + " "
                        + "FROM " + TABLE + " lb "
                        + "JOIN User u ON lb." + COL_USER_ID + " = u." + "id" + " "
                        + "WHERE lb." + COL_DIFFICULTY + " = ? "
                        + "ORDER BY lb." + COL_SCORE + " DESC "
                        + "LIMIT 10";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, difficulty);
            ResultSet rs = stmt.executeQuery();
            int rank = 1;
            while (rs.next()) {
                String name = rs.getString("username");
                int score = rs.getInt(COL_SCORE);
                topScores.add(new Score(rank, name, score));
                rank++;
            }
            rs.close();
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Error to List top 10 of LeaderBoard. Error: " + lastError);
        }

        disconnect();
        return topScores;
    }

    public List<Score> getTopScores(int difficulty, int id) {
        // get Top10 for User ID
        connect();
        List<Score> topScores = new ArrayList<>();
        if (!isConnected()) return null;
        String sql = "SELECT lb." + COL_ID + ", lb." + COL_USER_ID + ", u." + "name" + " AS username, lb." + COL_SCORE + ", lb." + COL_DATETIME + ", lb." + COL_DIFFICULTY + " "
                        + "FROM " + TABLE + " lb "
                        + "JOIN User u ON lb." + COL_USER_ID + " = u." + "id" + " "
                        + "WHERE lb." + COL_DIFFICULTY + " = ? "
                        + "AND lb." + COL_USER_ID + " = ?"
                        + "ORDER BY lb." + COL_SCORE + " DESC "
                        + "LIMIT 10";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, difficulty);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            int rank = 1;
            while (rs.next()) {
                String name = rs.getString("username");
                int score = rs.getInt(COL_SCORE);
                topScores.add(new Score(rank, name, score));
                rank++;
            }
            rs.close();
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            JOptionPane.showMessageDialog(null, "Error to List top 10 of LeaderBoard. Error: " + lastError);
        }

        disconnect();
        return topScores;
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
            JOptionPane.showMessageDialog(null, "Error to Delete all Leaderboard. Error: " + lastError);
        }

        disconnect();
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
            JOptionPane.showMessageDialog(null, "Error to Drop Leaderboard Table. Error: " + lastError);
        }

        disconnect();
    }


    public static void main(String[] args) {
        DAOLeaderBoard daoLeaderBoard = new DAOLeaderBoard();
        daoLeaderBoard.dropTable();
        daoLeaderBoard.createTable();
        daoLeaderBoard.addScore(1, 85, 2);
        daoLeaderBoard.addScore(2, 100, 2);
        daoLeaderBoard.addScore(3, 90, 2);
        daoLeaderBoard.addScore(4, 65, 2);
        daoLeaderBoard.addScore(5, 80, 2);
        daoLeaderBoard.addScore(6, 70, 2);
        daoLeaderBoard.addScore(1, 100, 1);
        daoLeaderBoard.addScore(2, 85, 1);
        daoLeaderBoard.addScore(3, 95, 1);
        daoLeaderBoard.addScore(4, 70, 1);
        daoLeaderBoard.addScore(5, 80, 1);
        daoLeaderBoard.addScore(6, 75, 1);
        daoLeaderBoard.addScore(1, 95, 3);
        daoLeaderBoard.addScore(2, 90, 3);
        daoLeaderBoard.addScore(3, 100, 3);
        
        // For difficulty 3
        System.out.println("\nONLY TOP 10 (DIFFICULTY = 3)");
        List<Score> topScoresDifficulty3 = daoLeaderBoard.getTopScores(3);
        for (Score score : topScoresDifficulty3) {
            System.out.println("Rank: " + score.getRank() +
                            " | Name: " + score.getName() +
                            " | Score: " + score.getScore());
        }

        // For difficulty 2
        System.out.println("\nONLY TOP 10 (DIFFICULTY = 2)");
        List<Score> topScoresDifficulty2 = daoLeaderBoard.getTopScores(2);
        for (Score score : topScoresDifficulty2) {
            System.out.println("Rank: " + score.getRank() +
                            " | Name: " + score.getName() +
                            " | Score: " + score.getScore());
        }

        // For difficulty 1
        System.out.println("\nONLY TOP 10 (DIFFICULTY = 1)");
        List<Score> topScoresDifficulty1 = daoLeaderBoard.getTopScores(1);
        for (Score score : topScoresDifficulty1) {
            System.out.println("Rank: " + score.getRank() +
                            " | Name: " + score.getName() +
                            " | Score: " + score.getScore());
        }
    }
}
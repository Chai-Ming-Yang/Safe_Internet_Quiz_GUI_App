package App;
import java.awt.*;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import DataAccessObject.DAO;
import DataAccessObject.DAOLeaderBoard;
import DataAccessObject.DAOUser;
import GraphicalUserInterface.*;

public class Navigator extends JFrame implements InterfaceNavigator {
    private User user = null;
    private CardLayout cardLayout = new CardLayout();
    private JPanel screen_main = new JPanel();
    private JPanel  screen_Login,
                    screen_MainMenu,
                    screen_LearningModule,
                    screen_Quiz,
                    screen_LeaderBoard;
    
    public static final String  LOGIN = "login",
                                MENU = "main menu",
                                LEARNING_MODULE = "learning module",
                                QUIZ = "quiz",
                                LEADERBOARD = "leaderboard";

    public Navigator() {
        // Initialize database tables (unchanged)
        /*   POLYMORPHISM  */
        DAO daoLeaderBoard = new DAOLeaderBoard();
        DAO daoUser = new DAOUser();
        daoLeaderBoard.createTable();
        daoUser.createTable();

        // Configure main window
        setTitle("Safe Internet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setResizable(false); // Disable resizing to enforce fixed size
        setUndecorated(false); // Keep window borders

        // Initialize screens (unchanged)
        screen_Login = new GUILogin(this);
        screen_MainMenu = new GUIMainMenu(this);
        screen_LearningModule = new GUILearningModule(this);
        screen_Quiz = new GUIQuiz(this);
        screen_LeaderBoard = new GUILeaderBoard(this);

        // Configure card layout (unchanged)
        screen_main.setLayout(cardLayout);
        screen_main.add(screen_Login, LOGIN);
        screen_main.add(screen_MainMenu, MENU);
        screen_main.add(screen_LearningModule, LEARNING_MODULE);
        screen_main.add(screen_Quiz, QUIZ);
        screen_main.add(screen_LeaderBoard, LEADERBOARD);

        //Global Styling
        UIManager.put("Panel.background", new Color(240, 245, 240)); // Soft green-gray
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.background", new Color(70, 130, 180)); // Steel blue
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", new Color(100, 150, 200)); // Lighter blue

        setLayout(new BorderLayout());
        add(screen_main, BorderLayout.CENTER);

        // Set fixed smartphone-friendly size (360x640)
        setSize(360, 640);
        setLocationRelativeTo(null); // Center the window
        showScreen(LOGIN);
        setVisible(true);

        new Thread(() -> {
            try {
                File soundFile = new File(System.getProperty("user.dir") + File.separator + "resources\\music\\omori-bgm.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                synchronized (this) {
                    this.wait(); // Wait forever
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void setUser(String id, String username, int stars) {
        user = new User(id, username, stars);
    }

    public void clearUser() {
        user = null;
    }

    public User getUser() {
        return user;
    }

    public void showScreen(String screen_name) {
        if (screen_name.equals(MENU) && screen_MainMenu != null) {
            ((GUIMainMenu) screen_MainMenu).refresh();
        }
        cardLayout.show(screen_main, screen_name);
    }

    public static void main(String[] args) {
        //SwingUtilities used to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            new Navigator();
        });
    }
}
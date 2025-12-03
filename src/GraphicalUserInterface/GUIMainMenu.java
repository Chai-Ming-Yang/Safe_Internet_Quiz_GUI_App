package GraphicalUserInterface;
import java.awt.*;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import App.Navigator;

public class GUIMainMenu extends JPanel {
    private Navigator navigator = null;
    private JPanel  panel_title = new JPanel(new BorderLayout()),
                    panel_user = new JPanel(),
                    panel_buttons = new JPanel();
    private JLabel  label_title = new JLabel("Main Menu"),
                    label_username = new JLabel("USERNAME"),
                    label_stars = new JLabel("STAR");
    private JButton button_LearningModule = new JButton("Learning Module"),
                    button_Quiz = new JButton("Quiz"),
                    button_LeaderBoard = new JButton("Leaderboard"),
                    button_LogOut = new JButton("Log Out");
                    

    public GUIMainMenu() {
        // Title Section (larger font)
        label_title.setFont(new Font("Courier", Font.BOLD, 28));  // Increased from 24
        label_title.setHorizontalAlignment(JLabel.CENTER);
        label_title.setForeground(new Color(150, 150, 250));
        panel_title.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));  // Slightly more padding
        panel_title.add(label_title, BorderLayout.NORTH);
        panel_title.setBackground(new Color(25, 55, 65)); // Dark slate
        label_title.setForeground(new Color(220, 240, 220));
        label_title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 32));

        //User Info Labels (larger font)
        int labelWidth = 150;       // Slightly wider
        int labelHeight = 35;       // Slightly taller
        
        label_username.setHorizontalAlignment(JLabel.LEFT);
        label_username.setPreferredSize(new Dimension(labelWidth, labelHeight));
        label_username.setFont(new Font("Courier", Font.BOLD, 16));  // Increased from 14
        
        label_stars.setHorizontalAlignment(JLabel.RIGHT);
        label_stars.setPreferredSize(new Dimension(labelWidth, labelHeight));
        label_stars.setFont(new Font("Courier", Font.BOLD, 16));  // Increased from 14
        
        //User Panel
        panel_user.setLayout(new BoxLayout(panel_user, BoxLayout.X_AXIS));
        panel_user.setPreferredSize(new Dimension(300, labelHeight));  // Slightly wider
        panel_user.add(Box.createHorizontalGlue());
        panel_user.add(label_username);
        panel_user.add(Box.createHorizontalStrut(10));
        panel_user.add(label_stars);
        panel_user.add(Box.createHorizontalGlue());
        panel_title.add(panel_user, BorderLayout.CENTER);

        //Larger buttons
        Dimension buttonSize = new Dimension(220, 40);  // Increased from 200×30
        Font buttonFont = new Font("Courier", Font.PLAIN, 16);  // Increased from 14
        button_LearningModule.setForeground(Color.WHITE);
        button_Quiz.setForeground(Color.WHITE);
        button_LeaderBoard.setForeground(Color.WHITE);
        button_LogOut.setForeground(Color.BLACK);
        
        //Button listeners
        button_LearningModule.addActionListener(e -> {
            playButtonPressSound();
            if (navigator != null) navigator.showScreen(Navigator.LEARNING_MODULE);
        });
        button_Quiz.addActionListener(e -> {
            playButtonPressSound();
            if (navigator != null) navigator.showScreen(Navigator.QUIZ);
        });
        button_LeaderBoard.addActionListener(e -> {
            playButtonPressSound();
            if (navigator != null) navigator.showScreen(Navigator.LEADERBOARD);
        });
        button_LogOut.addActionListener(e -> {
            playButtonPressSound();
            if (navigator != null) {
                navigator.clearUser();
                navigator.showScreen(Navigator.LOGIN);
            }
        });

        //Configure buttons
        configureButton(button_LearningModule, buttonSize, buttonFont);
        configureButton(button_Quiz, buttonSize, buttonFont);
        configureButton(button_LeaderBoard, buttonSize, buttonFont);
        configureButton(button_LogOut, buttonSize, buttonFont);
        
        //Add buttons with spacing
        panel_buttons.setLayout(new BoxLayout(panel_buttons, BoxLayout.Y_AXIS));
        int verticalSpacing = 12;  // Slightly more spacing
        panel_buttons.add(Box.createRigidArea(new Dimension(0, verticalSpacing)));
        panel_buttons.add(button_LearningModule);
        panel_buttons.add(Box.createRigidArea(new Dimension(0, verticalSpacing)));
        panel_buttons.add(button_Quiz);
        panel_buttons.add(Box.createRigidArea(new Dimension(0, verticalSpacing)));
        panel_buttons.add(button_LeaderBoard);
        panel_buttons.add(Box.createRigidArea(new Dimension(0, verticalSpacing)));
        panel_buttons.add(button_LogOut);
        panel_buttons.setBackground(new Color(240, 245, 240));
        button_LearningModule.setBackground(new Color(65, 105, 225)); // Royal blue
        button_Quiz.setBackground(new Color(220, 20, 60)); // Crimson
        button_LeaderBoard.setBackground(new Color(46, 139, 87)); // Sea green
        button_LogOut.setBackground(new Color(169, 169, 169)); // Gray
        
        //Main layout
        setLayout(new BorderLayout());
        add(panel_title, BorderLayout.NORTH);
        add(panel_buttons, BorderLayout.CENTER);
        
        //Set preferred size for smartphone
        setPreferredSize(new Dimension(360, 640));
    }

    public GUIMainMenu(Navigator nav) {
        this();
        navigator = nav;
    }

    private void playButtonPressSound() {
        new Thread(() -> {
            try {
                File soundFile = new File(System.getProperty("user.dir") + File.separator + "resources\\music\\minecraft_click.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public void refresh() {
        if (navigator==null)    return;
        int numStars = navigator.getUser().getStars();
        label_stars.setText("⭐".repeat(numStars));

        String username = navigator.getUser().getUsername();
        label_username.setText(username);
        label_username.setForeground(new Color(30, 30, 30));
        label_stars.setForeground(new Color(218, 165, 32)); // Gold
    }

    private void configureButton(JButton button, Dimension size, Font font) {
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(font);
        button.setFocusable(false);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(360,640));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GUIMainMenu());
        frame.setVisible(true);
    }
}
package GraphicalUserInterface;
import java.awt.*;
import java.io.*;
import java.util.Random;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import App.Navigator;
import App.User;
import QuizModule.*;
import DataAccessObject.DAOLeaderBoard;
import DataAccessObject.DAOUser;

public class GUIQuiz extends JPanel {
    private Navigator navigator = null;
    private Quiz quiz = null;
    private JPanel  panel_top = new JPanel(new BorderLayout()),
                    panel_mid = new JPanel(new BorderLayout()),
                    panel_bottom = new JPanel(new GridLayout(2,2));
    private JLabel  label_page = new JLabel(),
                    label_hearts = new JLabel();
    private JTextPane textPane_content = new JTextPane();
    private JButton button_back = new JButton();
    private JButton[] button_ans = { new JButton("A"), 
                                     new JButton("B"), 
                                     new JButton("C"), 
                                     new JButton("D") };
    private JButton[] button_difficulty = { new JButton("Easy"),
                                            new JButton("Normal"),
                                            new JButton("Hard") };
    
    //New visual design constants
    private final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private final Color PANEL_COLOR = new Color(255, 255, 255);
    private final Color BUTTON_COLOR = new Color(70, 130, 180);
    private final Color CORRECT_COLOR = new Color(100, 200, 100);
    private final Color WRONG_COLOR = new Color(220, 100, 100);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 22);
    private final Font QUESTION_FONT = new Font("Arial", Font.PLAIN, 18);
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);
    private final Font FEEDBACK_FONT = new Font("Arial", Font.PLAIN, 14);

    public GUIQuiz() {
        //Set background
        setBackground(BACKGROUND_COLOR);
        panel_top.setBackground(PANEL_COLOR);
        panel_mid.setBackground(PANEL_COLOR);
        panel_bottom.setBackground(PANEL_COLOR);

        initializeQuizQuestions();

        /* Top panel */
        label_page.setText("Difficulty Selection");
        label_page.setFont(TITLE_FONT);
        label_page.setHorizontalAlignment(SwingConstants.CENTER);
        panel_top.add(label_page, BorderLayout.CENTER);

        ImageIcon img_home = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources\\button_back.png");
        img_home = new ImageIcon(img_home.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        button_back.setIcon(img_home);
        button_back.setContentAreaFilled(false);
        button_back.setBorderPainted(false);
        button_back.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, 
                                            "Are you sure you want to exit?", 
                                            "Confirm Exit", 
                                            JOptionPane.YES_NO_OPTION, 
                                            JOptionPane.QUESTION_MESSAGE);
    
            if (option == JOptionPane.NO_OPTION) return;
            showDifficultySelection();
            if (navigator != null) 
                navigator.showScreen(Navigator.MENU);
        });
        panel_top.add(button_back, BorderLayout.WEST);
        
        JPanel right_spacer = new JPanel();
        label_hearts.setFont(new Font("Arial", Font.BOLD, 20));
        label_hearts.setForeground(Color.RED);
        right_spacer.add(label_hearts);
        right_spacer.setBackground(PANEL_COLOR);
        panel_top.add(right_spacer, BorderLayout.EAST);
        
        /* Mid panel */
        textPane_content.setEditable(false);
        textPane_content.setFont(QUESTION_FONT);
        textPane_content.setMargin(new Insets(15, 20, 15, 20));
        JScrollPane scrollPane = new JScrollPane(textPane_content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel_mid.add(scrollPane);
        
        /* Bottom panel & buttons */
        for (JButton btn: button_ans) {
            btn.setFont(BUTTON_FONT);
            btn.setForeground(Color.WHITE);
            btn.setBackground(BUTTON_COLOR);
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(10, 15, 10, 15));
            btn.setOpaque(true);
            btn.addActionListener(e -> {
                btn.setEnabled(false);
                String  ans = quiz.getCurrentQuestion().getAnswer(),
                        userAns = btn.getText();
                boolean correctStatus = userAns.equals(ans);
                
                playButtonPressSound(correctStatus);
                highlightOption(userAns, WRONG_COLOR);
                highlightOption(ans, CORRECT_COLOR);
                javax.swing.Timer timer = new javax.swing.Timer(800, ex -> {
                    btn.setEnabled(true);
                    highlightOption(userAns, BUTTON_COLOR);
                    highlightOption(ans, BUTTON_COLOR);
                    quiz.answerQuestion(correctStatus);
                    if (quiz.hasMoreQuestions())
                        setQuestion();
                    if (quiz.isGameOver() || !quiz.hasMoreQuestions()) {
                        showResult();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            });
        }
        
        for (JButton btn: button_difficulty) {
            btn.setFont(BUTTON_FONT);
            btn.setForeground(Color.WHITE);
            btn.setBackground(BUTTON_COLOR);
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(10, 15, 10, 15));
            btn.setOpaque(true);
        }
        
        button_difficulty[0].addActionListener(e -> { showQuiz(Quiz.EASY); });
        button_difficulty[1].addActionListener(e -> { showQuiz(Quiz.NORMAL); });
        button_difficulty[2].addActionListener(e -> { showQuiz(Quiz.HARD); });

        panel_bottom.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));
        
        setLayout(new BorderLayout());
        add(panel_top, BorderLayout.NORTH);
        add(panel_mid, BorderLayout.CENTER);
        add(panel_bottom, BorderLayout.SOUTH);

        showDifficultySelection();
    }

    public GUIQuiz(Navigator nav) {
        this();
        navigator = nav;
    }

    private void playButtonPressSound(boolean isCorrect) {
        String[] correctSounds = {
            System.getProperty("user.dir") + File.separator + "resources" + File.separator + "music" + File.separator + "correct1.wav",
            System.getProperty("user.dir") + File.separator + "resources" + File.separator + "music" + File.separator + "correct2.wav",
            System.getProperty("user.dir") + File.separator + "resources" + File.separator + "music" + File.separator + "correct3.wav"
        };

        String[] wrongSounds = {
            System.getProperty("user.dir") + File.separator + "resources" + File.separator + "music" + File.separator + "wrong1.wav",
            System.getProperty("user.dir") + File.separator + "resources" + File.separator + "music" + File.separator + "wrong2.wav",
            System.getProperty("user.dir") + File.separator + "resources" + File.separator + "music" + File.separator + "wrong3.wav"
        };

        String[] selectedSounds = isCorrect ? correctSounds : wrongSounds;

        // Randomly pick a sound
        Random rand = new Random();
        String soundFilePath = selectedSounds[rand.nextInt(selectedSounds.length)];

        // Play the selected sound in a new thread
        new Thread(() -> {
            try {
                File soundFile = new File(soundFilePath);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float volume = -20.0f; // decrease volume by 10 decibels
                volumeControl.setValue(volume); // value in decibels

                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void initializeQuizQuestions() {
        Gson gson = new Gson();
        quiz = new Quiz();
        try {
            FileReader reader = new FileReader(System.getProperty("user.dir") + File.separator + "resources\\quiz.json");
            quiz = gson.fromJson(reader, Quiz.class);
            System.out.println(reader);
            if (quiz == null) 
                System.out.println("Pages are null after deserialization");
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error Parsing learning_module.json. Error: "+e);
            return;
        }
    }

    public void showDifficultySelection() {
        label_page.setText("Quiz!      ");
        label_page.setFont(TITLE_FONT);
        String quizIntroduction =   "Welcome to the Quiz!\n\n" +
                                    "In this activity, you'll answer a series of questions to test your knowledge.\n\n" +
                                    "You start with a set number of hearts (lives). Each incorrect answer will cost you a heart, so try your best to answer correctly!\n\n" +
                                    "- Easy for a relaxed challenge\n" +
                                    "- Normal for a balanced experience\n" +
                                    "- Hard for a tough test\n\n" +
                                    "Once you've chosen a difficulty, the quiz will start, and you'll be prompted with questions to answer.\n\n" +
                                    "Good luck and have fun!";
        textPane_content.setText(quizIntroduction);
        textPane_content.setFont(FEEDBACK_FONT);
        label_hearts.setText("");
        panel_bottom.removeAll();
        panel_bottom.setLayout(new GridLayout(3, 1, 10, 10));
        for (JButton btn: button_difficulty) {
            panel_bottom.add(btn);
        }
        panel_bottom.revalidate();
        panel_bottom.repaint();
    }

    private void showQuiz(String difficulty) {
        quiz.startGame(difficulty);
        panel_bottom.removeAll();
        panel_bottom.setLayout(new GridLayout(2, 2, 10, 10));
        for (JButton btn: button_ans) {
            panel_bottom.add(btn);
        }
        panel_bottom.revalidate();
        panel_bottom.repaint();
        setQuestion();
    }

    private void setQuestion() {
        label_hearts.setText(Integer.toString(quiz.getHearts()) + "♥");
        Question q = quiz.getCurrentQuestion();
        label_page.setText("Question " + Integer.toString(quiz.getCurrentQuestionIndex() + 1));
        label_page.setFont(TITLE_FONT);
        textPane_content.setText(q.getQuestion());
        if (Question.TRUE_FALSE.equals(q.getType())) {
            button_ans[0].setText("True");
            button_ans[1].setText("False");
            button_ans[2].setVisible(false);
            button_ans[3].setVisible(false);
            button_ans[2].setEnabled(false);
            button_ans[3].setEnabled(false);
        }
        else {
            button_ans[0].setText("A");
            button_ans[1].setText("B");
            button_ans[2].setVisible(true);
            button_ans[3].setVisible(true);
            button_ans[2].setEnabled(true);
            button_ans[3].setEnabled(true);
        }
    }

    private void highlightOption(String option, Color color) {
        if ("A".equals(option) || "True".equals(option))
            button_ans[0].setBackground(color);
        if ("B".equals(option) || "False".equals(option))
            button_ans[1].setBackground(color);
        if ("C".equals(option))
            button_ans[2].setBackground(color);
        if ("D".equals(option))
            button_ans[3].setBackground(color);
    }

    private void showResult() {
        panel_bottom.removeAll();

        if (quiz.isGameOver()) {
            label_page.setText("You Lose...");
        } 
        else {
            label_page.setText("You Win!!!");
            if (quiz.getHearts() == 3) {
                int stars;
                if (quiz.getCurrentDifficulty() == Quiz.EASY) {
                    stars = 1;
                }
                else if (quiz.getCurrentDifficulty() == Quiz.NORMAL) {
                    stars = 2;
                }
                else {
                    stars = 3;
                }
                User curUser = navigator.getUser();
                while (curUser != null && curUser.getStars() < stars) {
                    curUser.incrementStars();
                }
                DAOUser daoUser = new DAOUser();
                daoUser.updateUserStars(Integer.parseInt(curUser.getID()), stars);
            }
        }
        
        int numCorrect = quiz.getNumCorrect();
        int score = numCorrect * 100 / quiz.NUM_QUESTIONS;
        String scoreFormatted = String.format("%3d", score);
        
        //Centered HTML content with larger fonts
        String message = "<html><div style='text-align: center; width: 100%;'>" +
                        "<p style='font-size: 24pt; margin-bottom: 15px;'>" +
                        "✅ Correct: " + numCorrect + "/" + quiz.NUM_QUESTIONS + "</p>" +
                        "<p style='font-size: 28pt; margin-bottom: 15px; color:rgb(0, 0, 0); font-weight: bold;'>" +
                        "🥇 Score: " + scoreFormatted + "%</p>" +
                        "<p style='font-size: 22pt; margin-top: 20px;'>" +
                        getMotivationalMessage(score) + "</p>" +
                        "</div></html>";
        
        textPane_content.setContentType("text/html");
        textPane_content.setText(message);
        
        //Button Styling
        JButton btnRestart = new JButton("Play Again");
        btnRestart.setFont(new Font("Arial", Font.BOLD, 18));
        btnRestart.setForeground(Color.WHITE);
        btnRestart.setBackground(new Color(70, 130, 180));
        btnRestart.setOpaque(true);
        btnRestart.setBorderPainted(false);
        btnRestart.addActionListener(e -> {
            showDifficultySelection();
        });

        JButton btnMainMenu = new JButton("Main Menu");
        btnMainMenu.setFont(new Font("Arial", Font.BOLD, 18));
        btnMainMenu.setForeground(Color.WHITE);
        btnMainMenu.setBackground(new Color(70, 130, 180));
        btnMainMenu.setOpaque(true);
        btnMainMenu.setBorderPainted(false);
        btnMainMenu.addActionListener(e -> {
            if (navigator != null)
                navigator.showScreen(Navigator.MENU);
            showDifficultySelection();
        });
        
        panel_bottom.setLayout(new GridLayout(1, 2, 20, 0));
        panel_bottom.add(btnRestart);
        panel_bottom.add(btnMainMenu);
        revalidate();
        repaint();

        if (navigator != null) {
            int difficulty;
            if (quiz.getCurrentDifficulty() == Quiz.EASY) 
                difficulty = 1;
            else if (quiz.getCurrentDifficulty() == Quiz.NORMAL) 
                difficulty = 2;
            else 
                difficulty = 3;
            User curUser = navigator.getUser();
            DAOLeaderBoard daoLeaderBoard = new DAOLeaderBoard();
            daoLeaderBoard.addScore(Integer.parseInt(curUser.getID()), score, difficulty);
        }
    }

    private String getMotivationalMessage(int score) {
        if (score < 20) return "Don't give up!";
        if (score < 40) return "You can do better!";
        if (score < 60) return "Good try!";
        if (score < 80) return "That's good!";
        return "Outstanding!";
    }

    public static void main (String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GUIQuiz());
        frame.setVisible(true);
    }
}
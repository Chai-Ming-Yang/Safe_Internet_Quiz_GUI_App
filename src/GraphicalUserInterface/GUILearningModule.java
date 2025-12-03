package GraphicalUserInterface;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import com.google.gson.Gson;

import LearningModules.LearningModule;
import App.Navigator;

public class GUILearningModule extends JPanel {
    private Navigator navigator = null;
    private LearningModule learningModule = null;
    private int totalPages;
    private JPanel  panel_top = new JPanel(new BorderLayout()),
                    panel_mid = new JPanel(new BorderLayout()),
                    panel_bottom = new JPanel(new BorderLayout()),
                    panel_content = new JPanel();
    private JLabel  label_page = new JLabel(),
                    label_title = new JLabel(),
                    label_img = new JLabel();
    private JTextArea textPane_content = new JTextArea();
    private ImageIcon img_content = new ImageIcon();
    private JButton button_back = new JButton(),
                    button_next = new JButton(),
                    button_prev = new JButton();
    private JComboBox<String> comboBox_pageSelector = new JComboBox<String>();

    private final Color BACKGROUND_COLOR = new Color(245, 245, 255);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color TITLE_COLOR = new Color(70, 70, 180);

    public GUILearningModule() {
        setBackground(BACKGROUND_COLOR);
        
        initializeLearningModulePages();
        
        label_page.setFont(new Font("Arial", Font.BOLD, 18));
        label_page.setHorizontalAlignment(SwingConstants.CENTER);
        panel_top.add(label_page, BorderLayout.CENTER);

        ImageIcon img_home = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources\\button_back.png");
        img_home = new ImageIcon(img_home.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        button_back.setIcon(img_home);
        button_back.addActionListener(e -> {
            if (navigator!=null)
                navigator.showScreen(Navigator.MENU);
        });
        panel_top.add(button_back, BorderLayout.WEST);
        
        JPanel right_spacer = new JPanel();
        right_spacer.setSize(button_back.getSize());
        right_spacer.setPreferredSize(button_back.getPreferredSize());
        panel_top.add(right_spacer, BorderLayout.EAST);

        label_title.setFont(new Font("Arial", Font.BOLD, 22));
        label_title.setForeground(TITLE_COLOR);
        label_title.setHorizontalAlignment(JLabel.CENTER);
        label_title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panel_mid.add(label_title, BorderLayout.NORTH);

        panel_content.setLayout(new BoxLayout(panel_content, BoxLayout.Y_AXIS));
        panel_content.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel_content.setBackground(BACKGROUND_COLOR);

        textPane_content.setEditable(false);
        textPane_content.setFont(new Font("Arial", Font.PLAIN, 16));
        textPane_content.setForeground(TEXT_COLOR);
        textPane_content.setOpaque(false);
        textPane_content.setLineWrap(true);
        textPane_content.setWrapStyleWord(true);
        panel_content.add(textPane_content);

        label_img.setHorizontalAlignment(JLabel.CENTER);
        label_img.setVerticalAlignment(JLabel.CENTER);
        label_img.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel_content.add(Box.createVerticalStrut(15)); // Add spacing
        panel_content.add(label_img);

        JScrollPane scrollPane = new JScrollPane(panel_content);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel_mid.add(scrollPane, BorderLayout.CENTER);

        for (int i = 1; i <= totalPages; i++) {
            comboBox_pageSelector.addItem("Page " + i);
        }
        comboBox_pageSelector.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox_pageSelector.addActionListener(e -> {
            int idx = comboBox_pageSelector.getSelectedIndex();
            setPage(idx);
        });
        panel_bottom.add(comboBox_pageSelector, BorderLayout.CENTER);

        ImageIcon img_arrowLeft = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources\\button_arrowLeft.png");
        img_arrowLeft = new ImageIcon(img_arrowLeft.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        button_prev.setIcon(img_arrowLeft);
        button_prev.addActionListener(e -> {
            int idx = comboBox_pageSelector.getSelectedIndex() - 1;
            if (idx >= 0) {
                comboBox_pageSelector.setSelectedIndex(idx);
            }
        });
        panel_bottom.add(button_prev, BorderLayout.WEST);

        ImageIcon img_arrowRight = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources\\button_arrowRight.png");
        img_arrowRight = new ImageIcon(img_arrowRight.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        button_next.setIcon(img_arrowRight);
        button_next.addActionListener(e -> {
            int idx = comboBox_pageSelector.getSelectedIndex() + 1;
            if (idx < totalPages) {
                comboBox_pageSelector.setSelectedIndex(idx);
            }
        });
        panel_bottom.add(button_next, BorderLayout.EAST);

        setPage(0);
        setLayout(new BorderLayout());
        add(panel_top, BorderLayout.NORTH);
        add(panel_mid, BorderLayout.CENTER);
        add(panel_bottom, BorderLayout.SOUTH);
    }

    public GUILearningModule(Navigator nav) {
        this();
        navigator = nav;
    }

    private void playPageTurnSound() {
        new Thread(() -> {
            try {
                File soundFile = new File(System.getProperty("user.dir") + File.separator + "resources\\music\\pageturn.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void initializeLearningModulePages() {
        Gson gson = new Gson();
        learningModule = new LearningModule();
        try {
            FileReader reader = new FileReader(System.getProperty("user.dir") + File.separator + "resources\\LearningModule\\learning_module.json");
            learningModule = gson.fromJson(reader, LearningModule.class);
            
            if (!learningModule.hasPages()) {
                System.out.println("Pages are null after deserialization");
            }
            totalPages = learningModule.getNumPages();
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error Parsing learning_module.json. Error: "+e);
        }
    }

    public void setPage(int idx) {
        playPageTurnSound();
        //Change to 1-based index for display
        label_page.setText("Page " + (idx + 1));
        label_title.setText(learningModule.getTitle(idx + 1));
        textPane_content.setText(learningModule.getText(idx + 1));

        //Load and display image with better sizing
        Path imagePath = Paths.get(System.getProperty("user.dir"), "resources", "LearningModule", learningModule.getImg(idx + 1));
        String imagePathStr = imagePath.toString();
        
        //Scale image to larger size while maintaining aspect ratio
        ImageIcon originalIcon = new ImageIcon(imagePathStr);
        Image originalImage = originalIcon.getImage();
        
        //Calculate scaled dimensions (max width 300px, proportional height)
        int maxWidth = 300;
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();
        int newWidth = Math.min(originalWidth, maxWidth);
        int newHeight = (int) ((double) originalHeight / originalWidth * newWidth);
        
        //Create scaled image
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        img_content = new ImageIcon(scaledImage);
        label_img.setIcon(img_content);
        
        //Debugging output
        System.out.println("Displaying image: " + imagePathStr);
        System.out.println("Original dimensions: " + originalWidth + "x" + originalHeight);
        System.out.println("Scaled dimensions: " + newWidth + "x" + newHeight);

        //Force UI update
        panel_content.revalidate();
        panel_content.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Learning Module");
            frame.setSize(360, 640);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GUILearningModule());
            frame.setVisible(true);
        });
    }
}
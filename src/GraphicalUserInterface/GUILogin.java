package GraphicalUserInterface;
import javax.swing.*;
import javax.swing.event.*;

import App.Navigator;
import DataAccessObject.DAOUser;

import java.awt.*;
import java.awt.event.*;

public class GUILogin extends JPanel implements InterfaceLogin{
    private Navigator navigator = null;
    private JPanel panel_title = new JPanel();
    private JPanel panel_contentField = new JPanel();
    private JPanel panel_name = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panel_pass = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panel_button = new JPanel();
    private JLabel label_title = new JLabel("Safe Internet");
    private JLabel label_nameField = new JLabel("Username: ");
    private JLabel label_nameFeedback = new JLabel("");
    private JLabel label_passField = new JLabel("Password: ");
    private JLabel label_passFeedback = new JLabel("");
    private JTextField field_name = new JTextField(13);
    private JPasswordField field_pass = new JPasswordField(13);
    private JButton button_signup = new JButton("Sign Up");
    private JButton button_login = new JButton("Log In");
    
    public GUILogin() {

        label_title.setFont(new Font("Courier", Font.BOLD, 40));
        label_title.setForeground(new Color(150,150,250));
        panel_title.add(label_title);
        panel_title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        panel_title.setBackground(new Color(53, 94, 59)); // Dark teal
        label_title.setForeground(new Color(220, 240, 220)); // Mint green
        label_title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        label_nameField.setFont(new Font("Courier", Font.PLAIN, 20));
        panel_name.add(label_nameField);
        field_name.setFont(new Font("Courier", Font.PLAIN, 20));
        field_name.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFeedback();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFeedback();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFeedback();
            }
            
            private void updateFeedback() {
                label_nameFeedback.setText(getNameFeedback());
            }
        });
        panel_name.add(field_name);
        Dimension size = panel_name.getPreferredSize();
        panel_name.setMinimumSize(size);
        panel_name.setMaximumSize(size);

        label_passField.setFont(new Font("Courier", Font.PLAIN, 20));
        panel_pass.add(label_passField);
        field_pass.setFont(new Font("Courier", Font.PLAIN, 20));
        field_pass.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFeedback();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFeedback();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFeedback();
            }
            
            private void updateFeedback() {
                label_passFeedback.setText(getPasswordFeedback());
            }
        });
        panel_pass.add(field_pass);
        panel_pass.setMaximumSize(panel_pass.getPreferredSize());
        panel_pass.setMinimumSize(panel_pass.getPreferredSize());
        
        label_nameFeedback.setFont(new Font("Courier", Font.PLAIN, 12));
        label_nameFeedback.setAlignmentX(Component.CENTER_ALIGNMENT);
        label_nameFeedback.setHorizontalAlignment(SwingConstants.CENTER);
        label_passFeedback.setFont(new Font("Courier", Font.PLAIN, 12));
        label_passFeedback.setAlignmentX(Component.CENTER_ALIGNMENT);
        label_passFeedback.setHorizontalAlignment(SwingConstants.CENTER);

        button_signup.setFont(new Font("Courier", Font.PLAIN, 20));
        button_signup.setFocusable(false);
        button_signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signup();
            }
        });
        panel_button.add(button_signup);
        button_login.setBackground(new Color(76, 175, 80)); // Green
        button_signup.setBackground(new Color(30, 144, 255)); // Dodger blue
        button_login.setFont(new Font("Courier", Font.PLAIN, 20));
        button_signup.setFocusable(false);
        button_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        panel_button.add(button_login);
        
        panel_contentField.setLayout(new BoxLayout(panel_contentField, BoxLayout.Y_AXIS));
        panel_contentField.add(panel_name);
        panel_contentField.add(label_nameFeedback);
        panel_contentField.add(panel_pass);
        panel_contentField.add(label_passFeedback);
        panel_contentField.add(panel_button);
        panel_contentField.setBackground(new Color(240, 245, 240));
        field_name.setBackground(new Color(255, 255, 230)); // Pale yellow
        field_pass.setBackground(new Color(255, 255, 230));

        setLayout(new BorderLayout());
        setBackground(new Color(0xc3,0xc3,0xc3));
        add(panel_title, BorderLayout.NORTH);
        add(panel_contentField, BorderLayout.CENTER);
    }

    public GUILogin(Navigator nav) {
        this();
        navigator = nav;
    }

    public void signup() {
        DAOUser daoUser = new DAOUser();
        if (!validName()) {
            JOptionPane.showMessageDialog(null, "Name does not meet requirements.");
            return;
        }
        if (!validPass()) {
            JOptionPane.showMessageDialog(null, "Password does not meet requirements.");
            return;
        }
        if (daoUser.findUser(field_name.getText())) {
            JOptionPane.showMessageDialog(null, "User by that username already exists. Please use another username.");
            return;
        }

        System.out.println("SIGN UP SUCCESSFUL!");
        if (navigator == null)  return;
        
        String cur_username = field_name.getText();
        String cur_password = new String(field_pass.getPassword());
        daoUser.addUser(cur_username, cur_password);

        String id = daoUser.getId(cur_username);
        int stars = daoUser.getStars(cur_username);
        navigator.setUser(id, cur_username, stars);

        field_name.setText("");
        field_pass.setText("");
        navigator.showScreen(Navigator.MENU);
    }

    public void login() {
        DAOUser daoUser = new DAOUser();
        if (!validName()) {
            JOptionPane.showMessageDialog(null, "Name does not meet requirements.");
            return;
        }
        if (!validPass()) {
            JOptionPane.showMessageDialog(null, "Password does not meet requirements.");
            return;
        }
        if (!daoUser.findUser(field_name.getText())) {
            JOptionPane.showMessageDialog(null, "User does not exist. (Wrong username?)");
            return;
        }
        if (!daoUser.vaildateUser(field_name.getText(), new String(field_pass.getPassword()))) {
            JOptionPane.showMessageDialog(null, "Incorrect Password");
            return;
        }
        System.out.println("LOGIN SUCCESSFUL!");
        if (navigator == null)  return;

        String cur_username = field_name.getText();
        String id = daoUser.getId(cur_username);
        int stars = daoUser.getStars(cur_username);
        navigator.setUser(id, cur_username, stars);
        
        field_name.setText("");
        field_pass.setText("");
        navigator.showScreen(Navigator.MENU);
    }

    public boolean validName() {
        String username = field_name.getText();
        return username.matches("^[a-zA-Z0-9_\\-@#!\\$%\\^&\\*\\(\\)]{6,30}$");
    }

    public boolean validPass() {
        String password = new String(field_pass.getPassword());
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()]).{8,}$");
    }

    public String getNameFeedback() {
        String username = field_name.getText();
        label_nameFeedback.setForeground(Color.RED);
        label_nameFeedback.setFont(new Font("Courier", Font.PLAIN, 12));
        if (username.length() < 6 || username.length() > 30) 
            return "Username must be between 6 to 30 characters.";
        if (!username.matches("^[a-zA-Z0-9_\\-@#!\\$%\\^&\\*\\(\\)]+$"))
            return "Only uppercase, lowercase, numbers, and _-@#!$%^&*() are allowed.";
        
            
        label_nameFeedback.setForeground(new Color(50, 160, 50));
        label_nameFeedback.setFont(new Font("Courier", Font.BOLD, 12));
        return "Username is Valid.";
    }

    public String getPasswordFeedback() {
        String password = new String(field_pass.getPassword());
        StringBuilder missing = new StringBuilder();
        label_passFeedback.setForeground(Color.RED);
        label_passFeedback.setFont(new Font("Courier", Font.PLAIN, 12));
        if (password.length() < 8) 
            return "<html>Password should be at least 8 characters.<br><html>";
        if (!password.matches(".*[A-Za-z].*"))
            missing.append("Must contain at least 1 uppercase and 1 lowercase letter.<br>");
        if (!password.matches(".*\\d.*"))
            missing.append("Must contain number.<br>");
        if (!password.matches(".*[!@#$%^&*()].*"))
            missing.append("Must contain special character.<br>");
        if (missing.length() == 0) {
            missing.append("Password meets all requirements");
            label_passFeedback.setForeground(new Color(20,160,20));
            label_passFeedback.setFont(new Font("Courier", Font.BOLD, 12));
        }
        return "<html>" + missing.toString() + "</html>";
    }

    public static void main (String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(360,640));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GUILogin());
        frame.setVisible(true);
    }
}
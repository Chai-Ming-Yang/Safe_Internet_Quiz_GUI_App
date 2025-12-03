package GraphicalUserInterface;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import App.Navigator;
import DataAccessObject.DAOLeaderBoard;
import DataAccessObject.Score;

import java.awt.*;
import java.io.File;
import java.util.List;

public class GUILeaderBoard extends JPanel {
    private Navigator navigator = null;
    private JPanel  panel_top = new JPanel(new BorderLayout()),
                    panel_mid = new JPanel(new BorderLayout()),
                    panel_bottom = new JPanel(new BorderLayout());
    private JLabel  label_page = new JLabel("🏆Leaderboard🏆"),
                    userHighScoreLabel = new JLabel();
    private JButton button_back = new JButton(),
                    btnView = new JButton();
    private JTable leaderboardTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboBox_difficulty;
    
    public GUILeaderBoard() {
        label_page.setHorizontalAlignment(SwingConstants.CENTER);
        panel_top.add(label_page, BorderLayout.CENTER);
        panel_top.setBackground(new Color(47, 79, 79));     //Dark slate gray
        label_page.setForeground(new Color(255, 215, 0));   //Gold
        label_page.setFont(new Font("Courier", Font.BOLD, 22));

        ImageIcon img_home = new ImageIcon(System.getProperty("user.dir") + File.separator + "resources\\button_back.png");
        img_home = new ImageIcon(img_home.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        button_back.setIcon(img_home);
        button_back.addActionListener(e -> {
            if (navigator!=null)
                navigator.showScreen(Navigator.MENU);
        });
        panel_top.add(button_back, BorderLayout.WEST);
        
        JPanel right_spacer = new JPanel();                 //Right spacer added to center the title
        right_spacer.setSize(button_back.getSize());
        right_spacer.setPreferredSize(button_back.getPreferredSize());
        panel_top.add(right_spacer, BorderLayout.EAST);
        
        JPanel viewPanel = new JPanel(new BorderLayout());
        btnView.setText("All");
        btnView.setFont(new Font("Courier", Font.PLAIN, 20));
        btnView.addActionListener(e -> {
            if (btnView.getText() == "Self") {
                btnView.setText("All");
            }
            else {
                btnView.setText("Self");
            }
            setLeaderBoard();
        });
        viewPanel.add(btnView, BorderLayout.WEST);

        String[] difficulties = {"Easy", "Normal", "Hard"};
        comboBox_difficulty = new JComboBox<>(difficulties);
        comboBox_difficulty.setBackground(new Color(245, 245, 245));
        comboBox_difficulty.addActionListener(e -> {
            setLeaderBoard();
        });
        viewPanel.add(comboBox_difficulty, BorderLayout.EAST);

        panel_mid.add(viewPanel, BorderLayout.NORTH);
        

        String[] columnNames = {"Rank", "Name", "Score"};
        Object[][] data = new Object[0][3];

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //Make table cells non-editable
            }
        };
        leaderboardTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        leaderboardTable.setBackground(new Color(240, 248, 255)); // Alice blue
        leaderboardTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leaderboardTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel_mid.add(scrollPane, BorderLayout.CENTER);
        
        
        //userHighScoreLabel.setText("THIS PART FOR USER's");
        userHighScoreLabel.setFont(new Font("Courier", Font.PLAIN, 24));
        panel_bottom.add(userHighScoreLabel);
        
        
        setLayout(new BorderLayout());
        add(panel_top, BorderLayout.NORTH);
        add(panel_mid, BorderLayout.CENTER);
        add(panel_bottom, BorderLayout.SOUTH);

        setLeaderBoard();
    }

    public GUILeaderBoard(Navigator navigator) {
        this();
        this.navigator = navigator;
    }

    private int mapDifficultyToCode(String difficulty) {
        switch (difficulty) {
            case "Easy": return 1;
            case "Normal": return 2;
            case "Hard": return 3;
            default: return 0;
        }
    }

    public void setLeaderBoard() {
        String diffString = (String) comboBox_difficulty.getSelectedItem();
        int diffCode = mapDifficultyToCode(diffString);
        DAOLeaderBoard daoLeaderBoard = new DAOLeaderBoard();
        List<Score> scores;
        if (btnView.getText() == "Self") {
            if (navigator == null) 
                scores = daoLeaderBoard.getTopScores(diffCode,1);
            else
                scores = daoLeaderBoard.getTopScores(diffCode, Integer.parseInt(navigator.getUser().getID()));
        }
        else {
            scores = daoLeaderBoard.getTopScores(diffCode);
        }
        populateTable(scores);
    }

    private void populateTable(List<Score> scores) {
        tableModel.setRowCount(0); //Clear existing data
        if (scores != null) {
            for (Score score : scores) {
                Object[] rowData = {
                    score.getRank(),
                    score.getName(),
                    score.getScore()
                };
                tableModel.addRow(rowData);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(360, 640);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GUILeaderBoard());
        frame.setVisible(true);
    }
}
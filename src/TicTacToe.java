import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private boolean playerXTurn = true;
    private boolean gameOver = false;
    private JLabel statusLabel;
    private JButton resetButton;
    private JButton exitButton;
    private JToggleButton darkModeToggle;

    private Color lightBG = Color.WHITE;
    private Color darkBG = new Color(45, 45, 45);
    private Color lightFG = Color.BLACK;
    private Color darkFG = Color.WHITE;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel board = new JPanel(new GridLayout(3, 3));
        Font btnFont = new Font("Arial", Font.BOLD, 40);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton("");
                btn.setFont(btnFont);
                btn.addActionListener(this);
                buttons[i][j] = btn;
                board.add(btn);
            }
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        darkModeToggle = new JToggleButton("Dark Mode");
        darkModeToggle.addActionListener(e -> toggleDarkMode());

        statusLabel = new JLabel("Player X's Turn");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        controlPanel.add(resetButton);
        controlPanel.add(darkModeToggle);
        controlPanel.add(exitButton);

        add(statusLabel, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
        toggleDarkMode(); // start with dark mode off
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        JButton btn = (JButton) e.getSource();
        if (!btn.getText().equals("")) return;

        btn.setText(playerXTurn ? "X" : "O");
        btn.setForeground(playerXTurn ? Color.BLUE : Color.RED);
        if (checkWinner()) {
            statusLabel.setText("Player " + (playerXTurn ? "X" : "O") + " Wins!");
            gameOver = true;
            autoResetGame();
        } else if (isBoardFull()) {
            statusLabel.setText("It's a Draw!");
            gameOver = true;
            autoResetGame();
        } else {
            playerXTurn = !playerXTurn;
            statusLabel.setText("Player " + (playerXTurn ? "X" : "O") + "'s Turn");
        }
    }

    private boolean checkWinner() {
        String symbol = playerXTurn ? "X" : "O";

        for (int i = 0; i < 3; i++)
            if (buttons[i][0].getText().equals(symbol) &&
                buttons[i][1].getText().equals(symbol) &&
                buttons[i][2].getText().equals(symbol)) return true;

        for (int j = 0; j < 3; j++)
            if (buttons[0][j].getText().equals(symbol) &&
                buttons[1][j].getText().equals(symbol) &&
                buttons[2][j].getText().equals(symbol)) return true;

        if (buttons[0][0].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][2].getText().equals(symbol)) return true;

        if (buttons[0][2].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][0].getText().equals(symbol)) return true;

        return false;
    }

    private boolean isBoardFull() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                if (btn.getText().isEmpty())
                    return false;
        return true;
    }

    private void resetGame() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                btn.setText("");

        gameOver = false;
        playerXTurn = true;
        statusLabel.setText("Player X's Turn");
    }

    private void autoResetGame() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(() -> resetGame());
            }
        }, 10000);
    }

    private void toggleDarkMode() {
        boolean isDark = darkModeToggle.isSelected();

        Color bg = isDark ? darkBG : lightBG;
        Color fg = isDark ? darkFG : lightFG;

        getContentPane().setBackground(bg);
        statusLabel.setForeground(fg);

        for (JButton[] row : buttons)
            for (JButton btn : row) {
                btn.setBackground(bg);
                btn.setForeground(btn.getText().equals("X") ? Color.BLUE : Color.RED);
            }

        resetButton.setBackground(bg);
        resetButton.setForeground(fg);

        exitButton.setBackground(bg);
        exitButton.setForeground(fg);

        darkModeToggle.setBackground(bg);
        darkModeToggle.setForeground(fg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}

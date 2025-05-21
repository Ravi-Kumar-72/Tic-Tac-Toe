import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TicTacToeQueueMode extends JFrame implements ActionListener {
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

    private final Color X_COLOR = Color.BLUE;
    private final Color O_COLOR = Color.RED;
    private final Color WARNING_COLOR = Color.ORANGE;

    private Queue<Point> playerXMoves = new LinkedList<>();
    private Queue<Point> playerOMoves = new LinkedList<>();

    private Point pendingRemove = null;  // Symbol pending removal
    private String pendingSymbol = "";  // "X" or "O"

    public TicTacToeQueueMode() {
        setTitle("Tic Tac Toe - Queue Mode");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel board = new JPanel(new GridLayout(3, 3));
        Font btnFont = new Font("Arial", Font.BOLD, 40);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton("");
                btn.setFont(btnFont);
                btn.setFocusPainted(false);
                btn.addActionListener(this);
                buttons[i][j] = btn;
                board.add(btn);
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
        toggleDarkMode(); // start with light mode
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        JButton clicked = (JButton) e.getSource();
        Point pos = getButtonPosition(clicked);

        if (!clicked.getText().isEmpty()) return;
        if (pendingRemove != null && pendingRemove.equals(pos)) {
            statusLabel.setText("That cell is temporarily blocked this turn.");
            return;
        }

        String currentSymbol = playerXTurn ? "X" : "O";
        Queue<Point> currentQueue = playerXTurn ? playerXMoves : playerOMoves;

        // Remove the previous pending symbol (if any)
        if (pendingRemove != null) {
            JButton btn = buttons[pendingRemove.x][pendingRemove.y];
            if (btn.getText().equals(pendingSymbol)) {
                btn.setText("");
            }
            pendingRemove = null;
            pendingSymbol = "";
        }

        // Place current move
        clicked.setText(currentSymbol);
        clicked.setForeground(playerXTurn ? X_COLOR : O_COLOR);
        currentQueue.add(pos);

        // Handle limit of 3
        if (currentQueue.size() > 3) {
            Point toRemove = currentQueue.poll();
            JButton btnToRemove = buttons[toRemove.x][toRemove.y];
            btnToRemove.setForeground(WARNING_COLOR);  // Warning color
            pendingRemove = toRemove;
            pendingSymbol = currentSymbol;
        }

        if (checkWinner(currentSymbol)) {
            statusLabel.setText("Player " + currentSymbol + " Wins!");
            gameOver = true;
        } else {
            playerXTurn = !playerXTurn;
            statusLabel.setText("Player " + (playerXTurn ? "X" : "O") + "'s Turn");
        }
    }

    private boolean checkWinner(String symbol) {
        String[][] grid = new String[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                grid[i][j] = buttons[i][j].getText();

        for (int i = 0; i < 3; i++)
            if (grid[i][0].equals(symbol) && grid[i][1].equals(symbol) && grid[i][2].equals(symbol)) return true;

        for (int j = 0; j < 3; j++)
            if (grid[0][j].equals(symbol) && grid[1][j].equals(symbol) && grid[2][j].equals(symbol)) return true;

        if (grid[0][0].equals(symbol) && grid[1][1].equals(symbol) && grid[2][2].equals(symbol)) return true;
        if (grid[0][2].equals(symbol) && grid[1][1].equals(symbol) && grid[2][0].equals(symbol)) return true;

        return false;
    }

    private Point getButtonPosition(JButton btn) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (buttons[i][j] == btn)
                    return new Point(i, j);
        return null;
    }

    private void resetGame() {
        for (JButton[] row : buttons)
            for (JButton btn : row) {
                btn.setText("");
                btn.setBackground(getBackground());
            }

        playerXMoves.clear();
        playerOMoves.clear();
        pendingRemove = null;
        pendingSymbol = "";

        playerXTurn = true;
        gameOver = false;
        statusLabel.setText("Player X's Turn");
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
                String txt = btn.getText();
                if (txt.equals("X")) btn.setForeground(X_COLOR);
                else if (txt.equals("O")) btn.setForeground(O_COLOR);
                else btn.setForeground(fg);
            }

        resetButton.setBackground(bg);
        resetButton.setForeground(fg);

        exitButton.setBackground(bg);
        exitButton.setForeground(fg);

        darkModeToggle.setBackground(bg);
        darkModeToggle.setForeground(fg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToeQueueMode());
    }
}

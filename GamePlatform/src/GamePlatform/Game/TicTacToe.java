package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends BaseGame {
    private static final int CELL_SIZE = 100;
    private static final int GRID_SIZE = 3;
    private static final int GAME_SIZE = CELL_SIZE * GRID_SIZE;
    private static final int WINDOW_HEIGHT = GAME_SIZE + 100;
    
    private char[][] board;
    private char currentPlayer;
    private boolean gameEnded;
    private String difficulty;
    private int moves;
    private JButton[][] buttons;
    private JLabel statusLabel;
    private JComboBox<String> difficultyCombo;
    private JButton restartButton;
    
    public TicTacToe() {
        super("Tic Tac Toe");
        setTitle("Tic Tac Toe - Player: " + username);
        setSize(GAME_SIZE + 16, WINDOW_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(44, 62, 80));
        setLayout(new BorderLayout());
        
        initializeGame();
        createComponents();
        
        setLocationRelativeTo(null);
    }
    
    private void initializeGame() {
        board = new char[GRID_SIZE][GRID_SIZE];
        currentPlayer = 'X';  // 玩家使用X
        gameEnded = false;
        difficulty = "Medium";
        moves = 0;
        
        // 初始化棋盘
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                board[i][j] = ' ';
            }
        }
    }
    
    private void createComponents() {
        // 游戏标题
        JLabel titleLabel = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);
        
        // 游戏面板
        JPanel gamePanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        buttons = new JButton[GRID_SIZE][GRID_SIZE];
        
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setFocusPainted(false);
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(e -> handleMove(row, col));
                gamePanel.add(buttons[i][j]);
            }
        }
        add(gamePanel, BorderLayout.CENTER);
        
        // 控制面板
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(new Color(44, 62, 80));
        
        // 难度选择
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setSelectedItem("Medium");
        difficultyCombo.addActionListener(e -> difficulty = (String)difficultyCombo.getSelectedItem());
        controlPanel.add(difficultyCombo);
        
        // 重新开始按钮
        restartButton = new JButton("New Game");
        restartButton.addActionListener(e -> restartGame());
        controlPanel.add(restartButton);
        
        // 状态标签
        statusLabel = new JLabel("Your turn (X)", SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        controlPanel.add(statusLabel);
        
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private void handleMove(int row, int col) {
        if (gameEnded || board[row][col] != ' ') return;
        
        // 玩家移动
        board[row][col] = currentPlayer;
        buttons[row][col].setText(String.valueOf(currentPlayer));
        moves++;
        
        if (checkWin(currentPlayer)) {
            gameEnded = true;
            statusLabel.setText("You win!");
            // 计算分数：首次获胜10000分，之后根据移动次数递减
            int score = Math.max(10000 - (moves - GRID_SIZE * 2) * 1000, 1000);
            saveScore(score);
            return;
        }
        
        if (moves == GRID_SIZE * GRID_SIZE) {
            gameEnded = true;
            statusLabel.setText("Draw!");
            saveScore(5000);  // 平局得5000分
            return;
        }
        
        // 电脑移动
        currentPlayer = 'O';
        statusLabel.setText("Computer's turn (O)");
        Timer timer = new Timer(500, e -> {
            makeComputerMove();
            if (!gameEnded) {
                currentPlayer = 'X';
                statusLabel.setText("Your turn (X)");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void makeComputerMove() {
        int[] move = getBestMove();
        board[move[0]][move[1]] = 'O';
        buttons[move[0]][move[1]].setText("O");
        moves++;
        
        if (checkWin('O')) {
            gameEnded = true;
            statusLabel.setText("Computer wins!");
            saveScore(0);  // 失败得0分
            return;
        }
        
        if (moves == GRID_SIZE * GRID_SIZE) {
            gameEnded = true;
            statusLabel.setText("Draw!");
            saveScore(5000);  // 平局得5000分
        }
    }
    
    private int[] getBestMove() {
        switch (difficulty) {
            case "Hard":
                return getHardMove();
            case "Medium":
                return Math.random() < 0.5 ? getHardMove() : getEasyMove();
            default:
                return getEasyMove();
        }
    }
    
    private int[] getEasyMove() {
        // 随机选择空位
        while (true) {
            int row = (int)(Math.random() * GRID_SIZE);
            int col = (int)(Math.random() * GRID_SIZE);
            if (board[row][col] == ' ') {
                return new int[]{row, col};
            }
        }
    }
    
    private int[] getHardMove() {
        // 使用极小化极大算法
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];
        
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    int score = minimax(board, 0, false);
                    board[i][j] = ' ';
                    
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }
    
    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        if (checkWin('O')) return 1;
        if (checkWin('X')) return -1;
        if (isBoardFull()) return 0;
        
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = ' ';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = ' ';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
    
    private boolean checkWin(char player) {
        // 检查行
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        
        // 检查列
        for (int j = 0; j < GRID_SIZE; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }
        
        // 检查对角线
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        
        return false;
    }
    
    private boolean isBoardFull() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }
    
    private void restartGame() {
        initializeGame();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setText("");
            }
        }
        statusLabel.setText("Your turn (X)");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TicTacToe().setVisible(true);
        });
    }
}

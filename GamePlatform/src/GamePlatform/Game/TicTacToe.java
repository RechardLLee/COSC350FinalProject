package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class TicTacToe extends JFrame {
    private char[][] board = new char[3][3];
    private char currentPlayer;
    private int playerXScore = 0;
    private int playerOScore = 0;
    private boolean isEnglish = true;  // 默认英文
    
    private JButton[][] buttons = new JButton[3][3];
    private JLabel statusLabel;
    private JLabel scoreLabel;
    
    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createComponents();
        initializeGame();
        
        setLocationRelativeTo(null);
    }
    
    private void createComponents() {
        // Status panel
        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel("Player X's turn");
        scoreLabel = new JLabel("X: 0  O: 0");
        statusPanel.add(statusLabel);
        statusPanel.add(scoreLabel);
        add(statusPanel, BorderLayout.NORTH);
        
        // Game board
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(e -> handleMove(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> initializeGame());
        controlPanel.add(newGameButton);
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private void initializeGame() {
        // Initialize board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        currentPlayer = 'X';
        updateStatus();
    }
    
    private void handleMove(int row, int col) {
        if (board[row][col] == ' ') {
            board[row][col] = currentPlayer;
            buttons[row][col].setText(String.valueOf(currentPlayer));
            
            if (checkWin()) {
                gameOver(currentPlayer + " wins!");
                updateScore();
            } else if (checkTie()) {
                gameOver("It's a tie!");
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                updateStatus();
            }
        }
    }
    
    private boolean checkWin() {
        // Check rows, columns and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return true;
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return true;
        }
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return true;
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return true;
        return false;
    }
    
    private boolean checkTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }
    
    private void updateScore() {
        if (currentPlayer == 'X') playerXScore++;
        else playerOScore++;
        scoreLabel.setText("X: " + playerXScore + "  O: " + playerOScore);
    }
    
    private void updateStatus() {
        statusLabel.setText("Player " + currentPlayer + "'s turn");
    }
    
    private void gameOver(String message) {
        statusLabel.setText(message);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TicTacToe().setVisible(true);
        });
    }
}

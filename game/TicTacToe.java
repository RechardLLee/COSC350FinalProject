package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private int playerXScore = 0;
    private int playerOScore = 0;
    private JLabel statusLabel;
    private JLabel scoreLabel;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> handleMove(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> resetGame());
        controlPanel.add(newGameButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void handleMove(int row, int col) {
        if (buttons[row][col].getText().equals("")) {
            buttons[row][col].setText(String.valueOf(currentPlayer));
            
            if (checkWin()) {
                JOptionPane.showMessageDialog(this, 
                    "Player " + currentPlayer + " wins!");
                updateScore();
                resetGame();
            } else if (checkTie()) {
                JOptionPane.showMessageDialog(this, "It's a tie!");
                resetGame();
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                statusLabel.setText("Player " + currentPlayer + "'s turn");
            }
        }
    }

    private boolean checkWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!buttons[i][0].getText().equals("") &&
                buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                buttons[i][0].getText().equals(buttons[i][2].getText())) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (!buttons[0][j].getText().equals("") &&
                buttons[0][j].getText().equals(buttons[1][j].getText()) &&
                buttons[0][j].getText().equals(buttons[2][j].getText())) {
                return true;
            }
        }

        // Check diagonals
        if (!buttons[0][0].getText().equals("") &&
            buttons[0][0].getText().equals(buttons[1][1].getText()) &&
            buttons[0][0].getText().equals(buttons[2][2].getText())) {
            return true;
        }

        if (!buttons[0][2].getText().equals("") &&
            buttons[0][2].getText().equals(buttons[1][1].getText()) &&
            buttons[0][2].getText().equals(buttons[2][0].getText())) {
            return true;
        }

        return false;
    }

    private boolean checkTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateScore() {
        if (currentPlayer == 'X') {
            playerXScore++;
        } else {
            playerOScore++;
        }
        scoreLabel.setText("X: " + playerXScore + "  O: " + playerOScore);
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        currentPlayer = 'X';
        statusLabel.setText("Player X's turn");
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;

public class GuessNumberGame extends JFrame {
    private Map<String, Map<String, Integer>> difficulties;
    private String currentDifficulty;
    private int targetNumber;
    private ArrayList<Integer> guesses;
    private int attemptsLeft;
    
    private JLabel infoLabel;
    private JTextField inputField;
    private JLabel resultLabel;
    private JComboBox<String> difficultyCombo;
    
    public GuessNumberGame() {
        // Initialize game settings
        difficulties = new HashMap<>();
        Map<String, Integer> easySettings = new HashMap<>();
        easySettings.put("range", 50);
        easySettings.put("attempts", 10);
        difficulties.put("Easy", easySettings);
        
        Map<String, Integer> mediumSettings = new HashMap<>();
        mediumSettings.put("range", 100);
        mediumSettings.put("attempts", 7);
        difficulties.put("Medium", mediumSettings);
        
        Map<String, Integer> hardSettings = new HashMap<>();
        hardSettings.put("range", 200);
        hardSettings.put("attempts", 5);
        difficulties.put("Hard", hardSettings);
        
        currentDifficulty = "Medium";
        guesses = new ArrayList<>();
        
        // Set up window
        setTitle("Guess the Number Game");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        createWidgets();
        startNewGame();
    }
    
    private void createWidgets() {
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Difficulty selection
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.add(new JLabel("Difficulty:"));
        difficultyCombo = new JComboBox<>(difficulties.keySet().toArray(new String[0]));
        difficultyCombo.setSelectedItem(currentDifficulty);
        difficultyCombo.addActionListener(e -> {
            currentDifficulty = (String)difficultyCombo.getSelectedItem();
            startNewGame();
        });
        difficultyPanel.add(difficultyCombo);
        mainPanel.add(difficultyPanel);
        
        // Game info
        infoLabel = new JLabel("");
        mainPanel.add(infoLabel);
        
        // Input area
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputField = new JTextField(10);
        JButton guessButton = new JButton("Guess!");
        guessButton.addActionListener(e -> makeGuess());
        inputField.addActionListener(e -> makeGuess());
        inputPanel.add(inputField);
        inputPanel.add(guessButton);
        mainPanel.add(inputPanel);
        
        // Result display
        resultLabel = new JLabel("");
        mainPanel.add(resultLabel);
        
        // New game button
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());
        mainPanel.add(newGameButton);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void startNewGame() {
        Map<String, Integer> difficultyInfo = difficulties.get(currentDifficulty);
        targetNumber = new Random().nextInt(difficultyInfo.get("range")) + 1;
        attemptsLeft = difficultyInfo.get("attempts");
        guesses.clear();
        updateInfoLabel();
        resultLabel.setText("");
        inputField.setText("");
        inputField.requestFocus();
    }
    
    private void updateInfoLabel() {
        Map<String, Integer> difficultyInfo = difficulties.get(currentDifficulty);
        infoLabel.setText(String.format("Guess a number between 1 and %d. Attempts left: %d",
                                      difficultyInfo.get("range"), attemptsLeft));
    }
    
    private void makeGuess() {
        if (attemptsLeft <= 0) return;
        
        try {
            int guess = Integer.parseInt(inputField.getText());
            guesses.add(guess);
            attemptsLeft--;
            
            if (guess == targetNumber) {
                resultLabel.setText("Congratulations! You guessed the number!");
                showGameResult(true);
            } else if (attemptsLeft == 0) {
                resultLabel.setText("Game over! The number was " + targetNumber);
                showGameResult(false);
            } else if (guess < targetNumber) {
                resultLabel.setText("Too low, try again");
            } else {
                resultLabel.setText("Too high, try again");
            }
            
            inputField.setText("");
            updateInfoLabel();
            
        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter a valid number");
        }
    }
    
    private void showGameResult(boolean won) {
        String message = won ? 
            "Congratulations! You won!\nWould you like to play again?" :
            "Game Over! The number was " + targetNumber + "\nWould you like to try again?";
            
        int choice = JOptionPane.showConfirmDialog(
            this,
            message,
            won ? "Victory!" : "Game Over",
            JOptionPane.YES_NO_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        } else {
            dispose();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GuessNumberGame().setVisible(true);
        });
    }
} 
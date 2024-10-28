import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryGame {

    private JButton[] buttons = new JButton[16]; // 16 cards (buttons) for a 4x4 grid
    private String[] cardValues = new String[16]; // Stores the values of each card
    private ArrayList<Integer> flippedCards = new ArrayList<>(); // Stores indexes of flipped cards
    private Timer timer;
    private int matchesFound = 0;

    public MemoryGame() {
        // Set up the JFrame
        setTitle("Memory Game");
        setSize(400, 400);
        setLayout(new GridLayout(4, 4)); // 4x4 grid layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Generate pairs of cards ( "A", "A", "B", "B", etc.)
        generateCardValues();

        // Initialize buttons
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }

        // Set window visible
        setVisible(true);
    }

    // Generate random pairs of card values
    private void generateCardValues() {
        ArrayList<String> values = new ArrayList<>();
        for (char c = 'A'; c <= 'H'; c++) { // 8 pairs, so letters A to H
            values.add(String.valueOf(c));
            values.add(String.valueOf(c));
        }
        Collections.shuffle(values); // Shuffle the card values
        for (int i = 0; i < cardValues.length; i++) {
            cardValues[i] = values.get(i);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Find which button was clicked
        JButton clickedButton = (JButton) e.getSource();
        int clickedIndex = -1;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == clickedButton) {
                clickedIndex = i;
                break;
            }
        }

        // If a card is already flipped, ignore the click
        if (flippedCards.contains(clickedIndex)) {
            return;
        }

        // Show the card value
        buttons[clickedIndex].setText(cardValues[clickedIndex]);
        flippedCards.add(clickedIndex);

        // If two cards are flipped, check if they match
        if (flippedCards.size() == 2) {
            int firstCardIndex = flippedCards.get(0);
            int secondCardIndex = flippedCards.get(1);

            if (cardValues[firstCardIndex].equals(cardValues[secondCardIndex])) {
                // Cards match, leave them flipped
                matchesFound++;
                flippedCards.clear();
                
                // Check if all matches are found
                if (matchesFound == cardValues.length / 2) {
                    JOptionPane.showMessageDialog(this, "You found all pairs! You win!");
                }
            } else {
                // Cards do not match, hide them after a delay
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        buttons[firstCardIndex].setText("");
                        buttons[secondCardIndex].setText("");
                        flippedCards.clear();
                    }
                }, 1000); // 1-second delay before hiding cards
            }
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        new MemoryGame();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SlotMachine {

    // GUI 
    private Label reel1, reel2, reel3;
    private Button spinButton;
    private Label resultLabel;

    // symbols
    private String[] symbols = { "♧", "♠︎", "♔", "❤️", "⚅", "⭐", "7" };

    public SlotMachine() {
        
        setTitle("Slot Machine");
        setSize(400, 300);
        setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the reels (labels)
        Panel reelPanel = new Panel();
        reelPanel.setLayout(new GridLayout(1, 3));
        reel1 = new Label("♧", SwingConstants.CENTER);
        reel1.setFont(new Font("Arial", Font.BOLD, 60));
        reel2 = new Label("♠︎", SwingConstants.CENTER);
        reel2.setFont(new Font("Arial", Font.BOLD, 60));
        reel3 = new Label("♔", SwingConstants.CENTER);
        reel3.setFont(new Font("Arial", Font.BOLD, 60));
        reelPanel.add(reel1);
        reelPanel.add(reel2);
        reelPanel.add(reel3);
        
        // Create the spin button
        spinButton = new Button("Spin");
        spinButton.setFont(new Font("Arial", Font.BOLD, 24));
        spinButton.addActionListener(this);

        // Create a result label
        resultLabel = new Label("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Add components to the frame
        add(reelPanel, BorderLayout.CENTER);
        add(spinButton, BorderLayout.SOUTH);
        add(resultLabel, BorderLayout.NORTH);

        // Set the frame visible
        setVisible(true);
    }

    // Handle the button click event
    @Override
    public void actionPerformed(ActionEvent e) {
        // Generate random symbols for each reel
        Random rand = new Random();
        String symbol1 = symbols[rand.nextInt(symbols.length)];
        String symbol2 = symbols[rand.nextInt(symbols.length)];
        String symbol3 = symbols[rand.nextInt(symbols.length)];

        // Set the reel labels to the new symbols
        reel1.setText(symbol1);
        reel2.setText(symbol2);
        reel3.setText(symbol3);

        // Check if all reels match
        if (symbol1.equals(symbol2) && symbol2.equals(symbol3)) {
            resultLabel.setText("Jackpot! You win!");
        } else {
            resultLabel.setText("Try again!");
        }
    }

    // Main method to run the slot machine game
    public static void main(String[] args) {
        new SlotMachine();
    }
}

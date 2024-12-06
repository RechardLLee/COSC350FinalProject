package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SlotMachine extends JFrame implements ActionListener {
    // GUI 
    private JLabel reel1, reel2, reel3;
    private JButton spinButton;
    private JLabel resultLabel;

    // symbols
    private String[] symbols = { "♧", "♠︎", "♔", "❤️", "⚅", "⭐", "7" };

    public SlotMachine() {
        setTitle("Slot Machine");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create the reels (labels)
        JPanel reelPanel = new JPanel();
        reelPanel.setLayout(new GridLayout(1, 3));
        reel1 = new JLabel("♧", SwingConstants.CENTER);
        reel1.setFont(new Font("Arial", Font.BOLD, 60));
        reel2 = new JLabel("♠︎", SwingConstants.CENTER);
        reel2.setFont(new Font("Arial", Font.BOLD, 60));
        reel3 = new JLabel("♔", SwingConstants.CENTER);
        reel3.setFont(new Font("Arial", Font.BOLD, 60));
        reelPanel.add(reel1);
        reelPanel.add(reel2);
        reelPanel.add(reel3);
        
        // Create the spin button
        spinButton = new JButton("Spin");
        spinButton.setFont(new Font("Arial", Font.BOLD, 24));
        spinButton.addActionListener(this);

        // Create a result label
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Add components to the frame
        add(reelPanel, BorderLayout.CENTER);
        add(spinButton, BorderLayout.SOUTH);
        add(resultLabel, BorderLayout.NORTH);
    }

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
}

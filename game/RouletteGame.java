import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class RouletteGame extends JFrame {
    private static final int INITIAL_BALANCE = 1000;
    private int balance;
    private JLabel balanceLabel;
    private JTextArea messageArea;
    private JTextField betAmountField;
    private JComboBox<String> betTypeCombo;
    private JTextField numberField;
    private JComboBox<String> colorCombo;
    private JButton spinButton;
    private Random random;

    public RouletteGame() {
        // Initialize game
        balance = INITIAL_BALANCE;
        random = new Random();
        
        // Set up window
        setTitle("Roulette Game");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        createComponents();
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        // Top panel for balance
        JPanel topPanel = new JPanel();
        balanceLabel = new JLabel("Balance: $" + balance);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(balanceLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for messages
        messageArea = new JTextArea(8, 30);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Bet type selection
        controlPanel.add(new JLabel("Bet Type:"));
        String[] betTypes = {"Number", "Color"};
        betTypeCombo = new JComboBox<>(betTypes);
        betTypeCombo.addActionListener(e -> updateBetOptions());
        controlPanel.add(betTypeCombo);

        // Number bet
        controlPanel.add(new JLabel("Number (0-36):"));
        numberField = new JTextField();
        controlPanel.add(numberField);

        // Color bet
        controlPanel.add(new JLabel("Color:"));
        String[] colors = {"Red", "Black"};
        colorCombo = new JComboBox<>(colors);
        controlPanel.add(colorCombo);

        // Bet amount
        controlPanel.add(new JLabel("Bet Amount:"));
        betAmountField = new JTextField();
        controlPanel.add(betAmountField);

        // Spin button
        spinButton = new JButton("Spin!");
        spinButton.addActionListener(e -> spin());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(spinButton);
        controlPanel.add(buttonPanel);

        add(controlPanel, BorderLayout.SOUTH);
        updateBetOptions();
    }

    private void updateBetOptions() {
        boolean isNumberBet = betTypeCombo.getSelectedItem().equals("Number");
        numberField.setEnabled(isNumberBet);
        colorCombo.setEnabled(!isNumberBet);
    }

    private void spin() {
        try {
            // Get bet amount
            int betAmount = Integer.parseInt(betAmountField.getText());
            if (betAmount <= 0 || betAmount > balance) {
                showMessage("Invalid bet amount! Please bet between $1 and $" + balance);
                return;
            }

            // Generate result
            int rolledNumber = random.nextInt(37); // 0-36
            String rolledColor = (rolledNumber == 0) ? "Green" : 
                               (rolledNumber % 2 == 0) ? "Black" : "Red";

            // Process bet
            boolean won = false;
            int winnings = 0;

            if (betTypeCombo.getSelectedItem().equals("Number")) {
                int betNumber = Integer.parseInt(numberField.getText());
                if (betNumber < 0 || betNumber > 36) {
                    showMessage("Invalid number! Please choose 0-36");
                    return;
                }
                won = (betNumber == rolledNumber);
                winnings = won ? betAmount * 35 : 0;
            } else {
                String betColor = (String)colorCombo.getSelectedItem();
                won = betColor.equals(rolledColor);
                winnings = won ? betAmount * 2 : 0;
            }

            // Update balance
            balance -= betAmount;
            if (won) {
                balance += winnings;
            }

            // Show result
            showMessage(String.format(
                "Ball landed on: %d (%s)\n%s\n%s%d",
                rolledNumber, rolledColor,
                won ? "Congratulations! You won!" : "Sorry, you lost.",
                won ? "You won $" : "You lost $",
                won ? winnings : betAmount
            ));

            // Update balance display
            balanceLabel.setText("Balance: $" + balance);

            // Check if game over
            if (balance <= 0) {
                showMessage("Game Over! You're out of money!");
                spinButton.setEnabled(false);
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter valid numbers!");
        }
    }

    private void showMessage(String message) {
        messageArea.append(message + "\n\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RouletteGame().setVisible(true);
        });
    }
} 
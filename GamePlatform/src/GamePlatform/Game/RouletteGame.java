package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import GamePlatform.Database.DatabaseService;
import GamePlatform.User.Management.UserSession;
import GamePlatform.Main.Interfaces.MainController;
import javafx.application.Platform;

public class RouletteGame extends BaseGame {
    private int score = 0;
    private int currentBet = 0;
    private JLabel scoreLabel;
    private JTextField betAmountField;
    private JComboBox<String> betTypeCombo;
    private JTextArea messageArea;
    private JTextField numberField;
    private JComboBox<String> colorCombo;
    private JButton spinButton;
    private Random random = new Random();
    private JLabel balanceLabel;
    private int totalBet = 0;    // 总下注金额
    private int totalWin = 0;    // 总赢得金额
    private int netProfit = 0;   // 净盈亏
    private int sessionWins = 0;    // 本次游戏获胜次数
    private int sessionGames = 0;   // 本次游戏总局数
    private static int highScore = 0;
    private static String topPlayer = "";
    private boolean hasUnsavedScore = false;  // 添加标志来追踪是否有未保存的分数
    private static int totalPlayers = 0;

    public RouletteGame() {
        super("Roulette");
        loadHighScore();
        setTitle("Roulette - Player: " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        // 设置游戏图标
        try {
            String iconPath = "/GamePlatform/Game/RouletteGame.png";
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Failed to load game icon");
        }
        
        createComponents();
        updateScoreDisplay();
        
        // 添加窗口关闭监听器
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (hasUnsavedScore && netProfit != 0) {  // 只在有未保存的非零分数时保存
                    saveScore(netProfit);
                    hasUnsavedScore = false;
                }
                dispose();
            }
        });
    }

    private void createComponents() {
        // Top panel for balance and score
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        
        // 显示平台余额
        balanceLabel = new JLabel("Balance: $" + DatabaseService.getUserMoney(username));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(balanceLabel);
        
        // 显示当前游戏分数
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(scoreLabel);
        
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

        // 添加New Game按钮
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            resetGame();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(spinButton);
        buttonPanel.add(newGameButton);
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
            // 检查下注金额是否���空
            String betAmountText = betAmountField.getText().trim();
            if (betAmountText.isEmpty()) {
                showMessage("Please enter a bet amount!");
                return;
            }

            // 获取下注金额
            int betAmount = Integer.parseInt(betAmountText);
            int currentBalance = DatabaseService.getUserMoney(username);
            
            // 检查余额是否足够
            if (betAmount <= 0 || betAmount > currentBalance) {
                showMessage("Invalid bet amount! You can bet between $1 and $" + currentBalance);
                return;
            }

            // 检查下注选项
            if (betTypeCombo.getSelectedItem().equals("Number")) {
                String numberText = numberField.getText().trim();
                if (numberText.isEmpty()) {
                    showMessage("Please enter a number to bet on!");
                    return;
                }
                int betNumber = Integer.parseInt(numberText);
                if (betNumber < 0 || betNumber > 36) {
                    showMessage("Invalid number! Please choose 0-36");
                    return;
                }
            }

            // 先扣除下注金额
            DatabaseService.updateUserMoney(username, currentBalance - betAmount);
            updateBalance();

            // 随机生成结果
            int rolledNumber = random.nextInt(37); // 0-36
            String rolledColor = (rolledNumber == 0) ? "Green" : 
                               (rolledNumber % 2 == 0) ? "Black" : "Red";

            // 处理下注
            boolean won = false;
            int winnings = 0;

            if (betTypeCombo.getSelectedItem().equals("Number")) {
                int betNumber = Integer.parseInt(numberField.getText());
                if (betNumber < 0 || betNumber > 36) {
                    showMessage("Invalid number! Please choose 0-36");
                    // 返还下注金额
                    DatabaseService.updateUserMoney(username, currentBalance);
                    updateBalance();
                    return;
                }
                won = (betNumber == rolledNumber);
                winnings = won ? betAmount * 35 : 0;
            } else {
                String betColor = (String)colorCombo.getSelectedItem();
                won = betColor.equals(rolledColor);
                winnings = won ? betAmount * 2 : 0;
            }

            // 更新总下注金额和净盈亏
            totalBet += betAmount;
            netProfit -= betAmount;
            sessionGames++;     
            
            // 处理获胜情况
            if (won) {
                sessionWins++;
                DatabaseService.updateUserMoney(username, DatabaseService.getUserMoney(username) + winnings);
                totalWin += winnings;
                netProfit += winnings;
                score = netProfit;
                messageArea.append(String.format(
                    "You win! +$%d (Wins: %d/%d, Total bet: $%d, Total win: $%d, Net profit: $%d)\n",
                    winnings, sessionWins, sessionGames, totalBet, totalWin, netProfit
                ));
            } else {
                messageArea.append(String.format(
                    "Better luck next time! -$%d (Wins: %d/%d, Total bet: $%d, Total win: $%d, Net profit: $%d)\n",
                    betAmount, sessionWins, sessionGames, totalBet, totalWin, netProfit
                ));
            }
            
            hasUnsavedScore = true;  // 标记有未保存的分数
            
            // 更新显示
            updateScoreDisplay();
            updateBalance();

            // 示结果
            showMessage(String.format(
                "Ball landed on: %d (%s)\n%s",
                rolledNumber, rolledColor,
                won ? "Congratulations! You won!" : "Sorry, you lost."
            ));

        } catch (NumberFormatException e) {
            showMessage("Please enter valid numbers!");
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error processing bet!");
        }
    }

    private void updateBalance() {
        int currentBalance = DatabaseService.getUserMoney(username);
        balanceLabel.setText("Balance: $" + currentBalance);
        spinButton.setEnabled(currentBalance >= 1);
        
        Platform.runLater(() -> {
            MainController.getInstance().updateBalance();
        });
    }

    private boolean checkWin(int result) {
        // ... 检查获胜逻辑 ...
        return false;
    }

    private void showMessage(String message) {
        messageArea.append(message + "\n\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    private void resetGame() {
        if (hasUnsavedScore && netProfit != 0) {  // 只在有未保存的非零分数时保存
            saveScore(netProfit);
            hasUnsavedScore = false;
        }
        
        totalBet = 0;
        totalWin = 0;
        netProfit = 0;
        score = 0;
        sessionWins = 0;
        sessionGames = 0;
        updateScoreDisplay();
        messageArea.setText("");
        updateBalance();
    }

    private void loadHighScore() {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("game_records/Roulette.txt");
            if (java.nio.file.Files.exists(path)) {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(path);
                
                // 用于记录不重复的玩家
                java.util.Set<String> uniquePlayers = new java.util.HashSet<>();
                
                highScore = 0;  // 重置最高分
                topPlayer = ""; // 重置最高分玩家
                
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String playerName = parts[0].trim();
                        int score = Integer.parseInt(parts[1].trim());
                        
                        // 添加到唯一玩家集合
                        uniquePlayers.add(playerName);
                        
                        // 更新最高分
                        if (score > highScore) {
                            highScore = score;
                            topPlayer = playerName;
                        }
                    }
                }
                
                // 更新总玩家数
                totalPlayers = uniquePlayers.size();
                
                // 更新��示
                updateScoreDisplay();
            }
        } catch (Exception e) {
            System.err.println("Error loading high score: " + e.getMessage());
        }
    }

    @Override
    protected void saveScore(int score) {
        // 如果分数为0，直接返回，不保存记录
        if (score == 0) {
            return;
        }

        // 调用父类的saveScore方法保存记录
        super.saveScore(score);
        
        // 更新最高分和显示
        if (score > highScore) {
            highScore = score;
            topPlayer = username;
        }
        updateScoreDisplay();
    }

    private void updateScoreDisplay() {
        scoreLabel.setText(String.format("Net Profit: $%d (Win Rate: %.1f%%) (Players: %d) (High Score: $%d by %s)", 
            netProfit, 
            sessionGames > 0 ? (sessionWins * 100.0 / sessionGames) : 0,
            totalPlayers,
            highScore, 
            topPlayer));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RouletteGame().setVisible(true);
        });
    }
} 
package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import GamePlatform.Database.DatabaseService;
import GamePlatform.Main.Interfaces.MainController;
import javafx.application.Platform;

public class SlotMachine extends BaseGame {
    private JLabel[] reels = new JLabel[3];
    private JButton spinButton;
    private JLabel resultLabel;
    private JLabel balanceLabel;
    private JLabel scoreLabel;
    private JTextField betField;
    private String[] symbols = { "♠", "♥", "♦", "♣", "★", "7", "$" };  // 使用基本符号
    private int score = 0;
    private Random random = new Random();
    private Timer spinTimer;
    private int spinCount = 0;
    private final int SPIN_TIMES = 20;  // 转动次数
    private static final Color DARK_BG = new Color(44, 62, 80);
    private static final Color REEL_BG = new Color(52, 73, 94);
    private static final Color BORDER_COLOR = new Color(149, 165, 166);
    private int totalBet = 0;    // 总下注金额
    private int totalWin = 0;    // 总赢得金额
    private int netProfit = 0;   // 净盈亏

    public SlotMachine() {
        super("Slot Machine");
        setTitle("Slot Machine - Player: " + username);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        
        // 设置游戏图标
        try {
            String iconPath = getClass().getResource("/GamePlatform/Game/SlotMachine.png").getPath();
            if (iconPath != null) {
                ImageIcon icon = new ImageIcon(iconPath);
                setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            System.err.println("Failed to load game icon: " + e.getMessage());
        }
        
        createComponents();
    }

    private void createComponents() {
        // 设置整体背景色
        getContentPane().setBackground(DARK_BG);
        setLayout(new BorderLayout(0, 10));

        // 结果显示标签
        resultLabel = new JLabel("Welcome! Place your bet and spin!", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setBackground(REEL_BG);
        resultLabel.setOpaque(true);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(resultLabel, BorderLayout.NORTH);

        // 中央面板 - 老虎机轮盘
        JPanel reelPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        reelPanel.setBackground(DARK_BG);
        reelPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        for (int i = 0; i < 3; i++) {
            JPanel reelBorder = new JPanel(new BorderLayout());
            reelBorder.setBackground(BORDER_COLOR);
            reelBorder.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 3));
            
            reels[i] = new JLabel("7", SwingConstants.CENTER);  // 默认显示7
            reels[i].setFont(new Font("Dialog", Font.BOLD, 72));  // 使用Dialog字体
            reels[i].setForeground(Color.WHITE);
            reels[i].setOpaque(true);
            reels[i].setBackground(REEL_BG);
            reels[i].setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            reelBorder.add(reels[i], BorderLayout.CENTER);
            reelPanel.add(reelBorder);
        }
        add(reelPanel, BorderLayout.CENTER);

        // 底部控制面板
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(DARK_BG);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));

        // 信息面板（余额和分数）
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        infoPanel.setBackground(DARK_BG);
        
        balanceLabel = new JLabel("Balance: $" + DatabaseService.getUserMoney(username));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(Color.WHITE);
        
        scoreLabel = new JLabel("Net Profit: $0");  // 修改标签文本
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        
        infoPanel.add(balanceLabel);
        infoPanel.add(scoreLabel);
        controlPanel.add(infoPanel);
        controlPanel.add(Box.createVerticalStrut(15));

        // 下注和按钮面板
        JPanel betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        betPanel.setBackground(DARK_BG);
        
        JLabel betLabel = new JLabel("Bet Amount: $");
        betLabel.setForeground(Color.WHITE);
        betLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        betField = new JTextField(5);
        betField.setText("10");
        betField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        spinButton = createStyledButton("SPIN!", new Color(46, 204, 113));
        JButton newGameButton = createStyledButton("New Game", new Color(52, 152, 219));
        
        betPanel.add(betLabel);
        betPanel.add(betField);
        betPanel.add(spinButton);
        betPanel.add(newGameButton);
        
        controlPanel.add(betPanel);
        add(controlPanel, BorderLayout.SOUTH);

        // 添加窗口关闭监听器
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (score > 0) {
                    saveScore(score);
                    score = 0;
                }
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 30));
        
        if (text.equals("SPIN!")) {
            button.addActionListener(e -> spin());
        } else {
            button.addActionListener(e -> {
                resetGame();
            });
        }
        
        return button;
    }

    private void spin() {
        try {
            int betAmount = Integer.parseInt(betField.getText().trim());
            int currentBalance = DatabaseService.getUserMoney(username);
            
            if (betAmount <= 0 || betAmount > currentBalance) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid bet amount! Please bet between $1 and $" + currentBalance);
                return;
            }

            // 更新总下注金额
            totalBet += betAmount;
            
            // 更新净盈亏
            netProfit -= betAmount;
            score = netProfit;  // 更新分数为净盈亏
            scoreLabel.setText("Net Profit: $" + netProfit);

            // 扣除下注金额
            DatabaseService.updateUserMoney(username, currentBalance - betAmount);
            updateBalance();

            // 禁用按钮
            spinButton.setEnabled(false);
            betField.setEnabled(false);

            // 开始动画
            spinCount = 0;
            spinTimer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 随机更新符号
                    for (JLabel reel : reels) {
                        reel.setText(symbols[random.nextInt(symbols.length)]);
                    }
                    
                    spinCount++;
                    if (spinCount >= SPIN_TIMES) {
                        ((Timer)e.getSource()).stop();
                        checkResult(betAmount);
                    }
                }
            });
            spinTimer.start();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid bet amount!");
        }
    }

    private void checkResult(int betAmount) {
        // 生成最终结果
        String[] finalSymbols = new String[3];
        for (int i = 0; i < 3; i++) {
            finalSymbols[i] = symbols[random.nextInt(symbols.length)];
            reels[i].setText(finalSymbols[i]);
        }

        // 检查获胜
        if (finalSymbols[0].equals(finalSymbols[1]) && finalSymbols[1].equals(finalSymbols[2])) {
            // Jackpot!
            int winnings = betAmount * 10;
            totalWin += winnings;
            netProfit += winnings;
            score = netProfit;  // 更新分数为净盈亏
            
            int finalWinnings = winnings;
            Platform.runLater(() -> {
                DatabaseService.updateUserMoney(username, 
                    DatabaseService.getUserMoney(username) + finalWinnings);
                updateBalance();
            });
            resultLabel.setText("★ JACKPOT! You won $" + winnings + "! ★");
            scoreLabel.setText("Net Profit: $" + netProfit);
            
        } else if (finalSymbols[0].equals(finalSymbols[1]) || 
                   finalSymbols[1].equals(finalSymbols[2]) || 
                   finalSymbols[0].equals(finalSymbols[2])) {
            // 部分匹配
            int winnings = betAmount * 2;
            totalWin += winnings;
            netProfit += winnings;
            score = netProfit;  // 更新分数为净盈亏
            
            int finalWinnings = winnings;
            Platform.runLater(() -> {
                DatabaseService.updateUserMoney(username, 
                    DatabaseService.getUserMoney(username) + finalWinnings);
                updateBalance();
            });
            resultLabel.setText("Nice! You won $" + winnings + "!");
            scoreLabel.setText("Net Profit: $" + netProfit);
            
        } else {
            resultLabel.setText("Better luck next time!");
            scoreLabel.setText("Net Profit: $" + netProfit);
        }

        // 更新显示
        scoreLabel.setText("Net Profit: $" + netProfit);
        updateBalance();

        // 重新启用按钮
        spinButton.setEnabled(true);
        betField.setEnabled(true);

        if (score > 0) {
            saveScore(score);
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

    private void resetGame() {
        totalBet = 0;
        totalWin = 0;
        netProfit = 0;
        score = 0;
        scoreLabel.setText("Net Profit: $0");
        resultLabel.setText("Welcome! Place your bet and spin!");
        updateBalance();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SlotMachine().setVisible(true);
        });
    }
}

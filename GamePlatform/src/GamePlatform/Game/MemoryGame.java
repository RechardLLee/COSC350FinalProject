package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryGame extends BaseGame {
    private JButton[] buttons = new JButton[16];
    private String[] cardValues = new String[16];
    private int score = 0;
    private JLabel scoreLabel;
    private int matchesFound = 0;
    private List<Integer> flippedCards = new ArrayList<>();
    private Timer timer;
    private boolean isProcessing = false;  // 添加处理标志，防止快速点击

    public MemoryGame() {
        super("Memory Game");
        setTitle("Memory Game - Player: " + username);
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        // 设置游戏图标
        try {
            String iconPath = getClass().getResource("/GamePlatform/Game/MemoryGame.png").getPath();
            if (iconPath != null) {
                ImageIcon icon = new ImageIcon(iconPath);
                setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            System.err.println("Failed to load game icon: " + e.getMessage());
        }
        
        generateCards();
        createComponents();

        // 添加窗口关闭监听器
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (score > 0) {  // 只有在有分数且大于0时才保存
                    saveScore(score);
                    score = 0;  // 保存后重置分数，避免重复保存
                }
            }
        });
    }

    private void createComponents() {
        // 添加分数面板
        JPanel topPanel = new JPanel();
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        // 游戏面板
        JPanel gamePanel = new JPanel(new GridLayout(4, 4));
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
            final int index = i;
            buttons[i].addActionListener(e -> handleCardClick(index));
            gamePanel.add(buttons[i]);
        }
        add(gamePanel, BorderLayout.CENTER);

        // 添加New Game按钮
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            resetGame();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleCardClick(int index) {
        // 如果正在处理匹配或卡片已经翻开或游戏结束，忽略点击
        if (isProcessing || flippedCards.contains(index) || cardValues == null) {
            return;
        }

        // 如果已经翻开了两张卡片，忽略新的点击
        if (flippedCards.size() >= 2) {
            return;
        }

        // 显示卡片
        buttons[index].setText(cardValues[index]);
        flippedCards.add(index);

        // 如果翻开了两张卡片，检查是否匹配
        if (flippedCards.size() == 2) {
            isProcessing = true;  // 开始处理匹配
            int firstCard = flippedCards.get(0);
            int secondCard = flippedCards.get(1);

            boolean isMatch = cardValues[firstCard].equals(cardValues[secondCard]);
            if (isMatch) {
                score += 100;
                scoreLabel.setText("Score: " + score);
                matchesFound++;
                flippedCards.clear();
                isProcessing = false;  // 处理完成

                if (matchesFound == 8) {
                    int finalScore = score;  // 保存最终分数
                    if (finalScore > 0) {
                        saveScore(finalScore);
                    }
                    JOptionPane.showMessageDialog(this, 
                        "Congratulations! Final score: " + finalScore);
                }
            } else {
                // 不匹配，缩短延迟时间并确保正确处理
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> {
                            buttons[firstCard].setText("");
                            buttons[secondCard].setText("");
                            flippedCards.clear();
                            isProcessing = false;  // 处理完成
                        });
                    }
                }, 500);  // 从1000ms减少到500ms
            }
        }
    }

    private void generateCards() {
        ArrayList<String> values = new ArrayList<>();
        for (char c = 'A'; c <= 'H'; c++) {
            values.add(String.valueOf(c));
            values.add(String.valueOf(c));
        }
        Collections.shuffle(values);
        cardValues = values.toArray(new String[0]);
    }

    private void resetGame() {
        if (score > 0) {
            saveScore(score);  // 保存当前分数（如果有）
        }
        score = 0;
        matchesFound = 0;
        scoreLabel.setText("Score: 0");
        generateCards();
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
        }
        flippedCards.clear();
        isProcessing = false;  // 重置处理标志
    }

    // 添加游戏结束时的清理方法
    @Override
    public void dispose() {
        if (score > 0) {
            saveScore(score);  // 确保保存最终分数
        }
        if (timer != null) {
            timer.cancel();  // 清理定时器
        }
        super.dispose();
    }
}

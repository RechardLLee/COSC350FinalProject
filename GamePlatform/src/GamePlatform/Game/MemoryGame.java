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
        // 如果卡片已经翻开或游戏结束，忽略点击
        if (flippedCards.contains(index) || cardValues == null) {
            return;
        }

        // 显示卡片
        buttons[index].setText(cardValues[index]);
        flippedCards.add(index);

        // 如果翻开了两张卡片，检查是否匹配
        if (flippedCards.size() == 2) {
            int firstCard = flippedCards.get(0);
            int secondCard = flippedCards.get(1);

            boolean isMatch = cardValues[firstCard].equals(cardValues[secondCard]);
            if (isMatch) {
                score += 100;
                scoreLabel.setText("Score: " + score);
                matchesFound++;
                flippedCards.clear();

                if (matchesFound == 8) {
                    if (score > 0) {
                        saveScore(score);
                        score = 0;  // 保存后重置分数
                    }
                    JOptionPane.showMessageDialog(this, 
                        "Congratulations! Final score: " + score);
                }
            } else {
                // 不匹配，延迟翻回
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> {
                            buttons[firstCard].setText("");
                            buttons[secondCard].setText("");
                            flippedCards.clear();
                        });
                    }
                }, 1000);
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
        score = 0;
        matchesFound = 0;
        scoreLabel.setText("Score: 0");
        generateCards();  // 重新生成卡片
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
        }
        flippedCards.clear();
    }
}

package GamePlatform.Game.BlackJackGui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import GamePlatform.Game.BaseGame;
import GamePlatform.Database.DatabaseService;
import GamePlatform.User.Management.UserSession;
import java.awt.image.BufferedImage;

public class BlackJackGame extends BaseGame {
    private class Card {
        String value;
        String type;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String toString() {
            return value + "-" + type;
        }

        public int getValue() {
            if ("AJQK".contains(value)) { //A J Q K
                if (value == "A") {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value); //2-10
        }

        public boolean isAce() {
            return value == "A";
        }

        public String getImagePath() {
            return "/GamePlatform/Game/BlackJackGui/cards/" + toString() + ".png";
        }
    }

    ArrayList<Card> deck;
    Random random = new Random(); //shuffle deck

    //dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    //player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    //window
    int boardWidth = 600;
    int boardHeight = boardWidth;

    int cardWidth = 110; //ratio should 1/1.4
    int cardHeight = 154;

    public JFrame frame;
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            try {
                //draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if (!stayButton.isEnabled()) {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                //draw dealer's hand
                for (int i = 0; i < dealerHand.size(); i++) {
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, cardHeight, null);
                }

                //draw player's hand
                for (int i = 0; i < playerHand.size(); i++) {
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5)*i, 320, cardWidth, cardHeight, null);
                }

                if (!stayButton.isEnabled()) {
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();

                    String message = "";
                    if (playerSum > 21) {
                        message = "You Lose!";
                    }
                    else if (dealerSum > 21) {
                        message = "You Win!";
                    }
                    //both you and dealer <= 21
                    else if (playerSum == dealerSum) {
                        message = "Tie!";
                    }
                    else if (playerSum > dealerSum) {
                        message = "You Win!";
                    }
                    else if (playerSum < dealerSum) {
                        message = "You Lose!";
                    }

                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JTextField betField = new JTextField(10);
    JButton nextGameButton = new JButton("Next Game");
    private int betAmount = 0;

    private String username;
    private int score = 0;

    public BlackJackGame() {
        super("Black Jack");
        username = UserSession.getCurrentUser();
        startGame();

        frame = new JFrame("Black Jack - Player: " + username);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(false);
        
        // 设置游戏图标
        try {
            String iconPath = "/GamePlatform/Game/BlackJackGui/BlackJack.png";
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            frame.setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Failed to load game icon");
            // 如果加载失败，使用空图标
            frame.setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        }

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);

        buttonPanel.add(new JLabel("Bet: "));
        buttonPanel.add(betField);

        nextGameButton.setFocusable(false);
        nextGameButton.setEnabled(false);
        buttonPanel.add(nextGameButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                if (reducePlayerAce() > 21) { //A + 2 + J --> 1 + 2 + J
                    hitButton.setEnabled(false); 
                }
                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealerSum < 17) {
                    Card card = deck.remove(deck.size()-1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                }
                checkGameOver();
                gamePanel.repaint();
            }
        });

        nextGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);
                nextGameButton.setEnabled(false);
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();

        // 添加窗口关闭监听器
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (score != 0) {
                    BlackJackGame.this.saveScore(score);
                }
            }
        });
    }

    private void checkGameOver() {
        boolean gameOver = (playerSum > 21 || !stayButton.isEnabled());
        if (gameOver) {
            nextGameButton.setEnabled(true);
            
            // 验证下注金
            if (!validateBet()) {
                return;
            }
            
            // 计算分数
            int finalScore;
            if (playerSum > 21) {
                finalScore = -betAmount;
            } else if (dealerSum > 21) {
                finalScore = betAmount * 2;
            } else if (playerSum > dealerSum) {
                finalScore = betAmount * 2;
            } else if (playerSum == dealerSum) {
                finalScore = 0;
            } else {
                finalScore = -betAmount;
            }
            
            // 更新用户余额
            if (finalScore != 0) {
                saveScore(finalScore);
                score = finalScore;
            }
        }
    }

    public void startGame() {
        //deck
        buildDeck();
        shuffleDeck();

        //dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size()-1); //remove card at last index
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size()-1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        //player
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }
    }

    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }
    }

    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

    private boolean validateBet() {
        String betText = betField.getText().trim();
        try {
            betAmount = Integer.parseInt(betText);
            if (betAmount <= 0) {
                JOptionPane.showMessageDialog(frame, "Please enter a positive bet amount.");
                return false;
            }
            
            // 检查余额是否足够
            int currentBalance = DatabaseService.getUserMoney(username);
            if (betAmount > currentBalance) {
                JOptionPane.showMessageDialog(frame, 
                    "Insufficient balance. Your current balance is: $" + currentBalance);
                return false;
            }
            
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid bet. Please enter a number.");
            return false;
        }
    }

    // 添加显示方法
    public void showGame() {
        frame.setVisible(true);
        frame.requestFocus();
    }
}

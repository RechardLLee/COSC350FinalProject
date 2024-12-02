package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SnakeGame extends JFrame {
    private static final int CELL_SIZE = 20;
    private static final int GRID_WIDTH = 30;
    private static final int GRID_HEIGHT = 25;
    private static final int GAME_WIDTH = GRID_WIDTH * CELL_SIZE;
    private static final int GAME_HEIGHT = GRID_HEIGHT * CELL_SIZE;
    private static final int WINDOW_HEIGHT = GAME_HEIGHT + 100;
    
    private ArrayList<Point> snake;
    private Point food;
    private ArrayList<Point> obstacles;
    private String direction;
    private int score;
    private int highScore;
    private int gameSpeed;
    private boolean gameRunning;
    
    private GamePanel gamePanel;
    private JLabel scoreLabel;
    private JLabel highScoreLabel;
    private JButton startButton;
    private JButton restartButton;
    private JComboBox<String> difficultyCombo;
    private Random random = new Random();
    private javax.swing.Timer gameTimer;
    
    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GAME_WIDTH + 16, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(GAME_WIDTH + 16, WINDOW_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(44, 62, 80));
        setLayout(new BorderLayout());
        
        snake = new ArrayList<>();
        obstacles = new ArrayList<>();
        
        // 初始化游戏定时器
        gameTimer = new javax.swing.Timer(gameSpeed, e -> {
            if (gameRunning) {
                moveSnake();
                gamePanel.repaint();
            }
        });
        
        createComponents();
        initializeGame();
        
        addWindowListener();
        setLocationRelativeTo(null);
        requestFocus();
    }
    
    private void createComponents() {
        // Game title
        JLabel titleLabel = new JLabel("Snake Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);
        
        // Game panel with scroll pane
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        gamePanel.setBackground(new Color(52, 73, 94));
        
        // Create scroll pane for game panel
        JScrollPane scrollPane = new JScrollPane(gamePanel);
        scrollPane.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Add scroll pane to frame
        add(scrollPane, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(new Color(44, 62, 80));
        
        // Difficulty selector
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setSelectedItem("Medium");
        difficultyCombo.addActionListener(e -> setDifficulty());
        controlPanel.add(difficultyCombo);
        
        // Buttons
        startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());
        controlPanel.add(startButton);
        
        restartButton = new JButton("Restart Game");
        restartButton.addActionListener(e -> restartGame());
        restartButton.setEnabled(false);
        controlPanel.add(restartButton);
        
        // Score labels
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        highScoreLabel = new JLabel("High Score: 0");
        highScoreLabel.setForeground(Color.WHITE);
        controlPanel.add(scoreLabel);
        controlPanel.add(highScoreLabel);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        // Key listener
        addKeyListener(new GameKeyListener());
        setFocusable(true);
    }
    
    private void initializeGame() {
        snake = new ArrayList<>();
        int centerX = GRID_WIDTH / 2;
        int centerY = GRID_HEIGHT / 2;
        snake.add(new Point(centerX, centerY));
        snake.add(new Point(centerX - 1, centerY));
        snake.add(new Point(centerX - 2, centerY));
        
        direction = "Right";
        score = 0;
        gameRunning = false;
        
        createObstacles();
        generateFood();
        setDifficulty();
        
        // 启动游戏定时器
        if (gameTimer != null) {
            gameTimer.start();
        }
    }
    
    private void generateFood() {
        // 确保食物不会生成在蛇身上或障碍物上
        while (true) {
            int x = random.nextInt(GRID_WIDTH);
            int y = random.nextInt(GRID_HEIGHT);
            Point newFood = new Point(x, y);
            
            // 检查是否与蛇身重叠
            if (snake.contains(newFood)) {
                continue;
            }
            
            // 检查是否与障碍物重叠
            if (obstacles.contains(newFood)) {
                continue;
            }
            
            food = newFood;
            break;
        }
    }
    
    private void checkCollision() {
        Point head = snake.get(0);
        
        // 检查是否吃到食物
        if (head.equals(food)) {
            score += 10;
            if (score > highScore) {
                highScore = score;
            }
            updateScore();
            
            // 立即生成新的食物
            generateFood();
            
            // 不移除蛇尾，让蛇变长
            return;
        }
        
        // 移除蛇尾
        snake.remove(snake.size() - 1);
        
        // 检查是否撞到墙
        if (head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT) {
            gameOver();
            return;
        }
        
        // 检查是否撞到自己
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                return;
            }
        }
        
        // 检查是否撞到障碍物
        if (obstacles.contains(head)) {
            gameOver();
            return;
        }
    }
    
    private void setDifficulty() {
        String difficulty = (String)difficultyCombo.getSelectedItem();
        switch (difficulty) {
            case "Easy": gameSpeed = 150; break;
            case "Medium": gameSpeed = 100; break;
            case "Hard": gameSpeed = 50; break;
        }
        
        // 更新游戏定时器的延迟
        if (gameTimer != null) {
            gameTimer.setDelay(gameSpeed);
        }
    }
    
    private void startGame() {
        if (!gameRunning) {
            gameRunning = true;
            startButton.setEnabled(false);
            restartButton.setEnabled(true);
            difficultyCombo.setEnabled(false);
            gameTimer.start();
        }
    }
    
    private void restartGame() {
        gameTimer.stop();
        gameRunning = false;
        initializeGame();
        startButton.setEnabled(true);
        restartButton.setEnabled(false);
        difficultyCombo.setEnabled(true);
        scoreLabel.setText("Score: 0");
        gamePanel.repaint();
    }
    
    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        
        switch (direction) {
            case "Right": newHead.x = (head.x + 1) % GRID_WIDTH; break;
            case "Left": newHead.x = (head.x - 1 + GRID_WIDTH) % GRID_WIDTH; break;
            case "Up": newHead.y = (head.y - 1 + GRID_HEIGHT) % GRID_HEIGHT; break;
            case "Down": newHead.y = (head.y + 1) % GRID_HEIGHT; break;
        }
        
        if (snake.contains(newHead) || obstacles.contains(newHead)) {
            gameOver();
            return;
        }
        
        snake.add(0, newHead);
        
        if (newHead.equals(food)) {
            score++;
            scoreLabel.setText("Score: " + score);
            generateFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }
    
    private void gameOver() {
        gameRunning = false;
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, 
            "Game Over!\nScore: " + score + "\nHigh Score: " + highScore,
            "Game Over", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateScore() {
        scoreLabel.setText("Score: " + score);
        highScoreLabel.setText("High Score: " + highScore);
    }
    
    private void createObstacles() {
        obstacles.clear();  // 清除现有障碍物
        
        // 根据难度设置障碍物数量
        int obstacleCount;
        String difficulty = (String)difficultyCombo.getSelectedItem();
        switch (difficulty) {
            case "Easy": obstacleCount = 5; break;
            case "Medium": obstacleCount = 10; break;
            case "Hard": obstacleCount = 15; break;
            default: obstacleCount = 10;
        }
        
        // 生成障碍物
        for (int i = 0; i < obstacleCount; i++) {
            while (true) {
                int x = random.nextInt(GRID_WIDTH);
                int y = random.nextInt(GRID_HEIGHT);
                Point obstacle = new Point(x, y);
                
                // 确保障碍物不会生成在蛇身上或食物位置
                if (!snake.contains(obstacle) && 
                    (food == null || !obstacle.equals(food)) && 
                    !obstacles.contains(obstacle)) {
                    obstacles.add(obstacle);
                    break;
                }
            }
        }
    }
    
    private class GamePanel extends JPanel {
        public GamePanel() {
            setDoubleBuffered(true);
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(GAME_WIDTH, GAME_HEIGHT);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Optional: Draw grid lines
            g.setColor(new Color(44, 62, 80));
            for (int x = 0; x < GRID_WIDTH; x++) {
                for (int y = 0; y < GRID_HEIGHT; y++) {
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
            
            // Draw snake
            for (Point p : snake) {
                g.setColor(new Color(46, 204, 113));
                g.fillRect(p.x * CELL_SIZE + 1, p.y * CELL_SIZE + 1, 
                          CELL_SIZE - 2, CELL_SIZE - 2);
                g.setColor(new Color(39, 174, 96));
                g.drawRect(p.x * CELL_SIZE + 1, p.y * CELL_SIZE + 1, 
                          CELL_SIZE - 2, CELL_SIZE - 2);
            }
            
            // Draw food
            g.setColor(new Color(231, 76, 60));
            g.fillOval(food.x * CELL_SIZE + 2, food.y * CELL_SIZE + 2, 
                      CELL_SIZE - 4, CELL_SIZE - 4);
            g.setColor(new Color(192, 57, 43));
            g.drawOval(food.x * CELL_SIZE + 2, food.y * CELL_SIZE + 2, 
                      CELL_SIZE - 4, CELL_SIZE - 4);
            
            // Draw obstacles
            for (Point p : obstacles) {
                g.setColor(new Color(149, 165, 166));
                g.fillRect(p.x * CELL_SIZE + 1, p.y * CELL_SIZE + 1, 
                          CELL_SIZE - 2, CELL_SIZE - 2);
                g.setColor(new Color(127, 140, 141));
                g.drawRect(p.x * CELL_SIZE + 1, p.y * CELL_SIZE + 1, 
                          CELL_SIZE - 2, CELL_SIZE - 2);
            }
        }
    }
    
    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    if (!direction.equals("Down")) direction = "Up";
                    break;
                case KeyEvent.VK_S:
                    if (!direction.equals("Up")) direction = "Down";
                    break;
                case KeyEvent.VK_A:
                    if (!direction.equals("Right")) direction = "Left";
                    break;
                case KeyEvent.VK_D:
                    if (!direction.equals("Left")) direction = "Right";
                    break;
            }
        }
    }
    
    // 添加窗口关闭事件监听器
    private void addWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 停止游戏定时器
                if (gameTimer != null && gameTimer.isRunning()) {
                    gameTimer.stop();
                }
                gameRunning = false;
                dispose();  // 释放窗口资源
            }
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame().setVisible(true);
        });
    }
} 
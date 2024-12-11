package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import GamePlatform.User.Management.UserSession;
import GamePlatform.Utility.LanguageUtil;

public class SnakeGame extends BaseGame {
    private static final int CELL_SIZE = 20;
    private static int GRID_WIDTH = 23;  // 从30改为23
    private static int GRID_HEIGHT = 19;  // 从25改为19
    private int gameWidth;
    private int gameHeight;
    private int windowHeight;
    
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
    private String currentPlayer;
    private boolean isPaused = false;
    private String lastValidDirection = "Right";
    private JFrame controlFrame; // 新增控制窗口
    
    public SnakeGame() {
        super("Snake");
        
        currentPlayer = UserSession.getCurrentUser();
        
        // 游戏主窗口设置
        GRID_WIDTH = 23;
        GRID_HEIGHT = 19;
        gameWidth = GRID_WIDTH * CELL_SIZE;
        gameHeight = GRID_HEIGHT * CELL_SIZE;
        
        setTitle("Snake Game - Player: " + currentPlayer);
        setSize(500, 450);  // 固定窗口大小为 500x450
        setMinimumSize(new Dimension(500, 450));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(44, 62, 80));
        setLayout(new BorderLayout(0, 10));
        
        // 初始化游戏组件
        snake = new ArrayList<>();
        obstacles = new ArrayList<>();
        
        gameTimer = new javax.swing.Timer(100, e -> {
            if (gameRunning) {
                moveSnake();
                gamePanel.repaint();
            }
        });
        
        createGameComponents(); // 创建游戏主窗口组件
        createControlFrame();   // 创建控制窗口
        initializeGame();
        
        addWindowListener();
        setLocationRelativeTo(null);
        requestFocus();
    }
    
    private void createGameComponents() {
        // 游戏主窗口只包含游戏面板
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(gameWidth, gameHeight));
        gamePanel.setBackground(new Color(52, 73, 94));
        
        JPanel gamePanelContainer = new JPanel(new BorderLayout());
        gamePanelContainer.setBackground(new Color(44, 62, 80));
        gamePanelContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gamePanelContainer.add(gamePanel, BorderLayout.CENTER);
        
        add(gamePanelContainer, BorderLayout.CENTER);
        addKeyListener(new GameKeyListener());
        setFocusable(true);
    }
    
    private void createControlFrame() {
        controlFrame = new JFrame("Snake Game Controls");
        controlFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        controlFrame.setLayout(new BorderLayout());
        controlFrame.setResizable(false);
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(44, 62, 80));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 游戏控制区域
        JPanel gameControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        gameControlPanel.setBackground(new Color(44, 62, 80));
        
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setSelectedItem("Medium");
        difficultyCombo.addActionListener(e -> setDifficulty());
        
        startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            startGame();
            requestFocus(); // 确保游戏窗口获得焦点
        });
        
        restartButton = new JButton("Restart Game");
        restartButton.addActionListener(e -> {
            restartGame();
            requestFocus(); // 确保游戏窗口获得焦点
        });
        restartButton.setEnabled(false);
        
        gameControlPanel.add(difficultyCombo);
        gameControlPanel.add(startButton);
        gameControlPanel.add(restartButton);
        
        // 分数显示区域
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scorePanel.setBackground(new Color(44, 62, 80));
        
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        highScoreLabel = new JLabel("High Score: 0");
        highScoreLabel.setForeground(Color.WHITE);
        
        scorePanel.add(scoreLabel);
        scorePanel.add(new JLabel("  |  ", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
        }});
        scorePanel.add(highScoreLabel);
        
        // 控制说明区域
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlsPanel.setBackground(new Color(44, 62, 80));
        
        JLabel controlsLabel = new JLabel(
            "<html><center>Controls:<br>" +
            "WASD - Move snake<br>" +
            "P - Pause/Resume<br>" +
            "R - Restart game</center></html>",
            SwingConstants.CENTER
        );
        controlsLabel.setForeground(Color.WHITE);
        controlsPanel.add(controlsLabel);
        
        // 添加所有组件到控制面板
        controlPanel.add(gameControlPanel);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(scorePanel);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(controlsPanel);
        
        controlFrame.add(controlPanel);
        controlFrame.pack();
        
        // 设置控制窗口位置（在游戏窗口右侧）
        controlFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose(); // 关闭控制窗口时同时关闭游戏窗口
            }
        });
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (controlFrame != null) {
            // 将控制窗口放在游戏窗口右侧
            if (visible) {
                Point loc = getLocation();
                controlFrame.setLocation(loc.x + getWidth() + 10, loc.y);
                controlFrame.setVisible(true);
            } else {
                controlFrame.setVisible(false);
            }
        }
    }
    
    @Override
    public void dispose() {
        if (controlFrame != null) {
            controlFrame.dispose();
        }
        super.dispose();
    }
    
    private void initializeGame() {
        snake.clear();
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
        
        scoreLabel.setText("Score: 0");
        highScoreLabel.setText("High Score: " + highScore);
        
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }
    
    private void generateFood() {
        ArrayList<Point> availablePositions = new ArrayList<>();
        
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                Point p = new Point(x, y);
                if (!snake.contains(p) && !obstacles.contains(p)) {
                    availablePositions.add(p);
                }
            }
        }
        
        if (availablePositions.isEmpty()) {
            gameOver();
            return;
        }
        
        food = availablePositions.get(random.nextInt(availablePositions.size()));
        System.out.println("New food generated at: " + food.x + "," + food.y);
        
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }
    
    private void setDifficulty() {
        String difficulty = (String)difficultyCombo.getSelectedItem();
        switch (difficulty) {
            case "Easy": gameSpeed = 150; break;
            case "Medium": gameSpeed = 100; break;
            case "Hard": gameSpeed = 50; break;
        }
        
        if (gameTimer != null) {
            gameTimer.setDelay(gameSpeed);
        }
        
        if (gameRunning) {
            restartGame();
        }
    }
    
    private void startGame() {
        if (!gameRunning) {
            gameRunning = true;
            startButton.setEnabled(false);
            restartButton.setEnabled(true);
            difficultyCombo.setEnabled(true);
            generateFood();
            gameTimer.start();
        }
    }
    
    private void restartGame() {
        snake.clear();
        int centerX = GRID_WIDTH / 2;
        int centerY = GRID_HEIGHT / 2;
        snake.add(new Point(centerX, centerY));
        snake.add(new Point(centerX - 1, centerY));
        snake.add(new Point(centerX - 2, centerY));
        
        direction = "Right";
        score = 0;
        scoreLabel.setText("Score: 0");
        
        createObstacles();
        generateFood();
        
        gameRunning = true;
        gameTimer.start();
        
        gamePanel.repaint();
        requestFocus();
    }
    
    private void moveSnake() {
        if (!gameRunning || isPaused) return;
        
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        
        switch (direction) {
            case "Right": newHead.x = head.x + 1; break;
            case "Left": newHead.x = head.x - 1; break;
            case "Up": newHead.y = head.y - 1; break;
            case "Down": newHead.y = head.y + 1; break;
        }
        
        lastValidDirection = direction;
        
        if (newHead.x < 0 || newHead.x >= GRID_WIDTH || 
            newHead.y < 0 || newHead.y >= GRID_HEIGHT) {
            gameOver();
            return;
        }
        
        for (Point p : snake) {
            if (newHead.equals(p)) {
                gameOver();
                return;
            }
        }
        
        if (obstacles.contains(newHead)) {
            gameOver();
            return;
        }
        
        snake.add(0, newHead);
        
        if (food != null && newHead.equals(food)) {
            score += 10;
            if (score > highScore) {
                highScore = score;
            }
            updateScore();
            generateFood();
        } else {
            snake.remove(snake.size() - 1);
        }
        
        gamePanel.repaint();
    }
    
    private void gameOver() {
        gameRunning = false;
        gameTimer.stop();
        
        if (score > 0) {
            saveScore(score);
        }
        
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("High Score: " + highScore);
        }
        
        String message = String.format(
            "Game Over!\nScore: %d\nHigh Score: %d\nWould you like to play again?",
            score, highScore
        );
        
        int option = JOptionPane.showConfirmDialog(
            this,
            message,
            "Game Over",
            JOptionPane.YES_NO_OPTION
        );
        
        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            dispose();
        }
    }
    
    private void updateScore() {
        scoreLabel.setText(String.format("Score: %d High Score: %d", score, highScore));
    }
    
    private void createObstacles() {
        obstacles.clear();
        
        int obstacleCount;
        String difficulty = (String)difficultyCombo.getSelectedItem();
        switch (difficulty) {
            case "Easy": obstacleCount = 3; break;    // 减少障碍物数量
            case "Medium": obstacleCount = 6; break;
            case "Hard": obstacleCount = 9; break;
            default: obstacleCount = 6;
        }
        
        for (int i = 0; i < obstacleCount; i++) {
            while (true) {
                int x = random.nextInt(GRID_WIDTH);
                int y = random.nextInt(GRID_HEIGHT);
                Point obstacle = new Point(x, y);
                
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
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.setColor(new Color(44, 62, 80));
            for (int x = 0; x < GRID_WIDTH; x++) {
                for (int y = 0; y < GRID_HEIGHT; y++) {
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
            
            if (snake != null && !snake.isEmpty()) {
                g.setColor(new Color(46, 204, 113));
                for (int i = 1; i < snake.size(); i++) {
                    Point p = snake.get(i);
                    g.fillRect(p.x * CELL_SIZE + 1, p.y * CELL_SIZE + 1, 
                             CELL_SIZE - 2, CELL_SIZE - 2);
                }
                
                Point head = snake.get(0);
                g.setColor(new Color(39, 174, 96));
                g.fillRect(head.x * CELL_SIZE + 1, head.y * CELL_SIZE + 1, 
                         CELL_SIZE - 2, CELL_SIZE - 2);
            }
            
            if (food != null) {
                g.setColor(new Color(231, 76, 60));
                g.fillOval(food.x * CELL_SIZE + 2, food.y * CELL_SIZE + 2, 
                         CELL_SIZE - 4, CELL_SIZE - 4);
            }
            
            if (obstacles != null) {
                g.setColor(new Color(149, 165, 166));
                for (Point p : obstacles) {
                    g.fillRect(p.x * CELL_SIZE + 1, p.y * CELL_SIZE + 1, 
                             CELL_SIZE - 2, CELL_SIZE - 2);
                }
            }
        }
    }
    
    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!gameRunning) return;
            
            switch (e.getKeyCode()) {
                case KeyEvent.VK_P:
                    togglePause();
                    break;
                case KeyEvent.VK_R:
                    restartGame();
                    break;
                case KeyEvent.VK_W:
                    if (!lastValidDirection.equals("Down") && !lastValidDirection.equals("Up")) {
                        direction = "Up";
                    }
                    break;
                case KeyEvent.VK_S:
                    if (!lastValidDirection.equals("Up") && !lastValidDirection.equals("Down")) {
                        direction = "Down";
                    }
                    break;
                case KeyEvent.VK_A:
                    if (!lastValidDirection.equals("Right") && !lastValidDirection.equals("Left")) {
                        direction = "Left";
                    }
                    break;
                case KeyEvent.VK_D:
                    if (!lastValidDirection.equals("Left") && !lastValidDirection.equals("Right")) {
                        direction = "Right";
                    }
                    break;
            }
        }
    }
    
    private void addWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (gameTimer != null && gameTimer.isRunning()) {
                    gameTimer.stop();
                }
                gameRunning = false;
                dispose();
            }
        });
    }
    
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            gameTimer.stop();
            startButton.setText("Resume");
        } else {
            gameTimer.start();
            startButton.setText("Pause");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame().setVisible(true);
        });
    }
} 
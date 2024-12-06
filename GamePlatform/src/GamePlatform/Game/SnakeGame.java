package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import GamePlatform.User.Management.UserSession;
import GamePlatform.Utility.LanguageUtil;

public class SnakeGame extends BaseGame {
    private static final int CELL_SIZE = 20;
    private static int GRID_WIDTH = 30;  // 默认宽度
    private static int GRID_HEIGHT = 25;  // 默认高度
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
    
    public SnakeGame() {
        super("Snake");
        
        currentPlayer = UserSession.getCurrentUser();
        
        gameWidth = GRID_WIDTH * CELL_SIZE;
        gameHeight = GRID_HEIGHT * CELL_SIZE;
        windowHeight = gameHeight + 100;
        
        setTitle("Snake Game - Player: " + currentPlayer);
        setSize(gameWidth + 16, windowHeight);
        setMinimumSize(new Dimension(gameWidth + 16, windowHeight));
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(44, 62, 80));
        setLayout(new BorderLayout());
        
        snake = new ArrayList<>();
        obstacles = new ArrayList<>();
        
        gameTimer = new javax.swing.Timer(100, e -> {
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
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (gamePanel != null) {
                    gamePanel.repaint();
                }
            }
        });
    }
    
    private void createComponents() {
        JLabel titleLabel = new JLabel("Snake Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);
        
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(gameWidth, gameHeight));
        gamePanel.setBackground(new Color(52, 73, 94));
        
        JScrollPane scrollPane = new JScrollPane(gamePanel);
        scrollPane.setPreferredSize(new Dimension(gameWidth, gameHeight));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(new Color(44, 62, 80));
        
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setSelectedItem("Medium");
        difficultyCombo.addActionListener(e -> setDifficulty());
        controlPanel.add(difficultyCombo);
        
        startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());
        controlPanel.add(startButton);
        
        restartButton = new JButton("Restart Game");
        restartButton.addActionListener(e -> restartGame());
        restartButton.setEnabled(false);
        controlPanel.add(restartButton);
        
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        highScoreLabel = new JLabel("High Score: 0");
        highScoreLabel.setForeground(Color.WHITE);
        controlPanel.add(scoreLabel);
        controlPanel.add(highScoreLabel);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        addKeyListener(new GameKeyListener());
        setFocusable(true);
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
        
        if (food.x < 0 || food.x >= GRID_WIDTH || food.y < 0 || food.y >= GRID_HEIGHT) {
            generateFood();
            return;
        }
        
        System.out.println("New food generated at: " + food.x + "," + food.y);
        
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }
    
    private void checkCollision() {
        Point head = snake.get(0);
        
        if (head.equals(food)) {
            score += 10;
            if (score > highScore) {
                highScore = score;
            }
            updateScore();
            
            generateFood();
            
            return;
        }
        
        snake.remove(snake.size() - 1);
        
        if (head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT) {
            gameOver();
            return;
        }
        
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                return;
            }
        }
        
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
        if (!gameRunning) return;
        
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        
        switch (direction) {
            case "Right": 
                newHead.x = head.x + 1;
                break;
            case "Left": 
                newHead.x = head.x - 1;
                break;
            case "Up": 
                newHead.y = head.y - 1;
                break;
            case "Down": 
                newHead.y = head.y + 1;
                break;
        }
        
        if (newHead.x < 0 || newHead.x >= GRID_WIDTH || 
            newHead.y < 0 || newHead.y >= GRID_HEIGHT) {
            gameOver();
            return;
        }
        
        if (snake.contains(newHead) || obstacles.contains(newHead)) {
            gameOver();
            return;
        }
        
        snake.add(0, newHead);
        
        if (newHead.equals(food)) {
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
            case "Easy": obstacleCount = 5; break;
            case "Medium": obstacleCount = 10; break;
            case "Hard": obstacleCount = 15; break;
            default: obstacleCount = 10;
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
        public GamePanel() {
            setDoubleBuffered(true);
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(gameWidth, gameHeight);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            
            GRID_WIDTH = Math.max(30, panelWidth / CELL_SIZE);
            GRID_HEIGHT = Math.max(25, panelHeight / CELL_SIZE);
            
            gameWidth = GRID_WIDTH * CELL_SIZE;
            gameHeight = GRID_HEIGHT * CELL_SIZE;
            
            g.setColor(new Color(44, 62, 80));
            for (int x = 0; x < GRID_WIDTH; x++) {
                for (int y = 0; y < GRID_HEIGHT; y++) {
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
            
            if (snake != null) {
                g.setColor(new Color(46, 204, 113));
                for (int i = 1; i < snake.size(); i++) {
                    Point p = snake.get(i);
                    g.fillRect(p.x * CELL_SIZE + 1, p.y * CELL_SIZE + 1, 
                              CELL_SIZE - 2, CELL_SIZE - 2);
                }
                
                if (!snake.isEmpty()) {
                    Point head = snake.get(0);
                    g.setColor(new Color(39, 174, 96));
                    g.fillRect(head.x * CELL_SIZE + 1, head.y * CELL_SIZE + 1, 
                              CELL_SIZE - 2, CELL_SIZE - 2);
                }
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame().setVisible(true);
        });
    }
} 
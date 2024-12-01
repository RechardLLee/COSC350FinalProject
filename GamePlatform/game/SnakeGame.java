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
    
    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GAME_WIDTH + 16, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(GAME_WIDTH + 16, WINDOW_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(44, 62, 80));
        setLayout(new BorderLayout());
        
        snake = new ArrayList<>();
        obstacles = new ArrayList<>();
        
        createComponents();
        initializeGame();
        
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
        createFood();
        setDifficulty();
    }
    
    private void createFood() {
        if (obstacles == null) {
            obstacles = new ArrayList<>();
        }
        
        Random rand = new Random();
        while (true) {
            int x = rand.nextInt(GRID_WIDTH);
            int y = rand.nextInt(GRID_HEIGHT);
            Point newFood = new Point(x, y);
            if (!snake.contains(newFood) && !obstacles.contains(newFood)) {
                food = newFood;
                break;
            }
        }
    }
    
    private void createObstacles() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            while (true) {
                int x = rand.nextInt(GRID_WIDTH);
                int y = rand.nextInt(GRID_HEIGHT);
                Point obstacle = new Point(x, y);
                if (!snake.contains(obstacle) && !obstacle.equals(food) && 
                    !obstacles.contains(obstacle)) {
                    obstacles.add(obstacle);
                    break;
                }
            }
        }
    }
    
    private void setDifficulty() {
        String difficulty = (String)difficultyCombo.getSelectedItem();
        switch (difficulty) {
            case "Easy": gameSpeed = 150; break;
            case "Medium": gameSpeed = 100; break;
            case "Hard": gameSpeed = 50; break;
        }
    }
    
    private void startGame() {
        if (!gameRunning) {
            gameRunning = true;
            startButton.setEnabled(false);
            restartButton.setEnabled(true);
            difficultyCombo.setEnabled(false);
            
            new Thread(() -> {
                while (gameRunning) {
                    moveSnake();
                    gamePanel.repaint();
                    try {
                        Thread.sleep(gameSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    
    private void restartGame() {
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
            createFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }
    
    private void gameOver() {
        gameRunning = false;
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("High Score: " + highScore);
        }
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
        restartGame();
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame().setVisible(true);
        });
    }
} 
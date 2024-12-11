package GamePlatform.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import GamePlatform.User.Management.UserSession;

public class HanoiTowerGame extends BaseGame {
    private JPanel canvas;
    private Map<String, Integer> difficulties;
    private String currentDifficulty;
    private int numDisks;
    private ArrayList<Tower> towers;
    private Tower selectedTower;
    private int moves;
    private int minMoves;
    private JLabel movesLabel;
    private String currentPlayer;
    
    public HanoiTowerGame() {
        super("Hanoi Tower");
        currentPlayer = UserSession.getCurrentUser();
        
        setTitle("Hanoi Tower - Player: " + currentPlayer);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        difficulties = new HashMap<>();
        difficulties.put("Easy", 3);
        difficulties.put("Medium", 4);
        difficulties.put("Hard", 5);
        
        currentDifficulty = "Easy";
        numDisks = difficulties.get(currentDifficulty);
        
        createWidgets();
        resetGame();
    }
    
    private void createWidgets() {
        setLayout(new BorderLayout());
        
        // Create canvas
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };
        canvas.setPreferredSize(new Dimension(800, 400));
        add(canvas, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        
        // Difficulty selection
        JLabel diffLabel = new JLabel("Difficulty:");
        String[] diffOptions = {"Easy", "Medium", "Hard"};
        JComboBox<String> diffCombo = new JComboBox<>(diffOptions);
        diffCombo.setSelectedItem(currentDifficulty);
        diffCombo.addActionListener(e -> {
            currentDifficulty = (String)diffCombo.getSelectedItem();
            numDisks = difficulties.get(currentDifficulty);
            resetGame();
        });
        
        // Buttons
        JButton resetBtn = new JButton("Reset Game");
        resetBtn.addActionListener(e -> resetGame());
        
        JButton solveBtn = new JButton("Auto Solve");
        solveBtn.addActionListener(e -> autoSolve());
        
        JButton explainBtn = new JButton("Algorithm Explanation");
        explainBtn.addActionListener(e -> showAlgorithmExplanation());
        
        // Moves label
        movesLabel = new JLabel("Moves: 0");
        
        // Add components to control panel
        controlPanel.add(diffLabel);
        controlPanel.add(diffCombo);
        controlPanel.add(resetBtn);
        controlPanel.add(solveBtn);
        controlPanel.add(explainBtn);
        controlPanel.add(movesLabel);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        // Add mouse event listener
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }
    
    private void drawGame(Graphics g) {
        // Draw towers and disks
        for (Tower tower : towers) {
            tower.draw(g);
        }
    }
    
    private void resetGame() {
        towers = new ArrayList<>();
        towers.add(new Tower(200));  // Left tower
        towers.add(new Tower(400));  // Middle tower
        towers.add(new Tower(600));  // Right tower
        
        selectedTower = null;
        moves = 0;
        minMoves = Integer.MAX_VALUE;
        movesLabel.setText("Moves: " + moves);
        
        // Place disks on the first tower
        for (int i = numDisks; i > 0; i--) {
            towers.get(0).addDisk(new Disk(i));
        }
        
        canvas.repaint();
    }
    
    private void handleClick(int x, int y) {
        Tower clickedTower = getClickedTower(x);
        if (clickedTower == null) return;
        
        if (selectedTower == null) {
            if (!clickedTower.isEmpty()) {
                selectedTower = clickedTower;
                clickedTower.setSelected(true);
                canvas.repaint();
            }
        } else {
            if (clickedTower != selectedTower) {
                if (isValidMove(selectedTower, clickedTower)) {
                    moveDisk(selectedTower, clickedTower);
                    moves++;
                    movesLabel.setText("Moves: " + moves);
                    
                    if (checkWin()) {
                        JOptionPane.showMessageDialog(this, 
                            "Congratulations! You won in " + moves + " moves!", 
                            "Victory", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid move! Cannot place a larger disk on a smaller one.", 
                        "Invalid Move", 
                        JOptionPane.WARNING_MESSAGE);
                }
            }
            selectedTower.setSelected(false);
            selectedTower = null;
            canvas.repaint();
        }
    }
    
    private Tower getClickedTower(int x) {
        for (Tower tower : towers) {
            if (Math.abs(tower.getX() - x) < 50) {
                return tower;
            }
        }
        return null;
    }
    
    private boolean isValidMove(Tower from, Tower to) {
        if (from.isEmpty()) return false;
        if (to.isEmpty()) return true;
        return from.getTopDisk().getSize() < to.getTopDisk().getSize();
    }
    
    private void moveDisk(Tower from, Tower to) {
        to.addDisk(from.removeDisk());
    }
    
    private boolean checkWin() {
        if (towers.get(2).getDisks().size() == numDisks) {
            int minMoves = (1 << numDisks) - 1;
            int score = (int)(10000.0 * minMoves / moves);
            
            JOptionPane.showMessageDialog(this,
                String.format("Congration!\nMove Time: %d\nLess moves: %d\nScores: %d",
                    moves, minMoves, score),
                "Victory",
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
        }
        return false;
    }
    
    private void autoSolve() {
        resetGame();
        new Thread(() -> {
            solveHanoi(numDisks, towers.get(0), towers.get(2), towers.get(1));
        }).start();
    }
    
    private void solveHanoi(int n, Tower source, Tower target, Tower auxiliary) {
        if (n > 0) {
            solveHanoi(n - 1, source, auxiliary, target);
            try {
                Thread.sleep(500);  // Add delay for observation
                SwingUtilities.invokeLater(() -> {
                    moveDisk(source, target);
                    moves++;
                    movesLabel.setText("Moves: " + moves);
                    canvas.repaint();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            solveHanoi(n - 1, auxiliary, target, source);
        }
    }
    
    private void showAlgorithmExplanation() {
        String explanation = 
            "Hanoi Tower Algorithm Explanation:\n\n" +
            "Basic Principles:\n" +
            "1. Move n-1 disks from source pole to auxiliary pole\n" +
            "2. Move the largest disk from source pole to target pole\n" +
            "3. Move n-1 disks from auxiliary pole to target pole\n\n" +
            "Recursive Algorithm:\n" +
            "1. If n=1, move the disk directly from source to target\n" +
            "2. Otherwise:\n" +
            "   a. Move n-1 disks from source to auxiliary (recursive)\n" +
            "   b. Move the nth disk from source to target\n" +
            "   c. Move n-1 disks from auxiliary to target (recursive)";
        
        JTextArea textArea = new JTextArea(explanation);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Algorithm Explanation", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HanoiTowerGame().setVisible(true);
        });
    }
    
    private static class Disk {
        private int size;
        private int width;
        private int height;
        private Color color;
        
        public Disk(int size) {
            this.size = size;
            this.width = size * 20;
            this.height = 20;
            this.color = getColor(size);
        }
        
        private Color getColor(int size) {
            Color[] colors = {
                Color.RED, Color.ORANGE, Color.YELLOW,
                Color.GREEN, Color.BLUE, new Color(75, 0, 130),
                new Color(238, 130, 238)
            };
            return colors[size % colors.length];
        }
        
        public void draw(Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x - width/2, y - height/2, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x - width/2, y - height/2, width, height);
        }
        
        public int getSize() { return size; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
    }
    
    private static class Tower {
        private int x;
        private ArrayList<Disk> disks;
        private boolean selected = false;
        
        public Tower(int x) {
            this.x = x;
            this.disks = new ArrayList<>();
        }
        
        public void draw(Graphics g) {
            // Draw tower base
            g.setColor(new Color(139, 69, 19));
            g.fillRect(x - 50, 350, 100, 20);
            g.fillRect(x - 5, 100, 10, 250);
            
            // Draw disks
            for (int i = 0; i < disks.size(); i++) {
                disks.get(i).draw(g, x, 350 - i * 20 - 10);
            }
            
            // Draw highlight effect if tower is selected
            if (selected) {
                g.setColor(new Color(255, 255, 0, 100));
                g.fillRect(x - 50, 100, 100, 270);
            }
        }
        
        public void addDisk(Disk disk) {
            disks.add(disk);
        }
        
        public Disk removeDisk() {
            if (!disks.isEmpty()) {
                return disks.remove(disks.size() - 1);
            }
            return null;
        }
        
        public boolean isEmpty() {
            return disks.isEmpty();
        }
        
        public int getX() {
            return x;
        }
        
        public Disk getTopDisk() {
            if (!disks.isEmpty()) {
                return disks.get(disks.size() - 1);
            }
            return null;
        }
        
        public void setSelected(boolean selected) {
            this.selected = selected;
        }
        
        public ArrayList<Disk> getDisks() {
            return disks;
        }
    }
} 
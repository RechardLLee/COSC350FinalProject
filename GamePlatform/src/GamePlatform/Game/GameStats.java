package GamePlatform.Game;

public class GameStats {
    private int totalPlayers;
    private int highScore;
    private String topPlayer;
    private double averageScore;
    
    // Getters
    public int getTotalPlayers() { return totalPlayers; }
    public int getHighScore() { return highScore; }
    public String getTopPlayer() { return topPlayer; }
    public double getAverageScore() { return averageScore; }
    
    // Setters
    public void setTotalPlayers(int totalPlayers) { this.totalPlayers = totalPlayers; }
    public void setHighScore(int highScore) { this.highScore = highScore; }
    public void setTopPlayer(String topPlayer) { this.topPlayer = topPlayer; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }
} 
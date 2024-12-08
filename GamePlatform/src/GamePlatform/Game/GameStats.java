package GamePlatform.Game;

import java.sql.Timestamp;

public class GameStats {
    // 游戏统计字段
    private int totalPlayers;
    private int highScore;
    private String topPlayer;
    private double averageScore;
    
    // 游戏记录字段
    private String gameName;
    private String username;
    private int score;
    private Timestamp playTime;

    public GameStats() {
    }

    // 游戏统计相关的getter和setter
    public int getTotalPlayers() { return totalPlayers; }
    public void setTotalPlayers(int totalPlayers) { this.totalPlayers = totalPlayers; }
    
    public int getHighScore() { return highScore; }
    public void setHighScore(int highScore) { this.highScore = highScore; }
    
    public String getTopPlayer() { return topPlayer; }
    public void setTopPlayer(String topPlayer) { this.topPlayer = topPlayer; }
    
    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }

    // 游戏记录相关的getter和setter
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public Timestamp getPlayTime() { return playTime; }
    public void setPlayTime(Timestamp playTime) { this.playTime = playTime; }
} 
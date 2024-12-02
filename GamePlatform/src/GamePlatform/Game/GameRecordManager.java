package GamePlatform.Game;

import java.io.*;
import java.util.*;
import java.nio.file.*;

public class GameRecordManager {
    private static final String RECORDS_DIR = "game_records";
    
    public static void saveGameRecord(String username, String gameName, int score) {
        try {
            // 确保目录存在
            Files.createDirectories(Paths.get(RECORDS_DIR));
            
            // 游戏记录文件路径
            Path recordFile = Paths.get(RECORDS_DIR, gameName + ".txt");
            
            // 读取现有记录
            List<String> records = new ArrayList<>();
            if (Files.exists(recordFile)) {
                records = Files.readAllLines(recordFile);
            }
            
            // 添加新记录
            records.add(String.format("%s,%d,%s", username, score, 
                new java.util.Date().toString()));
            
            // 写入文件
            Files.write(recordFile, records);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static GameStats getGameStats(String gameName) {
        GameStats stats = new GameStats();
        Path recordFile = Paths.get(RECORDS_DIR, gameName + ".txt");
        
        if (Files.exists(recordFile)) {
            try {
                List<String> records = Files.readAllLines(recordFile);
                Set<String> players = new HashSet<>();
                int maxScore = Integer.MIN_VALUE;
                String topPlayer = "";
                
                for (String record : records) {
                    String[] parts = record.split(",");
                    String player = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    
                    players.add(player);
                    if (score > maxScore) {
                        maxScore = score;
                        topPlayer = player;
                    }
                }
                
                stats.setTotalPlayers(players.size());
                stats.setTotalGames(records.size());
                stats.setHighScore(maxScore);
                stats.setTopPlayer(topPlayer);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return stats;
    }
    
    public static class GameStats {
        private int totalPlayers;
        private int totalGames;
        private int highScore;
        private String topPlayer;
        
        // Getters and setters
        public int getTotalPlayers() { return totalPlayers; }
        public void setTotalPlayers(int totalPlayers) { this.totalPlayers = totalPlayers; }
        public int getTotalGames() { return totalGames; }
        public void setTotalGames(int totalGames) { this.totalGames = totalGames; }
        public int getHighScore() { return highScore; }
        public void setHighScore(int highScore) { this.highScore = highScore; }
        public String getTopPlayer() { return topPlayer; }
        public void setTopPlayer(String topPlayer) { this.topPlayer = topPlayer; }
    }
} 
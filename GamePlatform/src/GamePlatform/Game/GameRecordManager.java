package GamePlatform.Game;

import java.nio.file.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import GamePlatform.Database.DatabaseService;

public class GameRecordManager {
    private static final String RECORDS_DIR = "game_records";
    
    // 保存游戏记录到文件和数据库
    public static void saveGameRecord(String username, String gameName, int score) {
        // 只保存非零分数
        if (score != 0) {
            try {
                // 确保目录存在
                Files.createDirectories(Paths.get(RECORDS_DIR));
                
                Path scoreFile = Paths.get(RECORDS_DIR, gameName + ".txt");
                String scoreRecord = String.format("%s,%d,%s%n", 
                    username,           // 用户名
                    score,             // 分数
                    new Date()         // 时间
                );
                Files.write(scoreFile, scoreRecord.getBytes(), 
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    
                System.out.println("Score saved: " + scoreRecord);
                
                // 保存到数据库
                DatabaseService.saveGameScore(username, gameName, score);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    // 从文件加载游戏记录
    public static List<GameRecord> loadGameRecords(String gameName) {
        List<GameRecord> records = new ArrayList<>();
        Path scoreFile = Paths.get(RECORDS_DIR, gameName + ".txt");
        
        try {
            if (Files.exists(scoreFile)) {
                List<String> lines = Files.readAllLines(scoreFile);
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        try {
                            String[] parts = line.split(",");
                            if (parts.length >= 3) {
                                String player = parts[0].trim();
                                int score = Integer.parseInt(parts[1].trim());
                                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
                                    .parse(parts[2].trim());
                                
                                records.add(new GameRecord(player, score, date));
                                System.out.println("Loaded record: " + line);
                            }
                        } catch (Exception e) {
                            System.err.println("Error parsing line: " + line);
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return records;
    }
    
    // 同步文件记录到数据库
    public static void syncRecordsToDatabase(String gameName) {
        List<GameRecord> records = loadGameRecords(gameName);
        for (GameRecord record : records) {
            DatabaseService.saveGameScore(record.getPlayer(), gameName, record.getScore());
        }
    }
} 
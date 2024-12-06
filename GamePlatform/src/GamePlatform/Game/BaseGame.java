package GamePlatform.Game;

import javax.swing.JFrame;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import GamePlatform.Database.DatabaseService;
import GamePlatform.User.Management.UserSession;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class BaseGame extends JFrame {
    protected String username;
    protected String gameName;
    protected long startTime;
    protected int score;
    private static final String SCORES_DIR = "game_records";
    
    public BaseGame(String gameName) {
        this.username = UserSession.getCurrentUser();
        this.gameName = gameName;
        this.startTime = System.currentTimeMillis();
        this.score = 0;
        
        // 确保分数目录存在
        try {
            Files.createDirectories(Paths.get(SCORES_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 添加窗口关闭事件监听器，确保游戏结束时保存分数
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveScore(score);
            }
        });
    }
    
    protected void saveScore(int score) {
        // 只保存大于0的分数
        if (score > 0) {
            // 保存到数据库
            DatabaseService.saveGameScore(username, gameName, score);
            
            // 保存到本地文件
            try {
                Path scoreFile = Paths.get(SCORES_DIR, gameName + ".txt");
                String scoreRecord = String.format("%s,%d,%s%n", 
                    username,           // 用户名
                    score,             // 分数
                    new Date()         // 时间
                );
                Files.write(scoreFile, scoreRecord.getBytes(), 
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                
                System.out.println("Score saved: " + scoreRecord);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    protected GameStats getGameStats() {
        return DatabaseService.getGameStats(gameName);
    }
} 
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
        // 使用GameRecordManager保存记录
        GameRecordManager.saveGameRecord(username, gameName, score);
    }
    
    protected GameStats getGameStats() {
        return DatabaseService.getGameStats(gameName);
    }
} 
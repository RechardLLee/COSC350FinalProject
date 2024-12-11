package GamePlatform.Game;

import javax.swing.JFrame;
import GamePlatform.Game.GameRecordManager;
import GamePlatform.User.Management.UserSession;
import GamePlatform.Database.DatabaseService;

public abstract class BaseGame extends JFrame {
    protected String username;
    protected String gameName;
    
    public BaseGame(String gameName) {
        this.gameName = gameName;
        this.username = UserSession.getCurrentUser();
    }
    
    protected void saveScore(int score) {
        if (score != 0) {
            // 更新用户余额
            int currentBalance = DatabaseService.getUserMoney(username);
            DatabaseService.updateUserMoney(username, currentBalance + score);
            // 保存游戏记录
            GameRecordManager.saveGameRecord(username, gameName, score);
        }
    }
} 
package GamePlatform.Game;

import javax.swing.JFrame;

public abstract class BaseGame extends JFrame {
    protected String username;
    protected String gameName;
    protected long startTime;
    protected int score;
    
    public BaseGame(String title) {
        super(title);
        this.username = GameSession.getCurrentUser();
        this.gameName = GameSession.getCurrentGame();
        this.startTime = System.currentTimeMillis();
        this.score = 0;
    }
    
    protected void recordScore() {
        int playTime = (int)((System.currentTimeMillis() - startTime) / 1000);
        GameAPI.recordGameScore(gameName, username, score, playTime);
    }
    
    protected void showStats() {
        Map<String, Object> stats = GameAPI.getGameStats(gameName);
        showGameStats(stats);
    }
    
    protected abstract void showGameStats(Map<String, Object> stats);
} 
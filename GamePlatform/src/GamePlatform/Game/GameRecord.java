package GamePlatform.Game;

import java.util.Date;

public class GameRecord {
    private final String player;
    private final int score;
    private final Date date;
    
    public GameRecord(String player, int score, Date date) {
        this.player = player;
        this.score = score;
        this.date = date;
    }
    
    public String getPlayer() { return player; }
    public int getScore() { return score; }
    public Date getDate() { return date; }
} 
package GamePlatform.Game.bingo;

import java.util.Date;
import GamePlatform.Game.GameRecord;

public class BingoGameRecord extends GameRecord {
    private String pattern;  // 获胜图案类型
    private int roundNumber; // 回合数
    
    public BingoGameRecord(String player, int score, Date date, String pattern, int roundNumber) {
        super(player, score, date);
        this.pattern = pattern;
        this.roundNumber = roundNumber;
    }
    
    public String getPattern() { return pattern; }
    public int getRoundNumber() { return roundNumber; }
} 
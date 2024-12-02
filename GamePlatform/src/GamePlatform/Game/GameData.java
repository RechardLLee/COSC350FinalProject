package GamePlatform.Game;

import java.util.Date;

public class GameData {
    private String gameName;
    private String owner;
    private Date purchaseDate;
    private int playTime;
    
    public GameData(String gameName, String owner, Date purchaseDate, int playTime) {
        this.gameName = gameName;
        this.owner = owner;
        this.purchaseDate = purchaseDate;
        this.playTime = playTime;
    }
    
    // Getters
    public String getGameName() { return gameName; }
    public String getOwner() { return owner; }
    public Date getPurchaseDate() { return purchaseDate; }
    public int getPlayTime() { return playTime; }
} 
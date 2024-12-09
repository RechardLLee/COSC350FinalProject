package GamePlatform.User.Management;

import java.util.Date;
import GamePlatform.Database.DatabaseService;

public class UserData {
    private final int id;
    private final String username;
    private final String email;
    private final Date createdDate;
    private int money;
    
    public UserData(int id, String username, String email, Date createdDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdDate = createdDate;
        this.money = DatabaseService.getUserMoney(username);
    }
    
    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Date getCreatedDate() { return createdDate; }
    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }
} 
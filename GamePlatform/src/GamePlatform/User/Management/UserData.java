package GamePlatform.User.Management;

import java.util.Date;

public class UserData {
    private int id;
    private String username;
    private String email;
    private Date createdDate;
    
    public UserData(int id, String username, String email, Date createdDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdDate = createdDate;
    }
    
    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Date getCreatedDate() { return createdDate; }
} 
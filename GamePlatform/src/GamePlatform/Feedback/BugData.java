package GamePlatform.Feedback;

import java.util.Date;

public class BugData {
    private int id;
    private String username;
    private String description;
    private String status;
    private Date reportDate;
    
    public BugData(int id, String username, String description, String status, Date reportDate) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.status = status;
        this.reportDate = reportDate;
    }
    
    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public Date getReportDate() { return reportDate; }
} 
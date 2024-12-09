package GamePlatform.Models;

import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardData {
    private int totalUsers;
    private int totalGames;
    private int activeUsers;
    private Map<String, Integer> activityData;
    
    public DashboardData() {
        this.activityData = new LinkedHashMap<>();
    }
    
    public int getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public int getTotalGames() {
        return totalGames;
    }
    
    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }
    
    public int getActiveUsers() {
        return activeUsers;
    }
    
    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }
    
    public Map<String, Integer> getActivityData() {
        return activityData;
    }
    
    public void addActivityData(String date, int count) {
        this.activityData.put(date, count);
    }
} 
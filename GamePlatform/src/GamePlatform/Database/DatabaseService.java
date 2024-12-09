package GamePlatform.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import GamePlatform.Game.GameStats;
import GamePlatform.User.Management.UserData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import GamePlatform.Models.DashboardData;
import GamePlatform.Feedback.ReviewData;
import GamePlatform.Feedback.BugData;
import java.io.PrintWriter;
import java.nio.file.StandardCopyOption;

public class DatabaseService {
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:gameplatform.db";
    
    // developer username password security answer
    private static final String DEV_USERNAME = "admin";
    private static final String DEV_PASSWORD = "admin";
    private static final String DEV_SECURITY_ANSWER = "admin";
    
    private static final String USER_DATA_FILE = "user_data.txt";
    private static Map<String, Integer> userMoney = new HashMap<>();
    
    static {
        try {
            Class.forName(DRIVER);
            initializeDatabase();
            loadUserData();
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading SQLite JDBC driver:");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    
    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 只在表不存在时创建表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Users (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT UNIQUE NOT NULL," +
                "    email TEXT UNIQUE NOT NULL," +
                "    password TEXT NOT NULL," +
                "    money INTEGER DEFAULT 1000," +
                "    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS UserGames (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT NOT NULL," +
                "    game_name TEXT NOT NULL," +
                "    game_path TEXT NOT NULL," +
                "    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (username) REFERENCES Users(username)," +
                "    UNIQUE(username, game_name)" +
                ")"
            );
            
            // 创建GameReviews表（依赖于Users表）
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS GameReviews (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT NOT NULL," +
                "    game_name TEXT NOT NULL," +
                "    rating INTEGER," +
                "    review TEXT," +
                "    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
            // 创建BugReports表（依赖于Users表）
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS BugReports (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT NOT NULL," +
                "    description TEXT," +
                "    report_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    status TEXT DEFAULT 'Open'," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
            // 创建GameScores表（依赖于Users
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS GameScores (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT NOT NULL," +
                "    game_name TEXT NOT NULL," +
                "    score INTEGER NOT NULL," +
                "    play_time INTEGER NOT NULL," + // 游戏时长（秒）
                "    play_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
            // 创建游戏记录表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS game_records (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT NOT NULL," +
                "    game_name TEXT NOT NULL," +
                "    score INTEGER NOT NULL," +
                "    play_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
            // 添加 game_stats 表的创建
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS game_stats (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    game_name TEXT NOT NULL," +
                "    username TEXT NOT NULL," +
                "    score INTEGER NOT NULL," +
                "    play_time TIMESTAMP NOT NULL," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
            // 只在admin用户不存在时创建
            String checkAdmin = "SELECT COUNT(*) FROM Users WHERE username = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            if (rs.next() && rs.getInt(1) == 0) {
                String sql = "INSERT INTO Users (username, email, password, money) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, "admin@admin.com");
                    pstmt.setString(3, "admin");
                    pstmt.setInt(4, 1000);
                    pstmt.executeUpdate();
                    System.out.println("Created default admin account");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error initializing database tables:");
            e.printStackTrace();
        }
    }
    
    // 验证开发者登录
    public static boolean validateDeveloper(String username, String password, String securityAnswer) {
        System.out.println("Attempting developer login with:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Security Answer: " + securityAnswer);
        
        boolean isValid = DEV_USERNAME.equals(username) && 
                         DEV_PASSWORD.equals(password) && 
                         DEV_SECURITY_ANSWER.equals(securityAnswer);
                         
        System.out.println("Login result: " + isValid);
        return isValid;
    }
    
    // 获取所有用户
    public static ResultSet getUsers() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(
            "SELECT id, " +
            "username, " +
            "email, " +
            "created_date " +
            "FROM Users " +
            "ORDER BY id"
        );
    }
    
    // 删除用户
    public static boolean deleteUser(String username) {
        String[] queries = {
            "DELETE FROM GameReviews WHERE username = ?",
            "DELETE FROM BugReports WHERE username = ?",
            "DELETE FROM Users WHERE username = ?"
        };
        
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            
            for (String query : queries) {
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    pstmt.executeUpdate();
                }
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 重置数据库
    public static boolean resetDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 删除所有表
            stmt.executeUpdate("DROP TABLE IF EXISTS GameReviews");
            stmt.executeUpdate("DROP TABLE IF EXISTS BugReports");
            stmt.executeUpdate("DROP TABLE IF EXISTS Users");
            
            // 重创建表
            initializeDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 验证用户登录
    public static boolean validateUser(String username, String password) {
        System.out.println("Attempting to validate user: " + username);
        
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            boolean isValid = rs.next();
            
            System.out.println("Login result: " + isValid);
            
            // 如果登录成功，保存用户在userMoney中有记录
            if (isValid && !userMoney.containsKey(username)) {
                int money = rs.getInt("money");
                userMoney.put(username, money);
                saveUserData();
                System.out.println("Added user money record: " + username + " -> " + money);
            }
            
            return isValid;
        } catch (SQLException e) {
            System.err.println("Error validating user:");
            e.printStackTrace();
            return false;
        }
    }
    
    // 注册新用户
    public static boolean registerUser(String username, String email, String password) {
        System.out.println("Attempting to register user: " + username);
        
        if (isEmailRegistered(email)) {
            System.out.println("Email already registered: " + email);
            return false;
        }

        // 修改 SQL 语句,添加 money 字段
        String sql = "INSERT INTO Users(username, email, password, money) VALUES(?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setInt(4, 1000);  // 设置初始余额
            
            int result = pstmt.executeUpdate();
            System.out.println("Registration result: " + result);
            
            if (result > 0) {
                userMoney.put(username, 1000);  // 更新内存中的余额
                saveUserData();  // 保存到文件
                System.out.println("User registered successfully");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error registering user:");
            e.printStackTrace();
        }
        return false;
    }
    
    // 保存游戏评分
    public static boolean saveGameReview(String username, String gameName, int rating, String review) {
        String sql = "INSERT INTO GameReviews (username, game_name, rating, review) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, gameName);
            pstmt.setInt(3, rating);
            pstmt.setString(4, review);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 保存问题反馈
    public static boolean saveBugReport(String username, String description) {
        String sql = "INSERT INTO BugReports (username, description) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, description);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean isEmailRegistered(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateUserPassword(String email, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateUsername(String email, String newUsername) {
        String sql = "UPDATE Users SET username = ? WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newUsername);
            pstmt.setString(2, email);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 获取用户余额
    public static int getUserMoney(String username) {
        String sql = "SELECT money FROM Users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int money = rs.getInt("money");
                userMoney.put(username, money);  // 更新内存中的余额
                return money;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userMoney.getOrDefault(username, 1000);  // 如果查询失败,返回内存中的余额或默认值
    }
    
    // 更新用户余额
    public static void updateUserMoney(String username, int newAmount) {
        // 更新数据库中的余额
        String sql = "UPDATE Users SET money = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newAmount);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            
            // 更新内存中的余额
            userMoney.put(username, newAmount);
            saveUserData();  // 保存到文件
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 添加用户游戏
    public static boolean addUserGame(String username, String gameName, String gamePath) {
        String sql = "INSERT INTO UserGames(username, game_name, game_path) VALUES(?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, gameName);
            pstmt.setString(3, gamePath);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 获取用户的游戏列表
    public static Map<String, String> getUserGames(String username) {
        Map<String, String> games = new HashMap<>();
        String sql = "SELECT game_name, game_path FROM UserGames WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                games.put(rs.getString("game_name"), rs.getString("game_path"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return games;
    }
    
    public static boolean saveGameScore(String username, String gameName, int score) {
        String sql = "INSERT INTO game_records (username, game_name, score, play_date) " +
                     "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
                
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, gameName);
            pstmt.setInt(3, score);
            
            int result = pstmt.executeUpdate();
            
            // 调试输出
            System.out.println(String.format("Score saved to DB - User: %s, Game: %s, Score: %d", 
                username, gameName, score));
                
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static GameStats getGameStats(String gameName) {
        GameStats stats = new GameStats();
        String sql = 
            "WITH GameRecords AS (" +
            "    SELECT * FROM game_records WHERE game_name = ?" +
            ")" +
            "SELECT " +
            "    (SELECT COUNT(DISTINCT username) FROM GameRecords) as total_players, " +
            "    MAX(score) as high_score, " +
            "    (SELECT username FROM GameRecords WHERE score = (SELECT MAX(score) FROM GameRecords) LIMIT 1) as top_player, " +
            "    AVG(score) as avg_score " +
            "FROM GameRecords";
            
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, gameName);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int totalPlayers = rs.getInt("total_players");
                int highScore = rs.getInt("high_score");
                String topPlayer = rs.getString("top_player");
                double avgScore = rs.getDouble("avg_score");
                
                stats.setTotalPlayers(totalPlayers);
                stats.setHighScore(highScore);
                stats.setTopPlayer(topPlayer);
                stats.setAverageScore(avgScore);
                
                // 调试输出
                System.out.println("Game: " + gameName);
                System.out.println("Total Players: " + totalPlayers);
                System.out.println("High Score: " + highScore);
                System.out.println("Top Player: " + topPlayer);
                System.out.println("Average Score: " + avgScore);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stats;
    }
    
    public static List<UserData> getAllUsers() {
        List<UserData> users = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY created_date DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(new UserData(
                    rs.getInt("id"),
                    rs.getString("username"), 
                    rs.getString("email"),
                    rs.getTimestamp("created_date")
                ));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    public static void loadUserData() {
        try {
            File file = new File(USER_DATA_FILE);
            if (file.exists()) {
                List<String> lines = Files.readAllLines(file.toPath());
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        userMoney.put(parts[0], Integer.parseInt(parts[1]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveUserData() {
        try {
            List<String> lines = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : userMoney.entrySet()) {
                lines.add(entry.getKey() + "," + entry.getValue());
            }
            Files.write(Paths.get(USER_DATA_FILE), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveGameStats(GameStats gameStats) {
        try {
            Connection conn = getConnection();
            String sql = "INSERT INTO game_stats (game_name, username, score, play_time) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, gameStats.getGameName());
            pstmt.setString(2, gameStats.getUsername());
            pstmt.setInt(3, gameStats.getScore());
            pstmt.setTimestamp(4, gameStats.getPlayTime());
            
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 仪表盘数据
    public static DashboardData getDashboardData() {
        DashboardData data = new DashboardData();
        
        try (Connection conn = getConnection()) {
            // 获取总用户数
            String sql = "SELECT COUNT(*) FROM Users";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            if (rs.next()) {
                data.setTotalUsers(rs.getInt(1));
            }
            
            // 获取活跃用户数
            sql = "SELECT COUNT(DISTINCT username) FROM game_records " +
                  "WHERE DATE(play_date) = DATE('now')";
            rs = conn.createStatement().executeQuery(sql);
            if (rs.next()) {
                data.setActiveUsers(rs.getInt(1));
            }
            
            // 获取游戏数据
            sql = "SELECT COUNT(DISTINCT game_name) FROM game_records";
            rs = conn.createStatement().executeQuery(sql);
            if (rs.next()) {
                data.setTotalGames(rs.getInt(1));
            }
            
            // 获取活跃度趋势
            sql = "WITH RECURSIVE dates(date) AS (" +
                  "  SELECT DATE('now', '-6 days')" +
                  "  UNION ALL" +
                  "  SELECT DATE(date, '+1 day')" +
                  "  FROM dates" +
                  "  WHERE date < DATE('now')" +
                  ")" +
                  "SELECT dates.date, COALESCE(COUNT(DISTINCT username), 0) as count " +
                  "FROM dates LEFT JOIN game_records " +
                  "ON DATE(game_records.play_date) = dates.date " +
                  "GROUP BY dates.date " +
                  "ORDER BY dates.date";
                  
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                data.addActivityData(
                    rs.getString("date"),
                    rs.getInt("count")
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return data;
    }
    
    // 获取所有评论
    public static List<ReviewData> getAllReviews() {
        List<ReviewData> reviews = new ArrayList<>();
        String sql = "SELECT * FROM GameReviews ORDER BY review_date DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reviews.add(new ReviewData(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("game_name"),
                    rs.getInt("rating"),
                    rs.getString("review"),
                    rs.getTimestamp("review_date")
                ));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reviews;
    }
    
    // 获取所有bug报告
    public static List<BugData> getAllBugReports() {
        List<BugData> bugs = new ArrayList<>();
        String sql = "SELECT * FROM BugReports ORDER BY report_date DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bugs.add(new BugData(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getTimestamp("report_date")
                ));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bugs;
    }
    
    // 更新bug状态
    public static void updateBugStatus(int bugId, String newStatus) {
        String sql = "UPDATE BugReports SET status = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, bugId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 删除评论
    public static void deleteReview(int reviewId) {
        String sql = "DELETE FROM GameReviews WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reviewId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 删除bug报告
    public static void deleteBugReport(int bugId) {
        String sql = "DELETE FROM BugReports WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bugId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 导出评论到CSV
    public static void exportReviews(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("ID,User,Game,Rating,Review,Date");
            
            String sql = "SELECT * FROM GameReviews ORDER BY review_date DESC";
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    writer.printf("%d,%s,%s,%d,\"%s\",%s%n",
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("game_name"),
                        rs.getInt("rating"),
                        rs.getString("review").replace("\"", "\"\""),
                        rs.getTimestamp("review_date")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 导出bug报告到CSV
    public static void exportBugReports(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("ID,User,Description,Status,Date");
            
            String sql = "SELECT * FROM BugReports ORDER BY report_date DESC";
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    writer.printf("%d,%s,\"%s\",%s,%s%n",
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("description").replace("\"", "\"\""),
                        rs.getString("status"),
                        rs.getTimestamp("report_date")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean backupDatabase(File destination) {
        try {
            File sourceFile = new File("gameplatform.db");
            Files.copy(sourceFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean restoreDatabase(File source) {
        try {
            File destFile = new File("gameplatform.db");
            Files.copy(source.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<String> getSystemLogs() {
        List<String> logs = new ArrayList<>();
        String sql = "SELECT * FROM system_logs ORDER BY created_date DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                logs.add(String.format("[%s] %s: %s",
                    rs.getTimestamp("created_date"),
                    rs.getString("type"),
                    rs.getString("content")
                ));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return logs;
    }
    
    public static boolean clearCache() {
        try {
            Files.deleteIfExists(Paths.get("cache"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
} 
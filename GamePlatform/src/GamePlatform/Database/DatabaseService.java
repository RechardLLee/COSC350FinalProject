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
            
            // 创建GameScores表（依赖于Users表）
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
            
            // 如果登录成功，确保用户在userMoney中有记录
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

        String sql = "INSERT INTO Users(username, email, password, money) VALUES(?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setInt(4, 1000);
            
            int result = pstmt.executeUpdate();
            System.out.println("Registration result: " + result);
            
            if (result > 0) {
                userMoney.put(username, 1000);
                saveUserData();
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
        return userMoney.getOrDefault(username, 1000);  // 默认1000
    }
    
    // 更新用户余额
    public static void updateUserMoney(String username, int newAmount) {
        userMoney.put(username, newAmount);
        saveUserData();  // 立即保存到文件
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
        String sql = "SELECT * FROM Users ORDER BY id";
        
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
} 
package GamePlatform.Database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import GamePlatform.Game.GameStats;

public class DatabaseService {
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:gameplatform.db";
    
    // developer username password security answer
    private static final String DEV_USERNAME = "admin";
    private static final String DEV_PASSWORD = "admin";
    private static final String DEV_SECURITY_ANSWER = "admin";
    
    static {
        try {
            Class.forName(DRIVER);
            initializeDatabase();
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
            
            // 先删除所有表（按照依赖关系的反序）
            stmt.executeUpdate("DROP TABLE IF EXISTS GameReviews");
            stmt.executeUpdate("DROP TABLE IF EXISTS BugReports");
            stmt.executeUpdate("DROP TABLE IF EXISTS UserGames");
            stmt.executeUpdate("DROP TABLE IF EXISTS Users");
            
            // 创建Users表（作为基础表）
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Users (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT UNIQUE NOT NULL," +
                "    email TEXT UNIQUE NOT NULL," +
                "    password TEXT NOT NULL," +
                "    balance INTEGER DEFAULT 1000," +
                "    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            
            // 创建UserGames表（依赖于Users表）
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
            
            // 创建一个默认的管理员账户
            String sql = "INSERT OR IGNORE INTO Users (username, email, password, balance) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "admin");
                pstmt.setString(2, "admin@admin.com");
                pstmt.setString(3, "admin");
                pstmt.setInt(4, 9999);
                pstmt.executeUpdate();
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
            
            // 重新创建表
            initializeDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 验证用户登录
    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 注册新用户
    public static boolean registerUser(String username, String email, String password) {
        if (isEmailRegistered(email)) {
            return false;
        }

        String sql = "INSERT INTO Users(username, email, password, created_date) VALUES(?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
    public static int getUserBalance(String username) {
        String sql = "SELECT balance FROM Users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // 更新用户余额
    public static boolean updateUserBalance(String username, int newBalance) {
        String sql = "UPDATE Users SET balance = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newBalance);
            pstmt.setString(2, username);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
            
            return pstmt.executeUpdate() > 0;
            
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
} 
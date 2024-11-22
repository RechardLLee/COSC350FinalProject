import java.sql.*;

public class DatabaseService {
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:gameplatform.db";
    
    // 开发者登录凭证 - 修改为更容易记住的值
    private static final String DEV_USERNAME = "admin";
    private static final String DEV_PASSWORD = "admin";
    private static final String DEV_SECURITY_ANSWER = "admin";
    
    static {
        try {
            Class.forName(DRIVER);
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    
    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 创建Users表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Users (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT UNIQUE NOT NULL," +
                "    email TEXT UNIQUE NOT NULL," +
                "    password TEXT NOT NULL," +
                "    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            
            // 创建GameReviews表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS GameReviews (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT," +
                "    game_name TEXT," +
                "    rating INTEGER," +
                "    review TEXT," +
                "    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
            // 创建BugReports表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS BugReports (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT," +
                "    description TEXT," +
                "    report_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    status TEXT DEFAULT 'Open'," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
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
        String sql = "INSERT INTO Users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            
            return pstmt.executeUpdate() > 0;
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
} 
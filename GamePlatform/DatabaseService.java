import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://gambling-data.database.windows.net:1433;"
            + "database=SCOTT;"
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "loginTimeout=30;";
    private static final String USER = "weckmanscott";
    private static final String PASSWORD = "Isengard1";
    
    // 开发者登录凭证
    private static final String DEV_USERNAME = "root";
    private static final String DEV_PASSWORD = "admin";
    private static final String DEV_SECURITY_ANSWER = "Null";
    
    private static Connection connection;
    
    static {
        try {
            Class.forName(DRIVER);
            DatabaseInitializer.initializeDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
    
    // 验证开发者登录
    public static boolean validateDeveloper(String username, String password, String securityAnswer) {
        return DEV_USERNAME.equals(username) && 
               DEV_PASSWORD.equals(password) && 
               DEV_SECURITY_ANSWER.equals(securityAnswer);
    }
    
    // 用户验证
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
    
    // 用户注册
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
        String sql = "INSERT INTO BugReports (username, description, report_date) VALUES (?, ?, GETDATE())";
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
    
    // 重新排序用户ID
    private static void reorderUserIds() {
        String sql = 
            "WITH CTE AS (\n" +
            "    SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS new_id\n" +
            "    FROM Users\n" +
            ")\n" +
            "UPDATE CTE SET id = new_id";
            
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 修改删除用户方法
    public static boolean deleteUser(String username) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);  // 开始事务
            
            // 删除关联数据和用户
            String[] queries = {
                "DELETE FROM GameReviews WHERE username = ?",
                "DELETE FROM BugReports WHERE username = ?",
                "DELETE FROM Users WHERE username = ?"
            };
            
            for (String query : queries) {
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    pstmt.executeUpdate();
                }
            }
            
            // 重新排序ID
            reorderUserIds();
            
            conn.commit();  // 提交事务
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();  // 发生错误时回滚
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);  // 恢复自动提交
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // 修改获取用户列表的方法
    public static ResultSet getUsers() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(
            "SELECT ROW_NUMBER() OVER (ORDER BY id) AS display_id, " +
            "username, email, created_date FROM Users ORDER BY id"
        );
    }
    
    // 重置数据库
    public static boolean resetDatabase() {
        String[] dropTables = {
            "IF OBJECT_ID('GameReviews', 'U') IS NOT NULL DROP TABLE GameReviews",
            "IF OBJECT_ID('BugReports', 'U') IS NOT NULL DROP TABLE BugReports",
            "IF OBJECT_ID('Users', 'U') IS NOT NULL DROP TABLE Users"
        };
        
        Connection conn = null;
        try {
            conn = getConnection();
            
            // 删除所有表
            try (Statement stmt = conn.createStatement()) {
                for (String sql : dropTables) {
                    stmt.executeUpdate(sql);
                }
            }
            
            // 重新创建表
            try (Statement stmt = conn.createStatement()) {
                // Users表
                stmt.executeUpdate(
                    "CREATE TABLE Users (\n" +
                    "    id INT IDENTITY(1,1) PRIMARY KEY,\n" +
                    "    username VARCHAR(50) UNIQUE NOT NULL,\n" +
                    "    email VARCHAR(100) UNIQUE NOT NULL,\n" +
                    "    password VARCHAR(100) NOT NULL,\n" +
                    "    created_date DATETIME DEFAULT GETDATE()\n" +
                    ")"
                );
                
                // GameReviews表
                stmt.executeUpdate(
                    "CREATE TABLE GameReviews (\n" +
                    "    id INT IDENTITY(1,1) PRIMARY KEY,\n" +
                    "    username VARCHAR(50),\n" +
                    "    game_name VARCHAR(100),\n" +
                    "    rating INT,\n" +
                    "    review TEXT,\n" +
                    "    review_date DATETIME DEFAULT GETDATE(),\n" +
                    "    FOREIGN KEY (username) REFERENCES Users(username)\n" +
                    ")"
                );
                
                // BugReports表
                stmt.executeUpdate(
                    "CREATE TABLE BugReports (\n" +
                    "    id INT IDENTITY(1,1) PRIMARY KEY,\n" +
                    "    username VARCHAR(50),\n" +
                    "    description TEXT,\n" +
                    "    report_date DATETIME DEFAULT GETDATE(),\n" +
                    "    status VARCHAR(20) DEFAULT 'Open',\n" +
                    "    FOREIGN KEY (username) REFERENCES Users(username)\n" +
                    ")"
                );
            }
            
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 
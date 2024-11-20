import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 创建Users表
            String createUsersTable = 
                "IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))\n" +
                "BEGIN\n" +
                "CREATE TABLE Users (\n" +
                "    id INT IDENTITY(1,1) PRIMARY KEY,\n" +
                "    username VARCHAR(50) UNIQUE NOT NULL,\n" +
                "    email VARCHAR(100) UNIQUE NOT NULL,\n" +
                "    password VARCHAR(100) NOT NULL,\n" +
                "    created_date DATETIME DEFAULT GETDATE()\n" +
                ")\n" +
                "END";
            stmt.executeUpdate(createUsersTable);
            
            // 创建GameReviews表
            String createReviewsTable = 
                "IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[GameReviews]') AND type in (N'U'))\n" +
                "BEGIN\n" +
                "CREATE TABLE GameReviews (\n" +
                "    id INT IDENTITY(1,1) PRIMARY KEY,\n" +
                "    username VARCHAR(50),\n" +
                "    game_name VARCHAR(100),\n" +
                "    rating INT,\n" +
                "    review TEXT,\n" +
                "    review_date DATETIME DEFAULT GETDATE(),\n" +
                "    FOREIGN KEY (username) REFERENCES Users(username)\n" +
                ")\n" +
                "END";
            stmt.executeUpdate(createReviewsTable);
            
            // 创建BugReports表
            String createBugReportsTable = 
                "IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[BugReports]') AND type in (N'U'))\n" +
                "BEGIN\n" +
                "CREATE TABLE BugReports (\n" +
                "    id INT IDENTITY(1,1) PRIMARY KEY,\n" +
                "    username VARCHAR(50),\n" +
                "    description TEXT,\n" +
                "    report_date DATETIME DEFAULT GETDATE(),\n" +
                "    status VARCHAR(20) DEFAULT 'Open',\n" +
                "    FOREIGN KEY (username) REFERENCES Users(username)\n" +
                ")\n" +
                "END";
            stmt.executeUpdate(createBugReportsTable);
            
            System.out.println("Database tables initialized successfully");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database tables:");
            e.printStackTrace();
        }
    }
} 
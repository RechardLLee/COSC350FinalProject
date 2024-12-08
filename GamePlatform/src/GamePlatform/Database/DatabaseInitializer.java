package GamePlatform.Database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        // 用户表
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS Users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                email TEXT UNIQUE NOT NULL, 
                password TEXT NOT NULL,
                money INTEGER DEFAULT 1000,
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        // 游戏记录表
        String createGameRecordsTable = """
            CREATE TABLE IF NOT EXISTS game_records (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                game_name TEXT NOT NULL,
                score INTEGER,
                play_time INTEGER,
                play_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (username) REFERENCES Users(username)
            )
        """;

        // 反馈表
        String createFeedbackTable = """
            CREATE TABLE IF NOT EXISTS feedback (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                type TEXT NOT NULL,
                content TEXT,
                status TEXT DEFAULT 'pending',
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (username) REFERENCES Users(username)
            )
        """;

        // 系统日志表
        String createSystemLogsTable = """
            CREATE TABLE IF NOT EXISTS system_logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                content TEXT,
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        // 执行创建表
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createUsersTable);
            stmt.execute(createGameRecordsTable);
            stmt.execute(createFeedbackTable);
            stmt.execute(createSystemLogsTable);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
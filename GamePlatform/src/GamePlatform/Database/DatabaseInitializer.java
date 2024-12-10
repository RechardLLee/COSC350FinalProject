package GamePlatform.Database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 创建用户表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Users (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT UNIQUE NOT NULL," +
                "    email TEXT UNIQUE NOT NULL," +
                "    password TEXT NOT NULL," +
                "    money INTEGER DEFAULT 1000," +  // 添加 money 字段
                "    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            
            // 创建游戏记录表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS game_records (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT NOT NULL," +
                "    game_name TEXT NOT NULL," +
                "    score INTEGER," +
                "    play_time INTEGER," +
                "    play_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
            // 创建反馈表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS feedback (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT NOT NULL," +
                "    type TEXT NOT NULL," +
                "    content TEXT," +
                "    status TEXT DEFAULT 'pending'," +
                "    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
            // 创建系统日志表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS system_logs (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    type TEXT NOT NULL," +
                "    content TEXT," +
                "    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            
            // 创建游戏评论表
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
            
            // 创建Bug报告表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS BugReports (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT NOT NULL," +
                "    description TEXT," +
                "    status TEXT DEFAULT 'Open'," +
                "    report_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
            // 创建用户游戏表
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
            
            // 创建21点游戏记录表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS BlackJackRecords (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    username TEXT NOT NULL," +
                "    bet_amount INTEGER NOT NULL," +
                "    win_amount INTEGER NOT NULL," +
                "    play_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (username) REFERENCES Users(username)" +
                ")"
            );
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
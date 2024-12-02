package GamePlatform.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class GameAPI {
    
    // 记录游戏分数
    public static boolean recordGameScore(String gameName, String username, int score, int playTime) {
        try (Connection conn = DatabaseService.getConnection()) {
            // 开启事务
            conn.setAutoCommit(false);
            
            try {
                // 更新游戏总游玩次数
                String updateGameSql = 
                    "UPDATE Games SET total_plays = total_plays + 1 " +
                    "WHERE game_name = ?";
                    
                try (PreparedStatement pstmt = conn.prepareStatement(updateGameSql)) {
                    pstmt.setString(1, gameName);
                    pstmt.executeUpdate();
                }
                
                // 插入分数记录
                String insertScoreSql = 
                    "INSERT INTO GameScores (game_id, username, score, play_time) " +
                    "SELECT game_id, ?, ?, ? FROM Games WHERE game_name = ?";
                    
                try (PreparedStatement pstmt = conn.prepareStatement(insertScoreSql)) {
                    pstmt.setString(1, username);
                    pstmt.setInt(2, score);
                    pstmt.setInt(3, playTime);
                    pstmt.setString(4, gameName);
                    pstmt.executeUpdate();
                }
                
                // 更新游戏统计信息
                updateGameStats(conn, gameName);
                
                conn.commit();
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 获取游戏统计信息
    public static Map<String, Object> getGameStats(String gameName) {
        Map<String, Object> stats = new HashMap<>();
        
        String sql = 
            "SELECT g.total_plays, gs.* " +
            "FROM Games g " +
            "LEFT JOIN GameStats gs ON g.game_id = gs.game_id " +
            "WHERE g.game_name = ?";
            
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, gameName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                stats.put("totalPlays", rs.getInt("total_plays"));
                stats.put("totalPlayers", rs.getInt("total_players"));
                stats.put("highestScore", rs.getInt("highest_score"));
                stats.put("highestScorePlayer", rs.getString("highest_score_player"));
                stats.put("averageScore", rs.getFloat("average_score"));
                stats.put("averagePlayTime", rs.getInt("average_play_time"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stats;
    }
    
    // 获取排行榜
    public static List<Map<String, Object>> getLeaderboard(String gameName, int limit) {
        List<Map<String, Object>> leaderboard = new ArrayList<>();
        
        String sql = 
            "SELECT username, score, played_date " +
            "FROM GameScores gs " +
            "JOIN Games g ON gs.game_id = g.game_id " +
            "WHERE g.game_name = ? " +
            "ORDER BY score DESC " +
            "LIMIT ?";
            
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, gameName);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("username", rs.getString("username"));
                entry.put("score", rs.getInt("score"));
                entry.put("playedDate", rs.getTimestamp("played_date"));
                leaderboard.add(entry);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return leaderboard;
    }
    
    private static void updateGameStats(Connection conn, String gameName) throws SQLException {
        String sql = 
            "UPDATE GameStats SET " +
            "total_players = (SELECT COUNT(DISTINCT username) FROM GameScores gs " +
                           "JOIN Games g ON gs.game_id = g.game_id " +
                           "WHERE g.game_name = ?), " +
            "highest_score = (SELECT MAX(score) FROM GameScores gs " +
                           "JOIN Games g ON gs.game_id = g.game_id " +
                           "WHERE g.game_name = ?), " +
            "highest_score_player = (SELECT username FROM GameScores gs " +
                                  "JOIN Games g ON gs.game_id = g.game_id " +
                                  "WHERE g.game_name = ? " +
                                  "ORDER BY score DESC LIMIT 1), " +
            "average_score = (SELECT AVG(score) FROM GameScores gs " +
                           "JOIN Games g ON gs.game_id = g.game_id " +
                           "WHERE g.game_name = ?), " +
            "average_play_time = (SELECT AVG(play_time) FROM GameScores gs " +
                               "JOIN Games g ON gs.game_id = g.game_id " +
                               "WHERE g.game_name = ?) " +
            "WHERE game_id = (SELECT game_id FROM Games WHERE game_name = ?)";
            
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 6; i++) {
                pstmt.setString(i, gameName);
            }
            pstmt.executeUpdate();
        }
    }
    
    // 获取玩家游戏历史
    public static Map<String, Object> getPlayerGameHistory(String username, String gameName) {
        Map<String, Object> history = new HashMap<>();
        
        String sql = 
            "SELECT h.*, g.game_name " +
            "FROM PlayerGameHistory h " +
            "JOIN Games g ON h.game_id = g.game_id " +
            "WHERE h.username = ? AND g.game_name = ?";
            
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, gameName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                history.put("totalPlays", rs.getInt("total_plays"));
                history.put("totalPlayTime", rs.getInt("total_play_time"));
                history.put("bestScore", rs.getInt("best_score"));
                history.put("lastPlayed", rs.getTimestamp("last_played"));
                history.put("achievements", new JSONObject(rs.getString("achievements")));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return history;
    }
    
    // 创建多人游戏匹配
    public static int createMultiplayerMatch(String gameName, String matchType, List<String> participants) {
        try (Connection conn = DatabaseService.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // 创建比赛记录
                String createMatchSql = 
                    "INSERT INTO MultiplayerMatches (game_id, match_type) " +
                    "SELECT game_id, ? FROM Games WHERE game_name = ?";
                    
                int matchId;
                try (PreparedStatement pstmt = conn.prepareStatement(
                        createMatchSql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, matchType);
                    pstmt.setString(2, gameName);
                    pstmt.executeUpdate();
                    
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        matchId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to create match record");
                    }
                }
                
                // 添加参与者
                String addParticipantSql = 
                    "INSERT INTO MatchParticipants (match_id, username) VALUES (?, ?)";
                    
                try (PreparedStatement pstmt = conn.prepareStatement(addParticipantSql)) {
                    for (String username : participants) {
                        pstmt.setInt(1, matchId);
                        pstmt.setString(2, username);
                        pstmt.executeUpdate();
                    }
                }
                
                conn.commit();
                return matchId;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    // 更新多人游戏状态
    public static boolean updateMatchStatus(int matchId, String status, String winner) {
        String sql = 
            "UPDATE MultiplayerMatches SET " +
            "status = ?, winner = ?, end_time = CURRENT_TIMESTAMP " +
            "WHERE match_id = ?";
            
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, winner);
            pstmt.setInt(3, matchId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 更新参与者分数
    public static boolean updateParticipantScore(int matchId, String username, int score) {
        String sql = 
            "UPDATE MatchParticipants SET score = ? " +
            "WHERE match_id = ? AND username = ?";
            
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, score);
            pstmt.setInt(2, matchId);
            pstmt.setString(3, username);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 获取多人游戏记录
    public static List<Map<String, Object>> getMatchHistory(String gameName, int limit) {
        List<Map<String, Object>> matches = new ArrayList<>();
        
        String sql = 
            "SELECT m.*, g.game_name, " +
            "(SELECT GROUP_CONCAT(username || ':' || score) " +
            " FROM MatchParticipants WHERE match_id = m.match_id) as participants " +
            "FROM MultiplayerMatches m " +
            "JOIN Games g ON m.game_id = g.game_id " +
            "WHERE g.game_name = ? " +
            "ORDER BY m.start_time DESC " +
            "LIMIT ?";
            
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, gameName);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> match = new HashMap<>();
                match.put("matchId", rs.getInt("match_id"));
                match.put("matchType", rs.getString("match_type"));
                match.put("startTime", rs.getTimestamp("start_time"));
                match.put("endTime", rs.getTimestamp("end_time"));
                match.put("status", rs.getString("status"));
                match.put("winner", rs.getString("winner"));
                
                // 解析参与者信息
                String[] participants = rs.getString("participants").split(",");
                List<Map<String, Object>> participantList = new ArrayList<>();
                for (String p : participants) {
                    String[] parts = p.split(":");
                    Map<String, Object> participant = new HashMap<>();
                    participant.put("username", parts[0]);
                    participant.put("score", Integer.parseInt(parts[1]));
                    participantList.add(participant);
                }
                match.put("participants", participantList);
                
                matches.add(match);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return matches;
    }
} 
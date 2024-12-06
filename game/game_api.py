import sqlite3
import json
from datetime import datetime

class GameAPI:
    @staticmethod
    def record_score(game_name, username, score, play_time):
        try:
            # 连接到GamePlatform数据库
            conn = sqlite3.connect('../gameplatform.db')
            cursor = conn.cursor()
            
            # 更新游戏总游玩次数
            cursor.execute("""
                UPDATE Games 
                SET total_plays = total_plays + 1 
                WHERE game_name = ?
            """, (game_name,))
            
            # 获取game_id
            cursor.execute("SELECT game_id FROM Games WHERE game_name = ?", (game_name,))
            game_id = cursor.fetchone()[0]
            
            # 插入分数记录
            cursor.execute("""
                INSERT INTO GameScores (game_id, username, score, play_time)
                VALUES (?, ?, ?, ?)
            """, (game_id, username, score, play_time))
            
            # 更新统计信息
            cursor.execute("""
                UPDATE GameStats 
                SET total_players = (
                    SELECT COUNT(DISTINCT username) 
                    FROM GameScores 
                    WHERE game_id = ?
                ),
                highest_score = (
                    SELECT MAX(score) 
                    FROM GameScores 
                    WHERE game_id = ?
                ),
                highest_score_player = (
                    SELECT username 
                    FROM GameScores 
                    WHERE game_id = ? 
                    ORDER BY score DESC 
                    LIMIT 1
                ),
                average_score = (
                    SELECT AVG(score) 
                    FROM GameScores 
                    WHERE game_id = ?
                ),
                average_play_time = (
                    SELECT AVG(play_time) 
                    FROM GameScores 
                    WHERE game_id = ?
                )
                WHERE game_id = ?
            """, (game_id,) * 6)
            
            conn.commit()
            return True
            
        except Exception as e:
            print(f"Error recording score: {e}")
            if conn:
                conn.rollback()
            return False
            
        finally:
            if conn:
                conn.close()
    
    @staticmethod
    def get_game_stats(game_name):
        try:
            conn = sqlite3.connect('../gameplatform.db')
            cursor = conn.cursor()
            
            cursor.execute("""
                SELECT g.total_plays, gs.*
                FROM Games g
                LEFT JOIN GameStats gs ON g.game_id = gs.game_id
                WHERE g.game_name = ?
            """, (game_name,))
            
            row = cursor.fetchone()
            if row:
                return {
                    "totalPlays": row[0],
                    "totalPlayers": row[3],
                    "highestScore": row[4],
                    "highestScorePlayer": row[5],
                    "averageScore": row[6],
                    "averagePlayTime": row[7]
                }
            return {}
            
        except Exception as e:
            print(f"Error getting game stats: {e}")
            return {}
            
        finally:
            if conn:
                conn.close()
    
    @staticmethod
    def get_player_history(username, game_name):
        try:
            conn = sqlite3.connect('../gameplatform.db')
            cursor = conn.cursor()
            
            cursor.execute("""
                SELECT h.*, g.game_name
                FROM PlayerGameHistory h
                JOIN Games g ON h.game_id = g.game_id
                WHERE h.username = ? AND g.game_name = ?
            """, (username, game_name))
            
            row = cursor.fetchone()
            if row:
                return {
                    "totalPlays": row[3],
                    "totalPlayTime": row[4],
                    "bestScore": row[5],
                    "lastPlayed": row[6],
                    "achievements": json.loads(row[7])
                }
            return {}
            
        except Exception as e:
            print(f"Error getting player history: {e}")
            return {}
            
        finally:
            if conn:
                conn.close()
    
    @staticmethod
    def create_multiplayer_match(game_name, match_type, participants):
        try:
            conn = sqlite3.connect('../gameplatform.db')
            cursor = conn.cursor()
            
            # 创建比赛记录
            cursor.execute("""
                INSERT INTO MultiplayerMatches (game_id, match_type)
                SELECT game_id, ? FROM Games WHERE game_name = ?
            """, (match_type, game_name))
            
            match_id = cursor.lastrowid
            
            # 添加参与者
            for username in participants:
                cursor.execute("""
                    INSERT INTO MatchParticipants (match_id, username)
                    VALUES (?, ?)
                """, (match_id, username))
            
            conn.commit()
            return match_id
            
        except Exception as e:
            print(f"Error creating multiplayer match: {e}")
            if conn:
                conn.rollback()
            return -1
            
        finally:
            if conn:
                conn.close()
    
    @staticmethod
    def update_match_status(match_id, status, winner=None):
        try:
            conn = sqlite3.connect('../gameplatform.db')
            cursor = conn.cursor()
            
            cursor.execute("""
                UPDATE MultiplayerMatches
                SET status = ?, winner = ?, end_time = CURRENT_TIMESTAMP
                WHERE match_id = ?
            """, (status, winner, match_id))
            
            conn.commit()
            return True
            
        except Exception as e:
            print(f"Error updating match status: {e}")
            return False
            
        finally:
            if conn:
                conn.close()
  
from game_api import GameAPI
import time

class BaseGame:
    def __init__(self, game_name):
        self.username = None  # 将从配置文件读取
        self.game_name = game_name
        self.start_time = time.time()
        self.score = 0
        
        # 读取当前用户
        self.load_current_user()
    
    def load_current_user(self):
        try:
            with open('../gameplatform_session.txt', 'r') as f:
                self.username = f.read().strip()
        except:
            print("Warning: Could not load user session")
    
    def record_score(self):
        play_time = int(time.time() - self.start_time)
        GameAPI.record_score(self.game_name, self.username, self.score, play_time)
    
    def show_stats(self):
        stats = GameAPI.get_game_stats(self.game_name)
        self.show_game_stats(stats)
    
    def show_game_stats(self, stats):
        raise NotImplementedError("Subclasses must implement show_game_stats") 
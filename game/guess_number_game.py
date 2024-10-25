import tkinter as tk
from tkinter import ttk
import random
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.animation import FuncAnimation

class GuessNumberGame:
    def __init__(self, master):
        self.master = master
        self.master.title("Guess the Number Game")
        self.master.geometry("400x350")
        self.master.configure(bg='#f0f0f0')

        self.difficulties = {
            "Easy": {"range": (1, 50), "attempts": 10},
            "Medium": {"range": (1, 100), "attempts": 7},
            "Hard": {"range": (1, 200), "attempts": 5}
        }
        self.current_difficulty = "Medium"
        self.target_number = None
        self.guesses = []
        self.attempts_left = None

        self.create_widgets()
        self.start_new_game()

    def create_widgets(self):
        main_frame = ttk.Frame(self.master, padding="20")
        main_frame.pack(fill=tk.BOTH, expand=True)

        style = ttk.Style()
        style.configure('TLabel', font=('Helvetica', 12), background='#f0f0f0')
        style.configure('TButton', font=('Helvetica', 12))
        style.configure('TEntry', font=('Helvetica', 12))

        # 难度选择
        difficulty_frame = ttk.Frame(main_frame)
        difficulty_frame.pack(pady=10)
        ttk.Label(difficulty_frame, text="Difficulty:").pack(side=tk.LEFT)
        self.difficulty_var = tk.StringVar(value=self.current_difficulty)
        difficulty_menu = ttk.OptionMenu(difficulty_frame, self.difficulty_var, self.current_difficulty, *self.difficulties.keys(), command=self.change_difficulty)
        difficulty_menu.pack(side=tk.LEFT, padx=(10, 0))

        # 游戏信息
        self.info_label = ttk.Label(main_frame, text="")
        self.info_label.pack(pady=10)

        # 输入框和按钮
        input_frame = ttk.Frame(main_frame)
        input_frame.pack(pady=5)
        self.entry = ttk.Entry(input_frame, width=10)
        self.entry.pack(side=tk.LEFT, padx=(0, 10))
        ttk.Button(input_frame, text="Guess!", command=self.make_guess).pack(side=tk.LEFT)

        # 结果标签
        self.result_label = ttk.Label(main_frame, text="", wraplength=350)
        self.result_label.pack(pady=20)

        # 新游戏按钮
        ttk.Button(main_frame, text="New Game", command=self.start_new_game).pack(pady=10)

    def change_difficulty(self, difficulty):
        self.current_difficulty = difficulty
        self.start_new_game()

    def start_new_game(self):
        difficulty_info = self.difficulties[self.current_difficulty]
        self.target_number = random.randint(*difficulty_info["range"])
        self.attempts_left = difficulty_info["attempts"]
        self.guesses = []
        self.update_info_label()
        self.result_label.config(text="")

    def update_info_label(self):
        difficulty_info = self.difficulties[self.current_difficulty]
        self.info_label.config(text=f"Guess a number between {difficulty_info['range'][0]} and {difficulty_info['range'][1]}. "
                                    f"Attempts left: {self.attempts_left}")

    def make_guess(self):
        if self.attempts_left <= 0:
            return

        try:
            guess = int(self.entry.get())
            self.guesses.append(guess)
            self.attempts_left -= 1

            if guess == self.target_number:
                self.result_label.config(text=f"Congratulations! The target number was {self.target_number}")
                self.show_visualization_options()
            elif self.attempts_left == 0:
                self.result_label.config(text=f"Game over! The target number was {self.target_number}")
            elif guess < self.target_number:
                self.result_label.config(text="Too low, try again")
            else:
                self.result_label.config(text="Too high, try again")

            self.entry.delete(0, tk.END)
            self.update_info_label()
        except ValueError:
            self.result_label.config(text="Please enter a valid number")

    def show_visualization_options(self):
        visualization_window = tk.Toplevel(self.master)
        visualization_window.title("Choose Visualization Method")
        visualization_window.geometry("300x200")
        visualization_window.configure(bg='#f0f0f0')

        frame = ttk.Frame(visualization_window, padding="20")
        frame.pack(fill=tk.BOTH, expand=True)

        ttk.Label(frame, text="Select a search method to visualize:").pack(pady=10)
        ttk.Button(frame, text="Sequential Search", command=lambda: self.visualize_search("Sequential")).pack(pady=5, fill=tk.X)
        ttk.Button(frame, text="Binary Search", command=lambda: self.visualize_search("Binary")).pack(pady=5, fill=tk.X)

    def visualize_search(self, search_type):
        vis_window = tk.Toplevel(self.master)
        vis_window.title(f"{search_type} Search Visualization")
        vis_window.geometry("800x600")

        fig, ax = plt.subplots(figsize=(8, 4))
        canvas = FigureCanvasTkAgg(fig, master=vis_window)
        canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)

        x = list(range(1, 101))
        y = [0] * 100
        
        if search_type == "Sequential":
            steps = list(range(1, self.target_number + 1))
        else:  # Binary search
            steps = []
            low, high = 0, 99
            while low <= high:
                mid = (low + high) // 2
                steps.append(mid)
                if mid + 1 < self.target_number:
                    low = mid + 1
                elif mid + 1 > self.target_number:
                    high = mid - 1
                else:
                    break

        def update(frame):
            if frame < len(steps):
                if search_type == "Sequential":
                    y[steps[frame] - 1] = frame + 1
                else:
                    y[steps[frame]] = frame + 1
            ax.clear()
            ax.bar(x, y)
            ax.set_xlabel("Number")
            ax.set_ylabel("Search Step")
            ax.set_title(f"{search_type} Search Visualization (Step {frame+1})")
            ax.set_ylim(0, len(steps) + 1)
            if frame == len(steps) - 1:
                vis_window.after(1000, vis_window.destroy)  # Close the window after the last frame

        anim = FuncAnimation(fig, update, frames=len(steps), interval=1000, repeat=False)
        canvas.draw()

if __name__ == "__main__":
    root = tk.Tk()
    game = GuessNumberGame(root)
    try:
        root.mainloop()
    except KeyboardInterrupt:
        print("\nProgram interrupted by user. Exiting...")
        root.quit()

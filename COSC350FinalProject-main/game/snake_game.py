import tkinter as tk
from tkinter import ttk, messagebox
import random

class SnakeGame:
    def __init__(self, master):
        self.master = master
        self.master.title("Snake Game")
        self.master.geometry("600x700")
        self.master.resizable(False, False)
        self.master.configure(bg='#2C3E50')

        self.style = ttk.Style()
        self.style.theme_use('clam')
        self.style.configure('TButton', font=('Helvetica', 12), background='#3498DB', foreground='white')
        self.style.configure('TLabel', font=('Helvetica', 14), background='#2C3E50', foreground='white')
        self.style.configure('TOptionMenu', font=('Helvetica', 12), background='#3498DB', foreground='white')

        self.create_widgets()

        self.snake = []
        self.food = None
        self.obstacles = []
        self.direction = "Right"
        self.score = 0
        self.high_score = 0
        self.game_speed = 100
        self.game_running = False

    def create_widgets(self):
        # Game title
        title_label = ttk.Label(self.master, text="Snake Game", font=('Helvetica', 24, 'bold'))
        title_label.pack(pady=20)

        # Game canvas
        self.canvas = tk.Canvas(self.master, bg="#34495E", width=500, height=500, highlightthickness=0)
        self.canvas.pack(pady=10)

        # Score frame
        score_frame = ttk.Frame(self.master, style='TLabel')
        score_frame.pack(fill='x', padx=20)

        self.score_label = ttk.Label(score_frame, text="Score: 0")
        self.score_label.pack(side='left')

        self.high_score_label = ttk.Label(score_frame, text="High Score: 0")
        self.high_score_label.pack(side='right')

        # Control frame
        control_frame = ttk.Frame(self.master, style='TLabel')
        control_frame.pack(fill='x', padx=20, pady=10)

        self.difficulty_var = tk.StringVar(value="Medium")
        difficulty_menu = ttk.OptionMenu(control_frame, self.difficulty_var, "Medium", "Easy", "Medium", "Hard")
        difficulty_menu.pack(side='left')

        self.start_button = ttk.Button(control_frame, text="Start Game", command=self.start_game)
        self.start_button.pack(side='right')

        self.restart_button = ttk.Button(control_frame, text="Restart Game", command=self.restart_game, state=tk.DISABLED)
        self.restart_button.pack(side='right', padx=10)

        # Instructions
        instructions = "Use W, A, S, D keys to control the snake"
        instruction_label = ttk.Label(self.master, text=instructions, font=('Helvetica', 12))
        instruction_label.pack(pady=10)

    def start_game(self):
        self.snake = [(250, 250), (240, 250), (230, 250)]
        self.food = self.create_food()
        self.obstacles = self.create_obstacles()
        self.direction = "Right"
        self.score = 0
        self.update_score()
        self.game_running = True
        self.start_button.config(state=tk.DISABLED)
        self.restart_button.config(state=tk.NORMAL)
        self.set_difficulty()
        self.master.bind("<KeyPress>", self.change_direction)
        self.update()

    def restart_game(self):
        self.canvas.delete("all")
        self.start_game()

    def create_food(self):
        while True:
            x = random.randint(0, 49) * 10
            y = random.randint(0, 49) * 10
            if (x, y) not in self.snake and (x, y) not in self.obstacles:
                return (x, y)

    def create_obstacles(self):
        obstacles = []
        for _ in range(10):  # Create 10 obstacles
            while True:
                x = random.randint(0, 49) * 10
                y = random.randint(0, 49) * 10
                if (x, y) not in self.snake and (x, y) != self.food and (x, y) not in obstacles:
                    obstacles.append((x, y))
                    break
        return obstacles

    def move_snake(self):
        head = self.snake[0]
        if self.direction == "Right":
            new_head = ((head[0] + 10) % 500, head[1])
        elif self.direction == "Left":
            new_head = ((head[0] - 10) % 500, head[1])
        elif self.direction == "Up":
            new_head = (head[0], (head[1] - 10) % 500)
        elif self.direction == "Down":
            new_head = (head[0], (head[1] + 10) % 500)

        if new_head in self.snake[1:] or new_head in self.obstacles:
            self.game_over()
            return

        self.snake = [new_head] + self.snake[:-1]

        if new_head == self.food:
            self.score += 1
            self.update_score()
            self.snake.append(self.snake[-1])
            self.food = self.create_food()

    def change_direction(self, event):
        key = event.keysym.lower()
        if key == 'w' and self.direction != "Down":
            self.direction = "Up"
        elif key == 's' and self.direction != "Up":
            self.direction = "Down"
        elif key == 'a' and self.direction != "Right":
            self.direction = "Left"
        elif key == 'd' and self.direction != "Left":
            self.direction = "Right"

    def update(self):
        if self.game_running:
            self.move_snake()
            self.canvas.delete("all")
            for segment in self.snake:
                self.canvas.create_rectangle(segment[0], segment[1], 
                                             segment[0] + 10, segment[1] + 10, 
                                             fill="#2ECC71", outline="#27AE60")

            self.canvas.create_oval(self.food[0], self.food[1], 
                                    self.food[0] + 10, self.food[1] + 10, 
                                    fill="#E74C3C", outline="#C0392B")

            for obstacle in self.obstacles:
                self.canvas.create_rectangle(obstacle[0], obstacle[1],
                                             obstacle[0] + 10, obstacle[1] + 10,
                                             fill="#95A5A6", outline="#7F8C8D")

            self.master.after(self.game_speed, self.update)

    def game_over(self):
        self.game_running = False
        if self.score > self.high_score:
            self.high_score = self.score
            self.high_score_label.config(text=f"High Score: {self.high_score}")
        messagebox.showinfo("Game Over", f"Your score: {self.score}")
        self.start_button.config(state=tk.NORMAL)
        self.restart_button.config(state=tk.DISABLED)

    def update_score(self):
        self.score_label.config(text=f"Score: {self.score}")

    def set_difficulty(self):
        difficulty = self.difficulty_var.get()
        if difficulty == "Easy":
            self.game_speed = 150
        elif difficulty == "Medium":
            self.game_speed = 100
        elif difficulty == "Hard":
            self.game_speed = 50

if __name__ == "__main__":
    root = tk.Tk()
    game = SnakeGame(root)
    root.mainloop()

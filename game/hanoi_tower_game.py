import tkinter as tk
from tkinter import ttk, messagebox, scrolledtext
import time

class Disk:
    def __init__(self, size, canvas, x, y):
        self.size = size
        self.width = size * 20
        self.height = 20
        self.shape = canvas.create_rectangle(x - self.width // 2, y - self.height // 2, 
                                             x + self.width // 2, y + self.height // 2, 
                                             fill=self.get_color())

    def get_color(self):
        colors = ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet']
        return colors[self.size % len(colors)]

class Tower:
    def __init__(self, canvas, x):
        self.canvas = canvas
        self.x = x
        self.disks = []
        self.base = canvas.create_rectangle(x - 50, 350, x + 50, 370, fill="brown")
        self.pole = canvas.create_rectangle(x - 5, 100, x + 5, 350, fill="brown")

    def add_disk(self, disk):
        y = 350 - len(self.disks) * 20 - 10
        self.canvas.coords(disk.shape, self.x - disk.width // 2, y - disk.height // 2, 
                           self.x + disk.width // 2, y + disk.height // 2)
        self.disks.append(disk)

    def remove_disk(self):
        if self.disks:
            return self.disks.pop()
        return None

class HanoiTower:
    def __init__(self, master):
        self.master = master
        self.master.title("Hanoi Tower Game")
        self.master.geometry("800x500")

        self.difficulties = {
            "Easy": 3,
            "Medium": 4,
            "Hard": 5
        }
        self.current_difficulty = "Easy"
        self.num_disks = self.difficulties[self.current_difficulty]
        
        self.create_widgets()
        self.reset_game()

    def create_widgets(self):
        self.canvas = tk.Canvas(self.master, width=800, height=400)
        self.canvas.pack()

        control_frame = ttk.Frame(self.master, padding="10")
        control_frame.pack(fill=tk.X)

        ttk.Label(control_frame, text="Difficulty:").pack(side=tk.LEFT)
        self.difficulty_var = tk.StringVar(value=self.current_difficulty)
        difficulty_menu = ttk.OptionMenu(control_frame, self.difficulty_var, self.current_difficulty, *self.difficulties.keys(), command=self.change_difficulty)
        difficulty_menu.pack(side=tk.LEFT, padx=(10, 20))

        ttk.Button(control_frame, text="Reset Game", command=self.reset_game).pack(side=tk.LEFT)
        ttk.Button(control_frame, text="Auto Solve", command=self.auto_solve).pack(side=tk.LEFT, padx=(20, 0))
        ttk.Button(control_frame, text="Algorithm Explanation", command=self.show_algorithm_explanation).pack(side=tk.LEFT, padx=(20, 0))
        
        self.moves_label = ttk.Label(control_frame, text="Moves: 0")
        self.moves_label.pack(side=tk.RIGHT)

        self.canvas.bind("<Button-1>", self.on_click)

    def change_difficulty(self, difficulty):
        self.current_difficulty = difficulty
        self.num_disks = self.difficulties[self.current_difficulty]
        self.reset_game()

    def reset_game(self):
        self.canvas.delete("all")
        self.towers = [Tower(self.canvas, 200), Tower(self.canvas, 400), Tower(self.canvas, 600)]
        self.selected_tower = None
        self.moves = 0
        self.moves_label.config(text=f"Moves: {self.moves}")

        for i in range(self.num_disks, 0, -1):
            disk = Disk(i, self.canvas, 200, 350 - (self.num_disks - i) * 20)
            self.towers[0].add_disk(disk)

    def on_click(self, event):
        clicked_tower = self.get_clicked_tower(event.x)
        if clicked_tower is not None:
            if self.selected_tower is None:
                if clicked_tower.disks:
                    self.selected_tower = clicked_tower
                    top_disk = clicked_tower.disks[-1]
                    self.canvas.itemconfig(top_disk.shape, width=2)
            else:
                if not clicked_tower.disks or clicked_tower.disks[-1].size > self.selected_tower.disks[-1].size:
                    disk = self.selected_tower.remove_disk()
                    self.canvas.itemconfig(disk.shape, width=1)
                    clicked_tower.add_disk(disk)
                    self.moves += 1
                    self.moves_label.config(text=f"Moves: {self.moves}")
                    if self.check_win():
                        if self.verify_solution():
                            messagebox.showinfo("Congratulations!", f"You won in {self.moves} moves!")
                        else:
                            messagebox.showwarning("Oops!", "Something went wrong. The solution is not correct.")
                else:
                    messagebox.showwarning("Invalid Move", "You can't place a larger disk on a smaller one!")
                self.selected_tower = None

    def get_clicked_tower(self, x):
        for tower in self.towers:
            if abs(tower.x - x) < 100:
                return tower
        return None

    def check_win(self):
        return len(self.towers[2].disks) == self.num_disks

    def verify_solution(self):
        if len(self.towers[2].disks) != self.num_disks:
            return False
        for i in range(self.num_disks):
            if self.towers[2].disks[i].size != self.num_disks - i:
                return False
        return True

    def auto_solve(self):
        self.reset_game()
        self.solve_hanoi(self.num_disks, self.towers[0], self.towers[2], self.towers[1])

    def solve_hanoi(self, n, source, target, auxiliary):
        if n > 0:
            self.solve_hanoi(n - 1, source, auxiliary, target)
            self.move_disk(source, target)
            self.solve_hanoi(n - 1, auxiliary, target, source)

    def move_disk(self, source, target):
        disk = source.remove_disk()
        target.add_disk(disk)
        self.moves += 1
        self.moves_label.config(text=f"Moves: {self.moves}")
        self.master.update()
        time.sleep(0.5)  # 添加延迟以便观察移动过程

    def show_algorithm_explanation(self):
        explanation_window = tk.Toplevel(self.master)
        explanation_window.title("Hanoi Tower Algorithm Explanation")
        explanation_window.geometry("600x400")

        explanation_text = scrolledtext.ScrolledText(explanation_window, wrap=tk.WORD, width=70, height=20)
        explanation_text.pack(padx=10, pady=10, fill=tk.BOTH, expand=True)

        algorithm_explanation = """
Hanoi Tower Algorithm Explanation:

The Tower of Hanoi is a classic problem in computer science and mathematics. The algorithm to solve it is based on recursion.

Basic Principle:
1. Move n-1 disks from the source peg to the auxiliary peg.
2. Move the largest disk from the source peg to the target peg.
3. Move the n-1 disks from the auxiliary peg to the target peg.

Recursive Algorithm:
1. If n == 1, move the disk directly from source to target.
2. Otherwise:
   a. Move n-1 disks from source to auxiliary (recursively).
   b. Move the nth disk from source to target.
   c. Move n-1 disks from auxiliary to target (recursively).

Python Implementation:
def solve_hanoi(n, source, target, auxiliary):
    if n > 0:
        # Move n-1 disks from source to auxiliary
        solve_hanoi(n - 1, source, auxiliary, target)
        
        # Move the nth disk from source to target
        print(f"Move disk {n} from {source} to {target}")
        
        # Move n-1 disks from auxiliary to target
        solve_hanoi(n - 1, auxiliary, target, source)

Time Complexity:
The time complexity of this algorithm is O(2^n), where n is the number of disks.

Space Complexity:
The space complexity is O(n) due to the recursive call stack.

This algorithm demonstrates the power of recursive thinking in solving complex problems by breaking them down into smaller, manageable sub-problems.
        """

        explanation_text.insert(tk.END, algorithm_explanation)
        explanation_text.config(state=tk.DISABLED)  # Make the text read-only

if __name__ == "__main__":
    root = tk.Tk()
    game = HanoiTower(root)
    root.mainloop()
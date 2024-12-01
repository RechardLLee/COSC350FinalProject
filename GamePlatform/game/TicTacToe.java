import java.util.Scanner;

public class TicTacToe {
    private static char[][] board = new char[3][3];
    private static char currentPlayer;
    private static int playerXScore = 0;
    private static int playerOScore = 0;
    private static boolean isEnglish = true;  // 默认英文
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        selectLanguage();
        while (true) {
            initializeGame();
            playGame();
            if (!playAgain()) {
                showFinalScore();
                break;
            }
        }
        scanner.close();
    }

    private static void selectLanguage() {
        System.out.println("Select language / 选择语言:");
        System.out.println("1. English");
        System.out.println("2. 中文");
        int choice = scanner.nextInt();
        isEnglish = (choice == 1);
    }

    private static void initializeGame() {
        // 初始化棋盘
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = 'X';  // X先手
        
        // 显示游戏开始信息
        System.out.println("\n" + getMessage("welcome"));
        printBoard();
    }

    private static void playGame() {
        boolean gameEnded = false;

        while (!gameEnded) {
            System.out.println(getMessage("playerTurn") + currentPlayer);
            
            // 获取玩家输入
            int row, col;
            while (true) {
                try {
                    System.out.println(getMessage("enterMove"));
                    row = scanner.nextInt() - 1;
                    col = scanner.nextInt() - 1;
                    
                    if (isValidMove(row, col)) {
                        break;
                    } else {
                        System.out.println(getMessage("invalidMove"));
                    }
                } catch (Exception e) {
                    System.out.println(getMessage("invalidInput"));
                    scanner.nextLine(); // 清除输入缓冲
                }
            }

            // 执行移动
            board[row][col] = currentPlayer;
            printBoard();

            // 检查游戏状态
            if (checkWin()) {
                System.out.println(getMessage("winner") + currentPlayer);
                updateScore();
                gameEnded = true;
            } else if (checkTie()) {
                System.out.println(getMessage("tie"));
                gameEnded = true;
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }
    }

    private static void printBoard() {
        System.out.println("  1 2 3");
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j]);
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("  -----");
        }
        System.out.println(getMessage("score") + 
                         "X: " + playerXScore + " O: " + playerOScore);
    }

    private static boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ';
    }

    private static boolean checkWin() {
        // 检查行
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && 
                board[i][1] == currentPlayer && 
                board[i][2] == currentPlayer) {
                return true;
            }
        }

        // 检查列
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == currentPlayer && 
                board[1][i] == currentPlayer && 
                board[2][i] == currentPlayer) {
                return true;
            }
        }

        // 检查对角线
        return (board[0][0] == currentPlayer && 
                board[1][1] == currentPlayer && 
                board[2][2] == currentPlayer) ||
               (board[0][2] == currentPlayer && 
                board[1][1] == currentPlayer && 
                board[2][0] == currentPlayer);
    }

    private static boolean checkTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private static void updateScore() {
        if (currentPlayer == 'X') {
            playerXScore++;
        } else {
            playerOScore++;
        }
    }

    private static boolean playAgain() {
        System.out.println(getMessage("playAgain"));
        String response = scanner.next().toLowerCase();
        return response.startsWith(isEnglish ? "y" : "是");
    }

    private static void showFinalScore() {
        System.out.println("\n" + getMessage("finalScore"));
        System.out.println("X: " + playerXScore);
        System.out.println("O: " + playerOScore);
        
        if (playerXScore > playerOScore) {
            System.out.println(getMessage("xWins"));
        } else if (playerOScore > playerXScore) {
            System.out.println(getMessage("oWins"));
        } else {
            System.out.println(getMessage("finalTie"));
        }
    }

    private static String getMessage(String key) {
        if (isEnglish) {
            switch (key) {
                case "welcome": return "Welcome to Tic-Tac-Toe!\n";
                case "playerTurn": return "Player ";
                case "enterMove": return "Enter your move (row [1-3] and column [1-3]): ";
                case "invalidMove": return "Invalid move! Try again.";
                case "invalidInput": return "Invalid input! Please enter numbers between 1 and 3.";
                case "winner": return "Player ";
                case "tie": return "The game is a tie!";
                case "score": return "Current Score: ";
                case "playAgain": return "Play again? (yes/no): ";
                case "finalScore": return "Final Score:";
                case "xWins": return "Player X wins the series!";
                case "oWins": return "Player O wins the series!";
                case "finalTie": return "The series is tied!";
                default: return "";
            }
        } else {
            switch (key) {
                case "welcome": return "欢迎来到井字棋游戏！\n";
                case "playerTurn": return "玩家 ";
                case "enterMove": return "请输入你的移动（行 [1-3] 和列 [1-3]）：";
                case "invalidMove": return "无效的移动！请重试。";
                case "invalidInput": return "无效的输入！请输入1到3之间的数字。";
                case "winner": return "玩家 ";
                case "tie": return "游戏平局！";
                case "score": return "当前比分：";
                case "playAgain": return "再玩一次？（是/否）：";
                case "finalScore": return "最终比分：";
                case "xWins": return "玩家X赢得了比赛！";
                case "oWins": return "玩家O赢得了比赛！";
                case "finalTie": return "比赛平局！";
                default: return "";
            }
        }
    }
}

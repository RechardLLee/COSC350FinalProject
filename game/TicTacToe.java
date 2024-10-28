import java.util.Scanner;

public class TicTacToe {

    // 3x3 board
    private static char[][] board = { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } };
    private static char currentPlayer = 'X'; // Player X always goes first

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean gameEnded = false;

        System.out.println("Welcome to Tic-Tac-Toe!");
        printBoard();

        // Game loop
        while (!gameEnded) {
            System.out.println("Player " + currentPlayer + ", enter your move (row [1-3] and column [1-3]): ");
            int row = scanner.nextInt() - 1;
            int col = scanner.nextInt() - 1;

            // Check if the move is valid
            if (row < 0 || col < 0 || row > 2 || col > 2 || board[row][col] != ' ') {
                System.out.println("Invalid move! Try again.");
            } else {
                // Make the move
                board[row][col] = currentPlayer;
                printBoard();

                // Check if the current player has won
                if (checkWin()) {
                    System.out.println("Player " + currentPlayer + " wins!");
                    gameEnded = true;
                }
                // Check if the board is full (tie)
                else if (checkTie()) {
                    System.out.println("The game is a tie!");
                    gameEnded = true;
                } else {
                    // Switch player
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                }
            }
        }
        scanner.close();
    }

    // Function to print the current board state
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
    }

    // Function to check if the current player has won
    private static boolean checkWin() {
        // Check rows, columns, and diagonals
        return (checkRow() || checkCol() || checkDiagonal());
    }

    private static boolean checkRow() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkCol() {
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkDiagonal() {
        return ((board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
                (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer));
    }

    // Function to check if the game is a tie (i.e., board is full with no winner)
    private static boolean checkTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false; // Still empty spaces, so no tie
                }
            }
        }
        return true;
    }
}

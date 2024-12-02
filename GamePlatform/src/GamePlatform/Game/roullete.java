// NOT DONE
import java.util.Random;
import java.util.Scanner;

public class roullete {

    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println("Welcome to the Roulette Game!");
        
        // Player's starting balance
        int balance = 100;

        // Main game loop
        while (true) {
            System.out.println("\nYour current balance: " + balance + " chips");
            
            // If the balance is zero, end the game
            if (balance <= 0) {
                System.out.println("You are out of chips. Game over!");
                break;
            }

            System.out.println("Choose a bet:");
            System.out.println("1. Bet on a number (0 to 36)");
            System.out.println("2. Bet on a color (Red or Black)");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();

            if (choice == 3) {
                System.out.println("Thanks for playing! Your final balance is: " + balance);
                break;
            }

            System.out.print("How many chips do you want to bet? ");
            int bet = scanner.nextInt();

            // Validate the bet amount
            if (bet > balance || bet <= 0) {
                System.out.println("Invalid bet! Please try again.");
                continue;
            }

            // Spin the roulette
            int rolledNumber = random.nextInt(37); // Numbers from 0 to 36
            String rolledColor = (rolledNumber % 2 == 0) ? "Black" : "Red"; // Even numbers are "Black", odd are "Red"

            // Process the player's choice
            switch (choice) {
                case 1:
                    System.out.print("Pick a number (0 to 36): ");
                    int pickedNumber = scanner.nextInt();
                    if (pickedNumber < 0 || pickedNumber > 36) {
                        System.out.println("Invalid number! Please try again.");
                        continue;
                    }

                    System.out.println("The roulette spins... The rolled number is: " + rolledNumber + " (" + rolledColor + ")");
                    if (pickedNumber == rolledNumber) {
                        System.out.println("Congratulations! You hit the number and won " + (bet * 35) + " chips.");
                        balance += bet * 35; // 35:1 payout for number bets
                    } else {
                        System.out.println("You lost the bet.");
                        balance -= bet;
                    }
                    break;

                case 2:
                    System.out.print("Pick a color (Red or Black): ");
                    String pickedColor = scanner.next();

                    System.out.println("The roulette spins... The rolled number is: " + rolledNumber + " (" + rolledColor + ")");
                    if (pickedColor.equalsIgnoreCase(rolledColor)) {
                        System.out.println("Congratulations! You hit the color and won " + (bet * 2) + " chips.");
                        balance += bet * 2; // 2:1 payout for color bets
                    } else {
                        System.out.println("You lost the bet.");
                        balance -= bet;
                    }
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }

        scanner.close();
    }
}

package GamePlatform.Game.BlackJackGui;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new BlackJackGame();
        });
    }
}

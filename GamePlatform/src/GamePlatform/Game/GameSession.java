package GamePlatform.Game;

public class GameSession {
    private static String currentUser;
    private static String currentGame;
    
    public static void startGameSession(String username, String gameName) {
        currentUser = username;
        currentGame = gameName;
    }
    
    public static String getCurrentUser() {
        return currentUser;
    }
    
    public static String getCurrentGame() {
        return currentGame;
    }
    
    public static void endGameSession() {
        currentUser = null;
        currentGame = null;
    }
} 
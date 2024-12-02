package GamePlatform.Game;

public class GameSession {
    private static String currentUser;
    private static String currentGame;
    
    public static void startGameSession(String username, String gameName) {
        currentUser = username;
        currentGame = gameName;
        // 记录游戏开始
        GameAPI.recordGameStart(gameName, username);
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
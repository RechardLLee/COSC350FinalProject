public class UserSession {
    private static String currentUsername = null;
    
    public static void setCurrentUser(String username) {
        currentUsername = username;
    }
    
    public static String getCurrentUser() {
        return currentUsername;
    }
    
    public static boolean isLoggedIn() {
        return currentUsername != null;
    }
    
    public static void logout() {
        currentUsername = null;
    }
} 
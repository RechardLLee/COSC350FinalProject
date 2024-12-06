package GamePlatform.User.Management;

public class UserSession {
    private static String currentUser;
    
    public static void setCurrentUser(String username) {
        System.out.println("Setting current user: " + username);
        currentUser = username;
    }
    
    public static String getCurrentUser() {
        if (currentUser == null) {
            System.err.println("Warning: Attempting to get current user but no user is logged in");
        }
        return currentUser;
    }
    
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public static void clearCurrentUser() {
        System.out.println("Clearing current user");
        currentUser = null;
    }
} 
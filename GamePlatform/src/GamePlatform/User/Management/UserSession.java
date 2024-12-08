package GamePlatform.User.Management;

public class UserSession {
    private static UserSession instance;
    private static String currentUser;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getUsername() {
        return currentUser;
    }

    public void setUsername(String username) {
        currentUser = username;
    }

    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void clearCurrentUser() {
        currentUser = null;
    }
} 
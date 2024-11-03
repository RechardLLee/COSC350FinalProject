import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Button languageButton;
    @FXML private Button snakeButton;
    @FXML private Button game2Button;
    @FXML private Button game3Button;
    @FXML private Button game4Button;
    @FXML private Button bugReportButton;
    @FXML private Button reviewButton;
    @FXML private Button developerButton;
    
    private boolean isEnglish = true;
    
    @FXML
    private void initialize() {
        setLanguage(true);
        LanguageUtil.setEnglish(true);
    }
    
    @FXML
    private void handleLanguageToggle() {
        isEnglish = !isEnglish;
        LanguageUtil.setEnglish(isEnglish);
        setLanguage(isEnglish);
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            languageButton.setText("中文");
            usernameField.setPromptText("Username");
            passwordField.setPromptText("Password");
            loginButton.setText("Login");
            signupButton.setText("Sign Up");
            snakeButton.setText("Snake");
            game2Button.setText("Game 2");
            game3Button.setText("Game 3");
            game4Button.setText("Game 4");
            bugReportButton.setText("Bug Report");
            reviewButton.setText("Review");
            developerButton.setText("Developer Login");
        } else {
            languageButton.setText("English");
            usernameField.setPromptText("用户名");
            passwordField.setPromptText("密码");
            loginButton.setText("登录");
            signupButton.setText("注册");
            snakeButton.setText("贪吃蛇");
            game2Button.setText("游戏2");
            game3Button.setText("游戏3");
            game4Button.setText("游戏4");
            bugReportButton.setText("问题反馈");
            reviewButton.setText("评价");
            developerButton.setText("开发者登录");
        }
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Implement login logic
    }
    
    @FXML
    private void handleSignup() {
        openNewWindow("SignUp.fxml", isEnglish ? "Sign Up" : "注册");
    }
    
    @FXML
    private void handleGameSelect() {
        openNewWindow("GameView.fxml", isEnglish ? "Game" : "游戏");
    }
    
    @FXML
    private void handleBugReport() {
        openNewWindow("BugReport.fxml", isEnglish ? "Bug Report" : "问题反馈");
    }
    
    @FXML
    private void handleReview() {
        openNewWindow("Review.fxml", isEnglish ? "Review" : "评价");
    }
    
    @FXML
    private void handleDeveloperLogin() {
        openNewWindow("DeveloperLogin.fxml", isEnglish ? "Developer Login" : "开发者登录");
    }
    
    private void openNewWindow(String fxmlFile, String title) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading " + fxmlFile + ": " + e.getMessage());
        }
    }
} 
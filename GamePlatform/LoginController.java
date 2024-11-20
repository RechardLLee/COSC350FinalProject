import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML private Label titleLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Button languageButton;
    
    @FXML
    private void initialize() {
        setLanguage(LanguageUtil.isEnglish());
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            titleLabel.setText("Game Platform");
            usernameField.setPromptText("Username");
            passwordField.setPromptText("Password");
            loginButton.setText("Login");
            signupButton.setText("Sign Up");
            languageButton.setText("中文");
        } else {
            titleLabel.setText("游戏平台");
            usernameField.setPromptText("用户名");
            passwordField.setPromptText("密码");
            loginButton.setText("登录");
            signupButton.setText("注册");
            languageButton.setText("English");
        }
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError(
                LanguageUtil.isEnglish() ? "Login Error" : "登录错误",
                LanguageUtil.isEnglish() ? "Please enter username and password" : "请输入用户名和密码"
            );
            return;
        }
        
        if (DatabaseService.validateUser(username, password)) {
            UserSession.setCurrentUser(username);
            try {
                // 加载主界面
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
                Parent root = loader.load();
                
                // 创建新窗口
                Stage mainStage = new Stage();
                mainStage.setTitle(LanguageUtil.isEnglish() ? "Game Platform" : "游戏平台");
                mainStage.setScene(new Scene(root));
                
                // 关闭登录窗口
                ((Stage) loginButton.getScene().getWindow()).close();
                
                // 显示主界面
                mainStage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
                showError(
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? "Failed to load main interface" : "加载主界面失败"
                );
            }
        } else {
            showError(
                LanguageUtil.isEnglish() ? "Login Error" : "登录错误",
                LanguageUtil.isEnglish() ? "Invalid username or password" : "用户名或密码错误"
            );
        }
    }
    
    @FXML
    private void handleSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();
            
            Stage signupStage = new Stage();
            signupStage.setTitle(LanguageUtil.isEnglish() ? "Sign Up" : "注册");
            signupStage.setScene(new Scene(root));
            signupStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Failed to load signup interface" : "加载注册界面失败"
            );
        }
    }
    
    @FXML
    private void handleLanguageToggle() {
        boolean isEnglish = !LanguageUtil.isEnglish();
        LanguageUtil.setEnglish(isEnglish);
        setLanguage(isEnglish);
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
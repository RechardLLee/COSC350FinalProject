package GamePlatform.User.Management;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import GamePlatform.Database.DatabaseService;
import GamePlatform.Utility.LanguageUtil;

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
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // 验证输入
        if (username.isEmpty() || password.isEmpty()) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Username and password are required" : "用户名和密码不能为空"
            );
            return;
        }
        
        // 验证登录
        if (DatabaseService.validateUser(username, password)) {
            // 登录成功
            UserSession.setCurrentUser(username);
            
            try {
                // 打开主界面
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GamePlatform/Main/Interfaces/MainView.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle(LanguageUtil.isEnglish() ? "Game Platform" : "游戏平台");
                stage.setScene(new Scene(root));
                stage.show();
                
                // 关闭登录窗口
                ((Stage) usernameField.getScene().getWindow()).close();
                
            } catch (IOException e) {
                e.printStackTrace();
                showError(
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? 
                        "Failed to open main window: " + e.getMessage() :
                        "打开主窗口失败：" + e.getMessage()
                );
            }
        } else {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? 
                    "Invalid username or password" :
                    "用户名或密码错误"
            );
        }
    }
    
    @FXML
    private void handleSignup() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GamePlatform/User/Management/SignUp.fxml"));
            
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find SignUp.fxml");
            }
            
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
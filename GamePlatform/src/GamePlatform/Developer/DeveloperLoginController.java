package GamePlatform.Developer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import GamePlatform.Database.DatabaseService;
import GamePlatform.Utility.LanguageUtil;

public class DeveloperLoginController {
    @FXML private Label titleLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label securityLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField securityField;
    @FXML private Button loginButton;
    
    @FXML
    private void initialize() {
        setLanguage(LanguageUtil.isEnglish());
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            titleLabel.setText("Developer Login");
            usernameLabel.setText("Username:");
            passwordLabel.setText("Password:");
            securityLabel.setText("What is a good question?");
            usernameField.setPromptText("Enter username");
            passwordField.setPromptText("Enter password");
            securityField.setPromptText("Enter answer");
            loginButton.setText("Login");
        } else {
            titleLabel.setText("开发者登录");
            usernameLabel.setText("用户名:");
            passwordLabel.setText("密码:");
            securityLabel.setText("什么是一个好问题?");
            usernameField.setPromptText("请输入用户名");
            passwordField.setPromptText("请输入密码");
            securityField.setPromptText("请输入答案");
            loginButton.setText("登录");
        }
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String security = securityField.getText();
        
        if (DatabaseService.validateDeveloper(username, password, security)) {
            try {
                // 加载开发者管理界面
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DeveloperManageView.fxml"));
                Parent root = loader.load();
                
                Stage stage = new Stage();
                stage.setTitle(LanguageUtil.isEnglish() ? "Developer Management" : "开发者管理");
                stage.setScene(new Scene(root));
                
                // 关闭登录窗口
                usernameField.getScene().getWindow().hide();
                
                // 显示管理界面
                stage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
                showError(
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? "Failed to load management interface" : "加载管理界面失败"
                );
            }
        } else {
            showError(
                LanguageUtil.isEnglish() ? "Login Error" : "登录错误",
                LanguageUtil.isEnglish() ? "Invalid credentials" : "登录信息无效"
            );
        }
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
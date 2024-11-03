import javafx.fxml.FXML;
import javafx.scene.control.*;

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
            securityLabel.setText("Security Question:");
            usernameField.setPromptText("Enter username");
            passwordField.setPromptText("Enter password");
            securityField.setPromptText("Enter answer");
            loginButton.setText("Login");
        } else {
            titleLabel.setText("开发者登录");
            usernameLabel.setText("用户名:");
            passwordLabel.setText("密码:");
            securityLabel.setText("安全问题:");
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
        // Implement developer login logic
    }
} 
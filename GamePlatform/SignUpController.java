import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SignUpController {
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label passwordLabel;
    @FXML private Label codeLabel;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField codeField;
    @FXML private Button createButton;
    
    @FXML
    private void initialize() {
        setLanguage(LanguageUtil.isEnglish());
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            usernameLabel.setText("Username:");
            emailLabel.setText("Email:");
            passwordLabel.setText("Password:");
            codeLabel.setText("Code:");
            usernameField.setPromptText("Enter username");
            emailField.setPromptText("Enter email");
            passwordField.setPromptText("Enter password");
            codeField.setPromptText("Enter code");
            createButton.setText("Create Account");
        } else {
            usernameLabel.setText("用户名:");
            emailLabel.setText("邮箱:");
            passwordLabel.setText("密码:");
            codeLabel.setText("验证码:");
            usernameField.setPromptText("请输入用户名");
            emailField.setPromptText("请输入邮箱");
            passwordField.setPromptText("请输入密码");
            codeField.setPromptText("请输入验证码");
            createButton.setText("创建账号");
        }
    }
    
    @FXML
    private void handleSignUp() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String code = codeField.getText();
        // Implement signup logic
    }
} 
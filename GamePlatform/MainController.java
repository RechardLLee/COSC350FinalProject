import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;

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
        
        // 设置游戏按钮
        snakeButton.setText("Snake");
        game2Button.setText("Hanoi Tower");
        game3Button.setText("Guess Number");
        // game4Button.setText("Game 4");  // 预留给未来添加的游戏
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
            game2Button.setText("Hanoi Tower");
            game3Button.setText("Guess Number");
            game4Button.setText("Coming Soon");
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
            game2Button.setText("汉诺塔");
            game3Button.setText("猜数字");
            game4Button.setText("即将推出");
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
    private void handleGameSelect(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String gameName = clickedButton.getText();
        
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
            Parent root = loader.load();
            
            GameViewController controller = loader.getController();
            
            // 设置游戏标题和描述
            if (isEnglish) {
                switch(gameName.toLowerCase()) {
                    case "snake":
                        controller.setGameInfo("Snake", 
                            "Classic Snake Game\n\n" +
                            "Control the snake using W, A, S, D keys to eat food and grow longer.\n\n" +
                            "Features:\n" +
                            "- Multiple difficulty levels\n" +
                            "- Score tracking\n" +
                            "- Obstacle avoidance\n" +
                            "- High score system\n\n" +
                            "Try to achieve the highest score without hitting obstacles or yourself!");
                        break;
                        
                    case "hanoi tower":
                        controller.setGameInfo("Hanoi Tower", 
                            "Classic Tower of Hanoi Puzzle\n\n" +
                            "Move all disks from the leftmost rod to the rightmost rod.\n\n" +
                            "Features:\n" +
                            "- Multiple difficulty levels (3-5 disks)\n" +
                            "- Move counter\n" +
                            "- Auto-solve demonstration\n" +
                            "- Algorithm explanation\n\n" +
                            "Rules:\n" +
                            "1. Move only one disk at a time\n" +
                            "2. A larger disk cannot be placed on a smaller disk");
                        break;
                        
                    case "guess number":
                        controller.setGameInfo("Guess Number", 
                            "Number Guessing Game\n\n" +
                            "Try to guess the correct number within limited attempts.\n\n" +
                            "Features:\n" +
                            "- Three difficulty levels\n" +
                            "- Visual feedback\n" +
                            "- Search algorithm visualization\n" +
                            "- Learning tool for binary search\n\n" +
                            "Challenge yourself to find the number in as few attempts as possible!");
                        break;
                }
            } else {
                switch(gameName.toLowerCase()) {
                    case "贪吃蛇":
                        controller.setGameInfo("贪吃蛇", 
                            "经典贪吃蛇游戏\n\n" +
                            "使用W、A、S、D键控制蛇移动，吃食物使蛇变长。\n\n" +
                            "游戏特点：\n" +
                            "- 多个难度级别\n" +
                            "- 分数追踪\n" +
                            "- 障碍物躲避\n" +
                            "- 最高分系统\n\n" +
                            "尽可能获得高分，同时避免撞到障碍物或自己！");
                        break;
                        
                    case "汉诺塔":
                        controller.setGameInfo("汉诺塔", 
                            "经典汉诺塔解谜游戏\n\n" +
                            "将所有圆盘从最左边的柱子移动到最右边的柱子。\n\n" +
                            "游戏特点：\n" +
                            "- 多个难度级别（3-5个圆盘）\n" +
                            "- 移动步数计数\n" +
                            "- 自动解决演示\n" +
                            "- 算法原理说明\n\n" +
                            "规则：\n" +
                            "1. 每次只能移动一个圆盘\n" +
                            "2. 大圆盘不能放在小圆盘上面");
                        break;
                        
                    case "猜数字":
                        controller.setGameInfo("猜数字", 
                            "数字猜谜游戏\n\n" +
                            "在有限的尝试次数内猜出正确的数字。\n\n" +
                            "游戏特点：\n" +
                            "- 三个难度级别\n" +
                            "- 可视化反馈\n" +
                            "- 搜索算法可视化\n" +
                            "- 二分查找学习工具\n\n" +
                            "挑战自己，用最少的次数找到目标数字！");
                        break;
                }
            }
            
            stage.setTitle(gameName);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading GameView.fxml: " + e.getMessage());
        }
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
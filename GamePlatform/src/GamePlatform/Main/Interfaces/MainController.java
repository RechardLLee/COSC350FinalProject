package GamePlatform.Main.Interfaces;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.prefs.Preferences;
import GamePlatform.Utility.LanguageUtil;
import GamePlatform.Game.GameLauncher;
import GamePlatform.User.Management.UserSession;
import GamePlatform.Database.DatabaseService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Platform;

public class MainController {
    
    @FXML private Label balanceLabel;
    @FXML private Label titleLabel;
    @FXML private Button languageButton;
    @FXML private Button addGameButton;
    @FXML private Button bugReportButton;
    @FXML private Button reviewButton;
    @FXML private Button developerButton;
    @FXML private FlowPane gamePane;
    @FXML private AnchorPane mainPane;
    
    private Preferences prefs = Preferences.userNodeForPackage(MainController.class);
    private static final String CUSTOM_GAMES_KEY = "customGames";
    private Timeline balanceUpdateTimeline;
    private static MainController instance;
    
    @FXML
    private void initialize() {
        instance = this;
        setLanguage(LanguageUtil.isEnglish());
        updateBalance();
        
        // 设置FlowPane的响应式布局
        gamePane.prefWrapLengthProperty().bind(gamePane.widthProperty());
        
        // 添加所有游戏
        addGameButton("Snake", "SnakeGame");
        addGameButton("Hanoi Tower", "HanoiTowerGame");
        addGameButton("Guess Number", "GuessNumberGame");
        addGameButton("Tic Tac Toe", "TicTacToe");
        addGameButton("Slot Machine", "SlotMachine");
        addGameButton("Roulette", "RouletteGame");
        addGameButton("Memory Game", "MemoryGame");
        
        // 设置定时更新余额
        balanceUpdateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateBalance()));
        balanceUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        balanceUpdateTimeline.play();
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            titleLabel.setText("My Games");
            languageButton.setText("中文");
            addGameButton.setText("+\nAdd Game");
            bugReportButton.setText("Bug Report");
            reviewButton.setText("Review");
            developerButton.setText("Developer Login");
        } else {
            titleLabel.setText("我的游戏");
            languageButton.setText("English");
            addGameButton.setText("+\n添加游戏");
            bugReportButton.setText("问题反馈");
            reviewButton.setText("评价");
            developerButton.setText("开发者登录");
        }
    }
    
    @FXML
    private void handleLanguageToggle() {
        boolean isEnglish = !LanguageUtil.isEnglish();
        LanguageUtil.setEnglish(isEnglish);
        setLanguage(isEnglish);
    }
    
    @FXML
    private void handleGameSelect(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String gameName = clickedButton.getText();
        
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GamePlatform/Main/Interfaces/GameView.fxml"));
            
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find GameView.fxml");
            }
            
            Parent root = loader.load();
            GameViewController controller = loader.getController();
            
            // Set game info based on selected game
            switch(gameName) {
                case "Snake":
                case "贪吃蛇":
                    controller.setGameInfo("Snake", 
                        "Classic Snake Game\n\n" +
                        "Control the snake to eat food and grow longer.\n\n" +
                        "Features:\n" +
                        "- Multiple difficulty levels\n" +
                        "- Score tracking\n" +
                        "- Obstacle mode\n" +
                        "- Power-ups (costs 10 coins)\n\n" +
                        "Controls:\n" +
                        "WASD - Move snake\n" +
                        "P - Pause game\n" +
                        "R - Restart game\n" +
                        "B - Buy power-up\n\n" +
                        "Game Over:\n" +
                        "- Hitting walls\n" +
                        "- Hitting obstacles\n" +
                        "- Hitting yourself",
                        "snake");
                    break;
                    
                case "Hanoi Tower":
                case "汉诺塔":
                    controller.setGameInfo("Hanoi Tower", 
                        "Classic Tower of Hanoi puzzle game\n\n" +
                        "Move all disks from the leftmost peg to the rightmost peg.\n\n" +
                        "Rules:\n" +
                        "- Only one disk can be moved at a time\n" +
                        "- A larger disk cannot be placed on top of a smaller disk\n\n" +
                        "Features:\n" +
                        "- Multiple difficulty levels\n" +
                        "- Move counter\n" +
                        "- Auto-solve demonstration (costs 5 coins)\n" +
                        "- Hint system (costs 2 coins per hint)",
                        "hanoi_tower");
                    break;
                    
                case "Guess Number":
                case "猜数字":
                    controller.setGameInfo("Guess Number", 
                        "Number Guessing Game\n\n" +
                        "Try to guess the secret number within the given attempts.\n\n" +
                        "Features:\n" +
                        "- Multiple difficulty levels\n" +
                        "- Score based on attempts left\n" +
                        "- Perfect score (10000) for first try\n" +
                        "- Score decreases with more attempts\n" +
                        "- Zero score for failure\n" +
                        "- Hint system (costs 3 coins per hint)\n\n" +
                        "Strategy:\n" +
                        "- Use binary search\n" +
                        "- Think carefully before each guess\n" +
                        "- Fewer attempts = Higher score",
                        "guess_number");
                    break;
                    
                case "Tic Tac Toe":
                case "井字棋":
                    controller.setGameInfo("Tic Tac Toe", 
                        getGameDescription("Tic Tac Toe"),
                        "TicTacToe");
                    break;
                    
                case "Memory Game":
                    controller.setGameInfo("Memory Game", 
                        getGameDescription("Memory Game"),
                        "MemoryGame");
                    break;
                    
                case "Roulette":
                    controller.setGameInfo("Roulette", 
                        getGameDescription("Roulette"),
                        "RouletteGame");
                    break;
                    
                case "Slot Machine":
                    controller.setGameInfo("Slot Machine", 
                        getGameDescription("Slot Machine"),
                        "SlotMachine");
                    break;
            }
            
            Stage stage = new Stage();
            stage.setTitle(gameName);
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? 
                    "Failed to load game interface: " + e.getMessage() :
                    "加载游戏界面失败: " + e.getMessage()
            );
        }
    }
    
    @FXML
    private void handleBugReport() {
        openNewWindow("/GamePlatform/Feedback/BugReport.fxml", 
            LanguageUtil.isEnglish() ? "Bug Report" : "问题反馈");
    }
    
    @FXML
    private void handleReview() {
        openNewWindow("/GamePlatform/Feedback/Review.fxml", 
            LanguageUtil.isEnglish() ? "Review" : "评价");
    }
    
    @FXML
    private void handleDeveloperLogin() {
        openNewWindow("/GamePlatform/Developer/DeveloperLogin.fxml", 
            LanguageUtil.isEnglish() ? "Developer Login" : "开发者登录");
    }
    
    @FXML
    private void handleAddGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LanguageUtil.isEnglish() ? "Select Game Class File" : "选择游戏类文件");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                LanguageUtil.isEnglish() ? "Java Class Files" : "Java类文件", 
                "*.class"
            )
        );
        
        File selectedFile = fileChooser.showOpenDialog(addGameButton.getScene().getWindow());
        if (selectedFile != null) {
            String gameName = showGameNameDialog();
            if (gameName != null && !gameName.trim().isEmpty()) {
                // 获取类名(去掉.class后缀)
                String className = selectedFile.getName().replace(".class", "");
                // 保存完整的类名作为游戏路径
                String classPath = "GamePlatform.Game." + className;
                addCustomGame(gameName, classPath);
            }
        }
    }
    
    private String showGameNameDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(LanguageUtil.isEnglish() ? "Game Name" : "游戏名称");
        dialog.setHeaderText(null);
        dialog.setContentText(LanguageUtil.isEnglish() ? "Enter game name:" : "请输入游戏名称：");
        
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
    
    private void addCustomGame(String gameName, String exePath) {
        try {
            // 保存游戏信息
            Map<String, String> customGames = loadCustomGamesMap();
            customGames.put(gameName, exePath);
            saveCustomGames(customGames);
            
            // 创建新的游戏按钮
            Button gameButton = createGameButton(gameName, exePath);
            
            // 将按钮添加到 FlowPane
            FlowPane flowPane = (FlowPane) addGameButton.getParent();
            
            // 将新按钮添加到 addGameButton 之前
            int addButtonIndex = flowPane.getChildren().indexOf(addGameButton);
            if (addButtonIndex >= 0) {
                flowPane.getChildren().add(addButtonIndex, gameButton);
            } else {
                // 如果找不到 addGameButton，就直接添加末尾
                flowPane.getChildren().add(gameButton);
            }
            
        } catch (Exception e) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Failed to add game" : "添加游戏失败"
            );
            e.printStackTrace();
        }
    }
    
    private Button createGameButton(String gameName, String exePath) {
        Button button = new Button(gameName);
        button.setMinWidth(180);
        button.setMinHeight(180);
        button.setMaxWidth(180);
        button.setMaxHeight(180);
        button.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                       "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        
        button.setOnAction(event -> launchCustomGame(exePath));
        
        // 添加右键菜单用于删除
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem(LanguageUtil.isEnglish() ? "Delete" : "删除");
        deleteItem.setOnAction(event -> removeCustomGame(gameName, button));
        contextMenu.getItems().add(deleteItem);
        button.setContextMenu(contextMenu);
        
        return button;
    }
    
    private void launchCustomGame(String classPath) {
        try {
            // 使用GameLauncher启动游戏
            GameLauncher.launchGame(classPath);
        } catch (Exception e) {
            showError(
                LanguageUtil.isEnglish() ? "Launch Failed" : "启动失败",
                LanguageUtil.isEnglish() ? "Failed to launch game" : "启动游戏失败"
            );
            e.printStackTrace();
        }
    }
    
    private void removeCustomGame(String gameName, Button button) {
        Map<String, String> customGames = loadCustomGamesMap();
        customGames.remove(gameName);
        saveCustomGames(customGames);
        
        FlowPane flowPane = (FlowPane) button.getParent();
        flowPane.getChildren().remove(button);
    }
    
    private Map<String, String> loadCustomGamesMap() {
        String gamesStr = prefs.get(CUSTOM_GAMES_KEY, "");
        Map<String, String> games = new HashMap<>();
        
        if (!gamesStr.isEmpty()) {
            String[] entries = gamesStr.split(";");
            for (String entry : entries) {
                String[] parts = entry.split("\\|");
                if (parts.length == 2) {
                    games.put(parts[0], parts[1]);
                }
            }
        }
        
        return games;
    }
    
    private void saveCustomGames(Map<String, String> games) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : games.entrySet()) {
            if (sb.length() > 0) sb.append(";");
            sb.append(entry.getKey()).append("|").append(entry.getValue());
        }
        prefs.put(CUSTOM_GAMES_KEY, sb.toString());
    }
    
    private void loadCustomGames() {
        Map<String, String> games = loadCustomGamesMap();
        FlowPane flowPane = (FlowPane) addGameButton.getParent();
        
        for (Map.Entry<String, String> entry : games.entrySet()) {
            Button gameButton = createGameButton(entry.getKey(), entry.getValue());
            
            // 将新按钮添加到 addGameButton 之前
            int addButtonIndex = flowPane.getChildren().indexOf(addGameButton);
            if (addButtonIndex >= 0) {
                flowPane.getChildren().add(addButtonIndex, gameButton);
            } else {
                // 如果找不到 addGameButton，就直接添加到末尾
                flowPane.getChildren().add(gameButton);
            }
        }
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void openNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlPath));
            
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }
            
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? 
                    "Failed to load interface: " + e.getMessage() :
                    "加载界面失败: " + e.getMessage()
            );
        }
    }
    
    public void updateBalance() {
        Platform.runLater(() -> {
            String username = UserSession.getCurrentUser();
            int money = DatabaseService.getUserMoney(username);
            balanceLabel.setText(String.format(
                LanguageUtil.isEnglish() ? "Money: $%d" : "金币: %d",
                money
            ));
        });
    }
    
    private void loadUserGames() {
        String username = UserSession.getCurrentUser();
        Map<String, String> userGames = DatabaseService.getUserGames(username);
        
        // 清除现有游戏按钮保留添加按钮）
        gamePane.getChildren().clear();
        gamePane.getChildren().add(addGameButton);
        
        // 添加用户拥有的游戏
        for (Map.Entry<String, String> entry : userGames.entrySet()) {
            Button gameButton = createGameButton(entry.getKey(), entry.getValue());
            // 将新按钮添加到添加按钮之前
            int addButtonIndex = gamePane.getChildren().indexOf(addGameButton);
            gamePane.getChildren().add(addButtonIndex, gameButton);
        }
    }
    
    private void addGameButton(String gameName, String gameId) {
        Button gameButton = new Button(gameName);
        gameButton.setMinWidth(180);
        gameButton.setMinHeight(180);
        gameButton.setMaxWidth(180);
        gameButton.setMaxHeight(180);
        gameButton.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                           "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        
        gameButton.setOnAction(e -> showGameDetails(gameName, gameId));
        gamePane.getChildren().add(gameButton);
    }
    
    private void showGameDetails(String gameName, String gameId) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GamePlatform/Main/Interfaces/GameView.fxml"));
            Parent root = loader.load();
            GameViewController controller = loader.getController();
            
            // 设置游戏信息
            String description = getGameDescription(gameName);
            controller.setGameInfo(gameName, description, "GamePlatform.Game." + gameId);
            
            Stage stage = new Stage();
            stage.setTitle(gameName);
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(e -> controller.cleanup());
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? 
                    "Failed to load game details: " + e.getMessage() :
                    "加载游戏详情失败：" + e.getMessage()
            );
        }
    }
    
    private String getGameDescription(String gameName) {
        switch(gameName) {
            case "Snake":
                return "Classic Snake Game\n\n" +
                       "Control the snake to eat food and grow longer.\n\n" +
                       "Features:\n" +
                       "- Multiple difficulty levels\n" +
                       "- Score tracking\n" +
                       "- Obstacle mode\n" +
                       "- Power-ups (costs 10 coins)\n\n" +
                       "Controls:\n" +
                       "WASD - Move snake\n" +
                       "P - Pause game\n" +
                       "R - Restart game\n" +
                       "B - Buy power-up\n\n" +
                       "Game Over:\n" +
                       "- Hitting walls\n" +
                       "- Hitting obstacles\n" +
                       "- Hitting yourself";
                       
            case "Hanoi Tower":
                return "Classic Tower of Hanoi puzzle game\n\n" +
                       "Move all disks from the leftmost peg to the rightmost peg.\n\n" +
                       "Rules:\n" +
                       "- Only one disk can be moved at a time\n" +
                       "- A larger disk cannot be placed on top of a smaller disk\n\n" +
                       "Features:\n" +
                       "- Multiple difficulty levels\n" +
                       "- Move counter\n" +
                       "- Auto-solve demonstration (costs 5 coins)\n" +
                       "- Hint system (costs 2 coins per hint)";
                       
            case "Guess Number":
                return "Number Guessing Game\n\n" +
                       "Try to guess the secret number within the given attempts.\n\n" +
                       "Features:\n" +
                       "- Multiple difficulty levels\n" +
                       "- Score based on attempts left\n" +
                       "- Perfect score (10000) for first try\n" +
                       "- Score decreases with more attempts\n" +
                       "- Zero score for failure\n" +
                       "- Hint system (costs 3 coins per hint)\n\n" +
                       "Strategy:\n" +
                       "- Use binary search\n" +
                       "- Think carefully before each guess\n" +
                       "- Fewer attempts = Higher score";
                       
            case "Tic Tac Toe":
                return "Classic Tic Tac Toe Game\n\n" +
                       "Play against computer AI with different difficulty levels.\n\n" +
                       "Features:\n" +
                       "- Three difficulty levels (Easy/Medium/Hard)\n" +
                       "- Smart AI opponent\n" +
                       "- Score system based on moves\n" +
                       "- Perfect score (10000) for quick win\n\n" +
                       "Rules:\n" +
                       "- Get three X's in a row to win\n" +
                       "- Block computer's O's to prevent losing\n" +
                       "- Draw game gives 5000 points\n\n" +
                       "Controls:\n" +
                       "- Click empty cell to place X\n" +
                       "- New Game button to restart\n" +
                       "- Choose difficulty level anytime";
                       
            case "Memory Game":
                return "Classic Memory Card Game\n\n" +
                       "Match pairs of cards to earn points.\n\n" +
                       "Features:\n" +
                       "- 4x4 grid of cards\n" +
                       "- Score based on matches\n" +
                       "- 100 points per match\n" +
                       "- Memory training\n\n" +
                       "Rules:\n" +
                       "- Click cards to reveal them\n" +
                       "- Find matching pairs\n" +
                       "- Cards flip back if no match\n" +
                       "- Game ends when all pairs found\n\n" +
                       "Controls:\n" +
                       "- Click to flip cards\n" +
                       "- New Game to restart";
                       
            case "Roulette":
                return "Classic Casino Roulette\n\n" +
                       "Place bets and try your luck!\n\n" +
                       "Features:\n" +
                       "- Number bets (0-36)\n" +
                       "- Color bets (Red/Black)\n" +
                       "- Real money betting\n" +
                       "- High payouts\n\n" +
                       "Payouts:\n" +
                       "- Number bet: 35x\n" +
                       "- Color bet: 2x\n\n" +
                       "Controls:\n" +
                       "- Choose bet type\n" +
                       "- Enter bet amount\n" +
                       "- Click Spin to play\n" +
                       "- New Game to reset";
                       
            case "Slot Machine":
                return "Classic Casino Slot Machine\n\n" +
                       "Try your luck with the slot machine!\n\n" +
                       "Features:\n" +
                       "- Real money betting\n" +
                       "- Multiple winning combinations\n" +
                       "- Animated spinning reels\n" +
                       "- Net profit tracking\n\n" +
                       "Payouts:\n" +
                       "- Three of a kind: 10x bet\n" +
                       "- Two of a kind: 2x bet\n\n" +
                       "Controls:\n" +
                       "- Enter bet amount\n" +
                       "- Click SPIN! to play\n" +
                       "- New Game to reset\n\n" +
                       "Score shows net profit/loss";
                       
            default:
                return "Game description not available.";
        }
    }
    
    // 在窗口关闭时停止更新
    public void cleanup() {
        if (balanceUpdateTimeline != null) {
            balanceUpdateTimeline.stop();
        }
    }
    
    public static MainController getInstance() {
        return instance;
    }
} 
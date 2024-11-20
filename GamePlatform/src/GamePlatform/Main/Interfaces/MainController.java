import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.util.prefs.Preferences;
import java.util.*;
import javafx.scene.layout.FlowPane;

public class MainController {
    
    @FXML private Label titleLabel;
    @FXML private Button languageButton;
    @FXML private Button snakeButton;
    @FXML private Button game2Button;
    @FXML private Button game3Button;
    @FXML private Button addGameButton;
    @FXML private Button bugReportButton;
    @FXML private Button reviewButton;
    @FXML private Button developerButton;
    @FXML private FlowPane gamePane;
    
    private Preferences prefs = Preferences.userNodeForPackage(MainController.class);
    private static final String CUSTOM_GAMES_KEY = "customGames";
    
    @FXML
    private void initialize() {
        setLanguage(LanguageUtil.isEnglish());
        loadCustomGames();
        
        // 设置FlowPane的响应式布局
        gamePane.prefWrapLengthProperty().bind(
            gamePane.widthProperty()
        );
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            titleLabel.setText("Game Center");
            languageButton.setText("中文");
            snakeButton.setText("Snake");
            game2Button.setText("Hanoi Tower");
            game3Button.setText("Guess Number");
            addGameButton.setText("+\nAdd Game");
            bugReportButton.setText("Bug Report");
            reviewButton.setText("Review");
            developerButton.setText("Developer Login");
        } else {
            titleLabel.setText("游戏中心");
            languageButton.setText("English");
            snakeButton.setText("贪吃蛇");
            game2Button.setText("汉诺塔");
            game3Button.setText("猜数字");
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
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
            Parent root = loader.load();
            
            GameViewController controller = loader.getController();
            
            // 设置游戏标题和描述
            if (LanguageUtil.isEnglish()) {
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
                            "挑战自己，用最的次数找到目标数字！");
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
        openNewWindow("BugReport.fxml", LanguageUtil.isEnglish() ? "Bug Report" : "问题反馈");
    }
    
    @FXML
    private void handleReview() {
        openNewWindow("Review.fxml", LanguageUtil.isEnglish() ? "Review" : "评价");
    }
    
    @FXML
    private void handleDeveloperLogin() {
        openNewWindow("DeveloperLogin.fxml", LanguageUtil.isEnglish() ? "Developer Login" : "开发者登录");
    }
    
    @FXML
    private void handleAddGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LanguageUtil.isEnglish() ? "Select Game Executable" : "选择游戏可执行文件");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                LanguageUtil.isEnglish() ? "Executable Files" : "可执行文件", 
                "*.exe"
            )
        );
        
        File selectedFile = fileChooser.showOpenDialog(addGameButton.getScene().getWindow());
        if (selectedFile != null) {
            String gameName = showGameNameDialog();
            if (gameName != null && !gameName.trim().isEmpty()) {
                addCustomGame(gameName, selectedFile.getAbsolutePath());
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
            
            // 将按钮添加到网格
            GridPane gridPane = (GridPane) addGameButton.getParent();
            int numGames = gridPane.getChildren().size() - 1; // 减去添加按钮
            int row = numGames / 4;
            int col = numGames % 4;
            gridPane.add(gameButton, col, row);
            
            // 如果需要，移动添加按钮到新的位置
            GridPane.setColumnIndex(addGameButton, (numGames + 1) % 4);
            GridPane.setRowIndex(addGameButton, (numGames + 1) / 4);
            
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
        button.setPrefWidth(200);
        button.setPrefHeight(200);
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
    
    private void launchCustomGame(String exePath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(exePath);
            pb.start();
        } catch (IOException e) {
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
        
        GridPane gridPane = (GridPane) button.getParent();
        gridPane.getChildren().remove(button);
        reorganizeGrid(gridPane);
    }
    
    private void reorganizeGrid(GridPane gridPane) {
        List<Node> buttons = new ArrayList<>(gridPane.getChildren());
        gridPane.getChildren().clear();
        
        int index = 0;
        for (Node node : buttons) {
            if (node != addGameButton) {
                gridPane.add(node, index % 4, index / 4);
                index++;
            }
        }
        
        gridPane.add(addGameButton, index % 4, index / 4);
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
        for (Map.Entry<String, String> entry : games.entrySet()) {
            Button gameButton = createGameButton(entry.getKey(), entry.getValue());
            
            GridPane gridPane = (GridPane) addGameButton.getParent();
            int numGames = gridPane.getChildren().size() - 1;
            gridPane.add(gameButton, numGames % 4, numGames / 4);
        }
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
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
                        "- Power-ups\n\n" +
                        "Controls:\n" +
                        "WASD - Move snake\n" +
                        "P - Pause game\n" +
                        "R - Restart game");
                    break;
                    
                case "Hanoi Tower":
                case "汉诺塔":
                    controller.setGameInfo("Hanoi Tower", 
                        "Classic Tower of Hanoi puzzle game.\n\n" +
                        "Move all disks from the leftmost peg to the rightmost peg.\n\n" +
                        "Rules:\n" +
                        "- Only one disk can be moved at a time\n" +
                        "- A larger disk cannot be placed on top of a smaller disk\n\n" +
                        "Features:\n" +
                        "- Multiple difficulty levels\n" +
                        "- Move counter\n" +
                        "- Auto-solve demonstration");
                    break;
                    
                case "Guess Number":
                case "猜数字":
                    controller.setGameInfo("Guess Number", 
                        "Number guessing game.\n\n" +
                        "Try to guess the secret number within the given attempts.\n\n" +
                        "Features:\n" +
                        "- Multiple difficulty levels\n" +
                        "- Hints after each guess\n" +
                        "- Score tracking\n" +
                        "- Customizable number range");
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
} 
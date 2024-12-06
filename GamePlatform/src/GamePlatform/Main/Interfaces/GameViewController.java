package GamePlatform.Main.Interfaces;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.util.*;
import java.text.SimpleDateFormat;
import GamePlatform.Utility.LanguageUtil;
import GamePlatform.Game.GameLauncher;
import GamePlatform.Database.DatabaseService;
import GamePlatform.User.Management.UserSession;
import java.nio.file.*;
import java.io.*;
import GamePlatform.Game.GameStats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import GamePlatform.Game.GameRecord;
import GamePlatform.Game.GameRecordManager;
import javafx.scene.image.Image;

public class GameViewController {
    @FXML private Label titleLabel;
    @FXML private ImageView gameIcon;
    @FXML private TextArea gameDescription;
    @FXML private Label totalPlayersLabel;
    @FXML private Label highScoreLabel;
    @FXML private Label topPlayerLabel;
    @FXML private Button startButton;
    @FXML private TableView<GameRecord> historyTable;
    @FXML private TableColumn<GameRecord, String> playerColumn;
    @FXML private TableColumn<GameRecord, Integer> scoreColumn;
    @FXML private TableColumn<GameRecord, Date> dateColumn;
    
    private String gamePath;
    private Timeline refreshTimeline;
    
    @FXML
    private void initialize() {
        // 设置表格列
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        // 设置日期格式
        dateColumn.setCellFactory(column -> new TableCell<GameRecord, Date>() {
            private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item));
                }
            }
        });
        
        // 设置开始按钮文本
        startButton.setText(LanguageUtil.isEnglish() ? "Start Game" : "开始游戏");
        
        // 设置定时刷新
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> refreshGameInfo()));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }
    
    private void refreshGameInfo() {
        if (titleLabel != null && titleLabel.getText() != null) {
            String title = titleLabel.getText();
            
            try {
                java.nio.file.Path path = java.nio.file.Paths.get("game_records/" + title + ".txt");
                if (java.nio.file.Files.exists(path)) {
                    java.util.List<String> lines = java.nio.file.Files.readAllLines(path);
                    
                    java.util.Set<String> uniquePlayers = new java.util.HashSet<>();
                    int highScore = 0;
                    String topPlayer = "";
                    
                    for (String line : lines) {
                        String[] parts = line.split(",");
                        if (parts.length >= 2) {
                            String playerName = parts[0].trim();
                            int score = Integer.parseInt(parts[1].trim());
                            uniquePlayers.add(playerName);
                            
                            if (score > highScore) {
                                highScore = score;
                                topPlayer = playerName;
                            }
                        }
                    }
                    
                    // 更新显示
                    totalPlayersLabel.setText(String.valueOf(uniquePlayers.size()));
                    
                    // 根据游戏类型设置不同的显示格式
                    if (title.equals("Roulette") || title.equals("Slot Machine")) {
                        // 金钱游戏显示带$符号
                        if (highScore > 0) {
                            highScoreLabel.setText(String.format("$%d (by %s)", highScore, topPlayer));
                        } else {
                            highScoreLabel.setText("No records yet");
                        }
                    } else {
                        // 其他游戏显示普通分数
                        if (highScore > 0) {
                            highScoreLabel.setText(String.format("%d (by %s)", highScore, topPlayer));
                        } else {
                            highScoreLabel.setText("No records yet");
                        }
                    }
                    
                    topPlayerLabel.setText(topPlayer.isEmpty() ? "No top player yet" : topPlayer);
                }
            } catch (Exception e) {
                System.err.println("Error loading game records: " + e.getMessage());
            }
            
            // 设置表格列标题
            if (title.equals("Hanoi Tower")) {
                scoreColumn.setText(LanguageUtil.isEnglish() ? 
                    "Score (Perfect=10000)" : "分数 (最优解=10000)");
            } else if (title.equals("Guess Number")) {
                scoreColumn.setText(LanguageUtil.isEnglish() ? 
                    "Score (First Try=10000)" : "分数 (首次猜中=10000)");
            } else {
                scoreColumn.setText(LanguageUtil.isEnglish() ? "Score" : "分数");
            }
            
            // 从文件加载最新记录
            List<GameRecord> records = GameRecordManager.loadGameRecords(title);
            
            // 按分数降序排序
            records.sort((r1, r2) -> Integer.compare(r2.getScore(), r1.getScore()));
            
            // 更新表格
            historyTable.setItems(FXCollections.observableArrayList(records));
            
            // 立即刷新表格显示
            historyTable.refresh();
        }
    }
    
    // 在窗口关闭时停止刷新
    public void cleanup() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
    }
    
    @FXML
    private void handleStartGame() {
        if (gamePath != null) {
            GameLauncher.launchGame(gamePath);
            refreshTimeline.play();
        }
    }
    
    public void setGameInfo(String title, String description, String path) {
        titleLabel.setText(title);
        gameDescription.setText(description);
        gamePath = path;
        
        // 重置ImageView的可见性
        gameIcon.setVisible(true);
        
        // 加载游戏图片
        try {
            String imagePath = null;
            switch(title) {
                case "Snake":
                    imagePath = "/src/GamePlatform/Game/SnakeGame.png";
                    break;
                case "Hanoi Tower":
                    imagePath = "/src/GamePlatform/Game/HanoiTowerGame.png";
                    break;
                case "Guess Number":
                    imagePath = "/src/GamePlatform/Game/GuessNumberGame.png";
                    break;
                case "Tic Tac Toe":
                    imagePath = "/src/GamePlatform/Game/TicTacToe.png";
                    break;
                case "Slot Machine":
                    imagePath = "/src/GamePlatform/Game/SlotMachine.png";
                    break;
                case "Memory Game":
                    imagePath = "/src/GamePlatform/Game/MemoryGame.png";
                    break;
                case "Roulette":
                    imagePath = "/src/GamePlatform/Game/RouletteGame.png";
                    break;
                    
            }
            
            if (imagePath != null) {
                // 使用文件系统路径加载图片
                File imageFile = new File(System.getProperty("user.dir") + imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    gameIcon.setImage(image);
                } else {
                    System.out.println("Cannot find image: " + imageFile.getAbsolutePath());
                    gameIcon.setStyle(
                        "-fx-background-color: #f0f0f0;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);"
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading image for game: " + title);
            e.printStackTrace();
            gameIcon.setStyle(
                "-fx-background-color: #f0f0f0;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);"
            );
        }
        
        // 设置开始按钮文本
        startButton.setText(LanguageUtil.isEnglish() ? "Start Game" : "开始游戏");
        
        // 加载游戏统计信息
        GameStats stats = DatabaseService.getGameStats(title);
        totalPlayersLabel.setText(String.valueOf(stats.getTotalPlayers()));
        highScoreLabel.setText(String.format("%d (by %s)", 
            stats.getHighScore(), stats.getTopPlayer()));
        
        // 立即刷新历史记录
        refreshGameInfo();
    }
} 
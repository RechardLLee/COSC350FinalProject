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
            
            // 同步文件记录到数据库
            GameRecordManager.syncRecordsToDatabase(title);
            
            // 刷新游戏统计信息
            GameStats stats = DatabaseService.getGameStats(title);
            
            // 更新统计标签
            String totalPlayersText = stats.getTotalPlayers() > 0 ? 
                String.valueOf(stats.getTotalPlayers()) : 
                LanguageUtil.isEnglish() ? "No players yet" : "暂无玩家";
            totalPlayersLabel.setText(totalPlayersText);
            
            // 处理高分和最佳玩家的显示
            if (stats.getHighScore() > 0 && stats.getTopPlayer() != null) {
                if (title.equals("Hanoi Tower")) {
                    highScoreLabel.setText(String.format("%d (by %s, %s)", 
                        stats.getHighScore(), 
                        stats.getTopPlayer(),
                        LanguageUtil.isEnglish() ? "Perfect Solution" : "最优解"));
                } else {
                    highScoreLabel.setText(String.format("%d (by %s)", 
                        stats.getHighScore(), stats.getTopPlayer()));
                }
            } else {
                highScoreLabel.setText(LanguageUtil.isEnglish() ? "No records yet" : "暂无记录");
            }
            
            // 更新顶部玩家显示
            if (stats.getTopPlayer() != null) {
                topPlayerLabel.setText(stats.getTopPlayer());
            } else {
                topPlayerLabel.setText(LanguageUtil.isEnglish() ? "No top player yet" : "暂无最佳玩家");
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
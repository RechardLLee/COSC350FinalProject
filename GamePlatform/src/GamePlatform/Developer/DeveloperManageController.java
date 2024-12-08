package GamePlatform.Developer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.*;
import GamePlatform.Database.DatabaseService;
import GamePlatform.Models.DashboardData;
import GamePlatform.User.Management.UserData;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.chart.*;
import javafx.application.Platform;
import javafx.stage.FileChooser;

public class DeveloperManageController {
    @FXML private VBox dashboardPane;
    @FXML private VBox userPane;
    @FXML private Label totalUsersLabel;
    @FXML private Label activeUsersLabel;
    @FXML private Label totalGamesLabel;
    @FXML private LineChart<String,Number> activityChart;
    @FXML private PieChart gameDistChart;
    @FXML private TableView<UserData> userTable;
    @FXML private TableView<ReviewData> reviewTable;
    @FXML private TableView<BugData> bugTable;
    @FXML private VBox feedbackPane;
    @FXML private VBox settingsPane;

    private Timeline refreshTimer;

    @FXML
    private void initialize() {
        dashboardPane.setVisible(true);
        userPane.setVisible(false);
        
        initializeUserTable();
        
        Platform.runLater(() -> {
            initializeCharts();
            refreshDashboard();
        });
        
        refreshTimer = new Timeline(
            new KeyFrame(Duration.seconds(30), 
                e -> refreshDashboard())
        );
        refreshTimer.setCycleCount(Timeline.INDEFINITE);
        refreshTimer.play();
    }

    private void initializeUserTable() {
        TableColumn<UserData, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<UserData, String> usernameCol = new TableColumn<>("用户名");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        TableColumn<UserData, String> emailCol = new TableColumn<>("邮箱");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        TableColumn<UserData, java.util.Date> dateCol = new TableColumn<>("注册时间");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        
        userTable.getColumns().setAll(idCol, usernameCol, emailCol, dateCol);
    }

    private void refreshUserTable() {
        List<UserData> users = DatabaseService.getAllUsers();
        userTable.setItems(FXCollections.observableArrayList(users));
    }

    private void refreshDashboard() {
        try {
            DashboardData data = DatabaseService.getDashboardData();
            
            Platform.runLater(() -> {
                totalUsersLabel.setText(String.valueOf(data.getTotalUsers()));
                activeUsersLabel.setText(String.valueOf(data.getActiveUsers()));
                totalGamesLabel.setText(String.valueOf(data.getTotalGames()));
                
                updateActivityChart(data.getActivityData());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCharts() {
        try {
            activityChart.setAnimated(false);
            ((CategoryAxis) activityChart.getXAxis()).setAnimated(false);
            ((NumberAxis) activityChart.getYAxis()).setAnimated(false);
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("每日活跃用户");
            
            series.getData().add(new XYChart.Data<>("", 0));
            
            activityChart.getData().clear();
            activityChart.getData().add(series);
            
            gameDistChart.setAnimated(false);
            gameDistChart.setTitle("游戏分布");
            gameDistChart.getData().clear();
            gameDistChart.getData().add(new PieChart.Data("暂无数据", 100));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateActivityChart(Map<String, Integer> data) {
        try {
            Platform.runLater(() -> {
                try {
                    if (activityChart.getData().isEmpty()) {
                        XYChart.Series<String, Number> series = new XYChart.Series<>();
                        series.setName("每日活跃用户");
                        activityChart.getData().add(series);
                    }
                    
                    XYChart.Series<String, Number> series = activityChart.getData().get(0);
                    series.getData().clear();
                    
                    if (data.isEmpty()) {
                        series.getData().add(new XYChart.Data<>("", 0));
                    } else {
                        List<Map.Entry<String, Integer>> sortedData = new ArrayList<>(data.entrySet());
                        sortedData.sort(Map.Entry.comparingByKey());
                        
                        for (Map.Entry<String, Integer> entry : sortedData) {
                            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDashboard() {
        dashboardPane.setVisible(true);
        userPane.setVisible(false);
    }

    @FXML 
    private void showUsers() {
        dashboardPane.setVisible(false);
        userPane.setVisible(true);
        refreshUserTable();
    }

    @FXML
    private void showGames() {
        // TODO: 实现游戏管理界面
    }

    @FXML
    private void showFeedback() {
        dashboardPane.setVisible(false);
        userPane.setVisible(false);
        feedbackPane.setVisible(true);
        settingsPane.setVisible(false);
        
        refreshFeedbackTables();
    }

    private void refreshFeedbackTables() {
        // 加载评论数据
        List<ReviewData> reviews = DatabaseService.getAllReviews();
        reviewTable.setItems(FXCollections.observableArrayList(reviews));
        
        // 加载bug报告数据
        List<BugData> bugs = DatabaseService.getAllBugReports();
        bugTable.setItems(FXCollections.observableArrayList(bugs));
    }

    @FXML
    private void handleResolveBug() {
        BugData selectedBug = bugTable.getSelectionModel().getSelectedItem();
        if (selectedBug != null) {
            DatabaseService.updateBugStatus(selectedBug.getId(), "Resolved");
            refreshFeedbackTables();
        }
    }

    @FXML
    private void handleDeleteFeedback() {
        if (reviewTable.isFocused()) {
            ReviewData selectedReview = reviewTable.getSelectionModel().getSelectedItem();
            if (selectedReview != null) {
                DatabaseService.deleteReview(selectedReview.getId());
            }
        } else if (bugTable.isFocused()) {
            BugData selectedBug = bugTable.getSelectionModel().getSelectedItem();
            if (selectedBug != null) {
                DatabaseService.deleteBugReport(selectedBug.getId());
            }
        }
        refreshFeedbackTables();
    }

    @FXML
    private void handleExportFeedback() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Feedback");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            if (reviewTable.isFocused()) {
                DatabaseService.exportReviews(file);
            } else if (bugTable.isFocused()) {
                DatabaseService.exportBugReports(file);
            }
        }
    }

    @FXML
    private void showSettings() {
        // TODO: 实现系统设置界面
    }
} 
package GamePlatform.Developer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.*;
import java.io.File;
import java.util.Date;
import GamePlatform.Database.DatabaseService;
import GamePlatform.Models.DashboardData;
import GamePlatform.User.Management.UserData;
import GamePlatform.Feedback.ReviewData;
import GamePlatform.Feedback.BugData;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.chart.*;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.scene.control.ButtonBar.ButtonData;

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

    @FXML
    private void handleAddUser() {
        Dialog<UserData> dialog = new Dialog<>();
        dialog.setTitle("Add User");
        dialog.setHeaderText("Enter user details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(email, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(password, 1, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if (DatabaseService.registerUser(
                        username.getText(),
                        email.getText(),
                        password.getText())) {
                    return new UserData(0, username.getText(), email.getText(), new java.util.Date());
                }
            }
            return null;
        });

        Optional<UserData> result = dialog.showAndWait();
        result.ifPresent(userData -> refreshUserTable());
    }

    @FXML
    private void handleEditUser() {
        UserData selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to edit.");
            return;
        }

        Dialog<UserData> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Edit user details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField email = new TextField(selectedUser.getEmail());
        PasswordField password = new PasswordField();
        password.setPromptText("New password (leave blank to keep current)");

        grid.add(new Label("Email:"), 0, 0);
        grid.add(email, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (!password.getText().isEmpty()) {
                    DatabaseService.updateUserPassword(selectedUser.getEmail(), password.getText());
                }
                if (!email.getText().equals(selectedUser.getEmail())) {
                    DatabaseService.updateUsername(selectedUser.getEmail(), email.getText());
                }
                return selectedUser;
            }
            return null;
        });

        Optional<UserData> result = dialog.showAndWait();
        result.ifPresent(userData -> refreshUserTable());
    }

    @FXML
    private void handleDeleteUser() {
        UserData selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Delete User Confirmation");
        alert.setContentText("Are you sure you want to delete user: " + selectedUser.getUsername() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (DatabaseService.deleteUser(selectedUser.getUsername())) {
                refreshUserTable();
                showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleBackupDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Backup Database");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("SQLite Database", "*.db")
        );
        
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            if (DatabaseService.backupDatabase(file)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Database backup completed successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to backup database.");
            }
        }
    }

    @FXML
    private void handleRestoreDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Restore Database");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("SQLite Database", "*.db")
        );
        
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Restore");
            alert.setHeaderText("Database Restore");
            alert.setContentText("This will overwrite the current database. Are you sure?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (DatabaseService.restoreDatabase(file)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Database restored successfully.");
                    refreshDashboard();
                    refreshUserTable();
                    refreshFeedbackTables();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to restore database.");
                }
            }
        }
    }

    @FXML
    private void handleResetDatabase() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Database");
        alert.setHeaderText("Database Reset");
        alert.setContentText("This will delete all data and reset the database to its initial state. Are you sure?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (DatabaseService.resetDatabase()) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Database reset successfully.");
                refreshDashboard();
                refreshUserTable();
                refreshFeedbackTables();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to reset database.");
            }
        }
    }

    @FXML
    private void handleViewLogs() {
        List<String> logs = DatabaseService.getSystemLogs();
        TextArea textArea = new TextArea();
        textArea.setText(String.join("\n", logs));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("System Logs");
        dialog.setHeaderText(null);
        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().setPrefSize(600, 400);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        dialog.showAndWait();
    }

    @FXML
    private void handleClearCache() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Cache");
        alert.setHeaderText("Clear System Cache");
        alert.setContentText("This will clear all temporary files and cache. Continue?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (DatabaseService.clearCache()) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Cache cleared successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to clear cache.");
            }
        }
    }
} 
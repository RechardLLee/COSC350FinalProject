package GamePlatform.Developer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import java.sql.*;
import java.util.Date;
import java.util.Optional;
import GamePlatform.Database.DatabaseService;
import GamePlatform.Utility.LanguageUtil;
import GamePlatform.User.Management.UserData;
import GamePlatform.Feedback.ReviewData;
import GamePlatform.Feedback.BugData;
import GamePlatform.Game.GameData;

public class DeveloperManageController {
    @FXML private Label titleLabel;
    @FXML private TableView<UserData> userTable;
    @FXML private TableView<ReviewData> reviewTable;
    @FXML private TableView<BugData> bugTable;
    @FXML private TableView<Object> queryResultTable;
    @FXML private TextArea sqlQueryArea;
    
    // 用户表列
    @FXML private TableColumn<UserData, Integer> idColumn;
    @FXML private TableColumn<UserData, String> usernameColumn;
    @FXML private TableColumn<UserData, String> emailColumn;
    @FXML private TableColumn<UserData, Date> createdDateColumn;
    @FXML private TableColumn<UserData, Integer> balanceColumn;
    
    // 评论表列
    @FXML private TableColumn<ReviewData, Integer> reviewIdColumn;
    @FXML private TableColumn<ReviewData, String> reviewUserColumn;
    @FXML private TableColumn<ReviewData, String> reviewGameColumn;
    @FXML private TableColumn<ReviewData, Integer> ratingColumn;
    @FXML private TableColumn<ReviewData, String> reviewTextColumn;
    @FXML private TableColumn<ReviewData, Date> reviewDateColumn;
    
    // Bug报告表列
    @FXML private TableColumn<BugData, Integer> bugIdColumn;
    @FXML private TableColumn<BugData, String> bugUserColumn;
    @FXML private TableColumn<BugData, String> descriptionColumn;
    @FXML private TableColumn<BugData, String> statusColumn;
    @FXML private TableColumn<BugData, Date> reportDateColumn;
    
    @FXML private TableView<GameData> gameTable;
    @FXML private TableColumn<GameData, String> gameNameColumn;
    @FXML private TableColumn<GameData, String> ownerColumn;
    @FXML private TableColumn<GameData, Date> purchaseDateColumn;
    @FXML private TableColumn<GameData, Integer> playTimeColumn;
    
    @FXML
    private void initialize() {
        initializeColumns();
        loadAllData();
        setLanguage(LanguageUtil.isEnglish());
    }
    
    private void initializeColumns() {
        // 初始化用户表列
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        
        // 初始化评论表列
        reviewIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        reviewUserColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        reviewGameColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        reviewTextColumn.setCellValueFactory(new PropertyValueFactory<>("review"));
        reviewDateColumn.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));
        
        // 初始化Bug报告表列
        bugIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bugUserColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        reportDateColumn.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        
        // Initialize game table columns
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        playTimeColumn.setCellValueFactory(new PropertyValueFactory<>("playTime"));
    }
    
    private void loadAllData() {
        loadUsers();
        loadReviews();
        loadBugReports();
    }
    
    private void loadUsers() {
        try (Connection conn = DatabaseService.getConnection();
             ResultSet rs = DatabaseService.getUsers()) {
            
            ObservableList<UserData> users = FXCollections.observableArrayList();
            while (rs.next()) {
                users.add(new UserData(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getTimestamp("created_date")
                ));
            }
            userTable.setItems(users);
            
        } catch (SQLException e) {
            showError(
                LanguageUtil.isEnglish() ? "Database Error" : "数据库错误",
                LanguageUtil.isEnglish() ? 
                    "Failed to load users: " + e.getMessage() :
                    "加载用户失败：" + e.getMessage()
            );
        }
    }
    
    private void loadReviews() {
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM GameReviews")) {
            
            ObservableList<ReviewData> reviews = FXCollections.observableArrayList();
            while (rs.next()) {
                reviews.add(new ReviewData(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("game_name"),
                    rs.getInt("rating"),
                    rs.getString("review"),
                    rs.getTimestamp("review_date")
                ));
            }
            reviewTable.setItems(reviews);
            
        } catch (SQLException e) {
            showError("Database Error", "Failed to load reviews: " + e.getMessage());
        }
    }
    
    private void loadBugReports() {
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM BugReports")) {
            
            ObservableList<BugData> bugs = FXCollections.observableArrayList();
            while (rs.next()) {
                bugs.add(new BugData(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getTimestamp("report_date")
                ));
            }
            bugTable.setItems(bugs);
            
        } catch (SQLException e) {
            showError("Database Error", "Failed to load bug reports: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleExecuteQuery() {
        String query = sqlQueryArea.getText().trim();
        if (query.isEmpty()) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please enter a SQL query" : "请输入SQL查询"
            );
            return;
        }
        
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            // 清除现有列
            queryResultTable.getColumns().clear();
            
            // 获取结果集元数据
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // 创建列
            for (int i = 1; i <= columnCount; i++) {
                final int j = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(metaData.getColumnName(i));
                col.setCellValueFactory(cellData -> 
                    new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().get(j-1)
                    )
                );
                queryResultTable.getColumns().add((TableColumn)col);
            }
            
            // 添加数据
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    row.add(value == null ? "null" : value.toString());
                }
                data.add(row);
            }
            
            // 使用类型转换来解决类型不匹配问题
            @SuppressWarnings("unchecked")
            TableView<ObservableList<String>> tableView = (TableView<ObservableList<String>>) (TableView<?>) queryResultTable;
            tableView.setItems(data);
            
        } catch (SQLException e) {
            showError("Query Error", e.getMessage());
        }
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            titleLabel.setText("Developer Management");
        } else {
            titleLabel.setText("开发者管理");
        }
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @FXML
    private void handleDeleteUser() {
        UserData selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please select a user to delete" : "请选择要删除的用户"
            );
            return;
        }
        
        // 显示确认对话框
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageUtil.isEnglish() ? "Confirm Delete" : "确认删除");
        alert.setHeaderText(null);
        alert.setContentText(LanguageUtil.isEnglish() ? 
            "Are you sure you want to delete user: " + selectedUser.getUsername() + "?" :
            "确定要删除用户：" + selectedUser.getUsername() + "吗？");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            if (DatabaseService.deleteUser(selectedUser.getUsername())) {
                loadUsers();  // 重新加载用户列表
                showInfo(
                    LanguageUtil.isEnglish() ? "Success" : "成功",
                    LanguageUtil.isEnglish() ? "User deleted successfully" : "用户删除成功"
                );
            } else {
                showError(
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? "Failed to delete user" : "删除用户失败"
                );
            }
        }
    }
    
    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @FXML
    private void handleResetDatabase() {
        // 显示警告对话框
        Alert confirmAlert = new Alert(Alert.AlertType.WARNING);
        confirmAlert.setTitle(LanguageUtil.isEnglish() ? "Warning" : "警告");
        confirmAlert.setHeaderText(
            LanguageUtil.isEnglish() ? 
            "You are about to reset the entire database" :
            "您即将重置整个数据库"
        );
        confirmAlert.setContentText(
            LanguageUtil.isEnglish() ? 
            "This action will delete ALL data and cannot be undone. Are you sure?" :
            "此操作将删除所有数据且无法撤销。您确定要继续吗？"
        );
        
        // 添加自定义按钮
        ButtonType confirmButton = new ButtonType(
            LanguageUtil.isEnglish() ? "Yes, Reset Database" : "是的，重置数据库", 
            ButtonBar.ButtonData.OK_DONE
        );
        ButtonType cancelButton = new ButtonType(
            LanguageUtil.isEnglish() ? "Cancel" : "取消", 
            ButtonBar.ButtonData.CANCEL_CLOSE
        );
        confirmAlert.getButtonTypes().setAll(confirmButton, cancelButton);
        
        // 要求输入确认码
        if (confirmAlert.showAndWait().get() == confirmButton) {
            TextInputDialog confirmDialog = new TextInputDialog();
            confirmDialog.setTitle(LanguageUtil.isEnglish() ? "Confirm Reset" : "确认重置");
            confirmDialog.setHeaderText(
                LanguageUtil.isEnglish() ? 
                "Please type 'RESET' to confirm" :
                "请输入'RESET'以确认"
            );
            confirmDialog.setContentText(
                LanguageUtil.isEnglish() ? 
                "Confirmation code:" :
                "确认码："
            );
            
            Optional<String> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get().equals("RESET")) {
                if (DatabaseService.resetDatabase()) {
                    showInfo(
                        LanguageUtil.isEnglish() ? "Success" : "成功",
                        LanguageUtil.isEnglish() ? 
                        "Database has been reset successfully" :
                        "数据库已成功重置"
                    );
                    // 重新加载所有数据
                    loadAllData();
                } else {
                    showError(
                        LanguageUtil.isEnglish() ? "Error" : "错误",
                        LanguageUtil.isEnglish() ? 
                        "Failed to reset database" :
                        "重置数据库失败"
                    );
                }
            } else {
                showError(
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? 
                    "Invalid confirmation code" :
                    "确认码错误"
                );
            }
        }
    }
    
    @FXML
    private void handleAddBalance() {
        UserData selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please select a user" : "请选择用户"
            );
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(LanguageUtil.isEnglish() ? "Add Balance" : "添加余额");
        dialog.setHeaderText(null);
        dialog.setContentText(LanguageUtil.isEnglish() ? "Enter amount:" : "输入金额：");
        
        dialog.showAndWait().ifPresent(amount -> {
            try {
                int value = Integer.parseInt(amount);
                if (value <= 0) throw new NumberFormatException();
                
                int currentBalance = DatabaseService.getUserBalance(selectedUser.getUsername());
                if (DatabaseService.updateUserBalance(selectedUser.getUsername(), currentBalance + value)) {
                    loadAllData();  // Refresh data
                    showInfo(
                        LanguageUtil.isEnglish() ? "Success" : "成功",
                        LanguageUtil.isEnglish() ? 
                            "Balance updated successfully" :
                            "余额更新成功"
                    );
                }
            } catch (NumberFormatException e) {
                showError(
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? 
                        "Please enter a valid positive number" :
                        "请输入有效的正数"
                );
            }
        });
    }
    
    @FXML
    private void handleGameStats() {
        // ... implement game statistics display ...
    }
    
    @FXML
    private void handleRemoveGame() {
        // ... implement game removal ...
    }
    
    @FXML
    private void handleReviewAnalytics() {
        // ... implement review analytics ...
    }
    
    @FXML
    private void handleUpdateBugStatus() {
        // ... implement bug status update ...
    }
    
    @FXML
    private void handleBackupDatabase() {
        // ... implement database backup ...
    }
    
    @FXML
    private void handleRestoreDatabase() {
        // ... implement database restore ...
    }
    
    @FXML
    private void handleViewLogs() {
        // ... implement log viewing ...
    }
    
    @FXML
    private void handleSaveQuery() {
        // ... implement query saving ...
    }
    
    @FXML
    private void handleLoadQuery() {
        // ... implement query loading ...
    }
    
    @FXML
    private void handleDeleteReview() {
        ReviewData selectedReview = reviewTable.getSelectionModel().getSelectedItem();
        if (selectedReview == null) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please select a review to delete" : "请选择要删除的评论"
            );
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageUtil.isEnglish() ? "Confirm Delete" : "确认删除");
        alert.setHeaderText(null);
        alert.setContentText(LanguageUtil.isEnglish() ? 
            "Are you sure you want to delete this review?" :
            "确定要删除这条评论吗？");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            try (Connection conn = DatabaseService.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "DELETE FROM GameReviews WHERE id = ?"
                 )) {
                
                pstmt.setInt(1, selectedReview.getId());
                if (pstmt.executeUpdate() > 0) {
                    loadReviews();  // 重新加载评论列表
                    showInfo(
                        LanguageUtil.isEnglish() ? "Success" : "成功",
                        LanguageUtil.isEnglish() ? "Review deleted successfully" : "评论删除成功"
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showError(
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? 
                        "Failed to delete review: " + e.getMessage() :
                        "删除评论失败：" + e.getMessage()
                );
            }
        }
    }
    
    @FXML
    private void handleDeleteBugReport() {
        BugData selectedBug = bugTable.getSelectionModel().getSelectedItem();
        if (selectedBug == null) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please select a bug report to delete" : "请选择要删除的问题报告"
            );
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LanguageUtil.isEnglish() ? "Confirm Delete" : "确认删除");
        alert.setHeaderText(null);
        alert.setContentText(LanguageUtil.isEnglish() ? 
            "Are you sure you want to delete this bug report?" :
            "确定要删除这条问题报告吗？");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            try (Connection conn = DatabaseService.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "DELETE FROM BugReports WHERE id = ?"
                 )) {
                
                pstmt.setInt(1, selectedBug.getId());
                if (pstmt.executeUpdate() > 0) {
                    loadBugReports();  // 重新加载问题报告列表
                    showInfo(
                        LanguageUtil.isEnglish() ? "Success" : "成功",
                        LanguageUtil.isEnglish() ? "Bug report deleted successfully" : "问题报告删除成功"
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showError(
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? 
                        "Failed to delete bug report: " + e.getMessage() :
                        "删除问题报告失败：" + e.getMessage()
                );
            }
        }
    }
} 
package GamePlatform.Feedback;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import GamePlatform.Database.DatabaseService;
import GamePlatform.User.Management.UserSession;
import GamePlatform.Utility.LanguageUtil;

public class ReviewController {
    @FXML private Label titleLabel;
    @FXML private Label gameLabel;
    @FXML private ComboBox<String> gameComboBox;
    @FXML private Label ratingLabel;
    @FXML private RadioButton star1;
    @FXML private RadioButton star2;
    @FXML private RadioButton star3;
    @FXML private RadioButton star4;
    @FXML private RadioButton star5;
    @FXML private TextArea reviewArea;
    @FXML private Button submitButton;
    
    private ToggleGroup ratingGroup;
    
    @FXML
    private void initialize() {
        ratingGroup = new ToggleGroup();
        star1.setToggleGroup(ratingGroup);
        star2.setToggleGroup(ratingGroup);
        star3.setToggleGroup(ratingGroup);
        star4.setToggleGroup(ratingGroup);
        star5.setToggleGroup(ratingGroup);
        
        // 添加游戏列表
        gameComboBox.setItems(FXCollections.observableArrayList(
            "Snake",
            "Hanoi Tower",
            "Guess Number"
        ));
        
        setLanguage(LanguageUtil.isEnglish());
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            titleLabel.setText("Game Review");
            gameLabel.setText("Select Game:");
            ratingLabel.setText("Rating:");
            star1.setText("1 Star");
            star2.setText("2 Stars");
            star3.setText("3 Stars");
            star4.setText("4 Stars");
            star5.setText("5 Stars");
            reviewArea.setPromptText("Please enter your review...");
            submitButton.setText("Submit Review");
        } else {
            titleLabel.setText("游戏评价");
            gameLabel.setText("选择游戏：");
            ratingLabel.setText("评分：");
            star1.setText("1星");
            star2.setText("2星");
            star3.setText("3星");
            star4.setText("4星");
            star5.setText("5星");
            reviewArea.setPromptText("请输入您的评价...");
            submitButton.setText("提交评价");
        }
    }
    
    @FXML
    private void handleSubmitReview() {
        if (!UserSession.isLoggedIn()) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please login first" : "请先登录"
            );
            return;
        }
        
        String selectedGame = gameComboBox.getValue();
        if (selectedGame == null) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please select a game" : "请选择游戏"
            );
            return;
        }
        
        RadioButton selectedRating = (RadioButton) ratingGroup.getSelectedToggle();
        if (selectedRating == null) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please select a rating" : "请选择评分"
            );
            return;
        }
        
        String review = reviewArea.getText().trim();
        if (review.isEmpty()) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please enter your review" : "请输入评价内容"
            );
            return;
        }
        
        int rating = Integer.parseInt(selectedRating.getText().substring(0, 1));
        
        if (DatabaseService.saveGameReview(UserSession.getCurrentUser(), selectedGame, rating, review)) {
            showInfo(
                LanguageUtil.isEnglish() ? "Success" : "成功",
                LanguageUtil.isEnglish() ? "Review submitted successfully" : "评价提交成功"
            );
            // 关闭窗口
            submitButton.getScene().getWindow().hide();
        } else {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Failed to submit review" : "评价提交失败"
            );
        }
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showInfo(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ReviewController {
    @FXML private Label titleLabel;
    @FXML private Label ratingLabel;
    @FXML private RadioButton star1;
    @FXML private RadioButton star2;
    @FXML private RadioButton star3;
    @FXML private RadioButton star4;
    @FXML private RadioButton star5;
    @FXML private TextArea reviewArea;
    @FXML private Button submitButton;
    
    @FXML
    private void initialize() {
        setLanguage(LanguageUtil.isEnglish());
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            titleLabel.setText("Game Review");
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
            ratingLabel.setText("评分:");
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
        // Get rating
        int rating = 0;
        if(star1.isSelected()) rating = 1;
        else if(star2.isSelected()) rating = 2;
        else if(star3.isSelected()) rating = 3;
        else if(star4.isSelected()) rating = 4;
        else if(star5.isSelected()) rating = 5;
        
        String review = reviewArea.getText();
        // Implement submit review logic
    }
} 
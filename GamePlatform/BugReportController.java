import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BugReportController {
    @FXML private Label titleLabel;
    @FXML private TextArea bugDescriptionArea;
    @FXML private Button uploadButton;
    @FXML private Button submitButton;
    
    @FXML
    private void initialize() {
        setLanguage(LanguageUtil.isEnglish());
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            titleLabel.setText("Bug Report");
            bugDescriptionArea.setPromptText("Please enter bug description...");
            uploadButton.setText("Upload Screenshot");
            submitButton.setText("Submit Report");
        } else {
            titleLabel.setText("问题反馈");
            bugDescriptionArea.setPromptText("请输入问题描述...");
            uploadButton.setText("上传截图");
            submitButton.setText("提交反馈");
        }
    }
    
    @FXML
    private void handleUploadScreenshot() {
        // Implement upload screenshot logic
    }
    
    @FXML
    private void handleSubmitReport() {
        String description = bugDescriptionArea.getText();
        // Implement submit report logic
    }
} 
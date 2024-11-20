import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import java.io.File;

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
            bugDescriptionArea.setPromptText("Please describe the bug in detail...");
            uploadButton.setText("Upload Screenshot");
            submitButton.setText("Submit Report");
        } else {
            titleLabel.setText("问题反馈");
            bugDescriptionArea.setPromptText("请详细描述遇到的问题...");
            uploadButton.setText("上传截图");
            submitButton.setText("提交反馈");
        }
    }
    
    @FXML
    private void handleUploadScreenshot() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LanguageUtil.isEnglish() ? "Select Screenshot" : "选择截图");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (selectedFile != null) {
            // TODO: 处理截图上传
            showInfo(
                LanguageUtil.isEnglish() ? "Success" : "成功",
                LanguageUtil.isEnglish() ? "Screenshot uploaded" : "截图已上传"
            );
        }
    }
    
    @FXML
    private void handleSubmitReport() {
        if (!UserSession.isLoggedIn()) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please login first" : "请先登录"
            );
            return;
        }
        
        String description = bugDescriptionArea.getText().trim();
        if (description.isEmpty()) {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Please describe the bug" : "请描述问题"
            );
            return;
        }
        
        if (DatabaseService.saveBugReport(UserSession.getCurrentUser(), description)) {
            showInfo(
                LanguageUtil.isEnglish() ? "Success" : "成功",
                LanguageUtil.isEnglish() ? "Bug report submitted successfully" : "问题反馈提交成功"
            );
            // 关闭窗口
            submitButton.getScene().getWindow().hide();
        } else {
            showError(
                LanguageUtil.isEnglish() ? "Error" : "错误",
                LanguageUtil.isEnglish() ? "Failed to submit bug report" : "问题反馈提交失败"
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
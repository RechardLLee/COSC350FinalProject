import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

public class GameViewController {
    @FXML private Label titleLabel;
    @FXML private ImageView gameIcon;
    @FXML private TextArea gameDescription;
    @FXML private Button buyButton;
    @FXML private Button downloadButton;
    @FXML private Button startButton;
    
    @FXML
    private void initialize() {
        setLanguage(LanguageUtil.isEnglish());
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            titleLabel.setText("Snake");
            gameDescription.setText("Game Description: This is a classic Snake game...");
            buyButton.setText("Buy Game");
            downloadButton.setText("Download");
            startButton.setText("Start Game");
        } else {
            titleLabel.setText("贪吃蛇");
            gameDescription.setText("游戏描述：这是一个经典的贪吃蛇游戏...");
            buyButton.setText("购买游戏");
            downloadButton.setText("下载");
            startButton.setText("开始游戏");
        }
    }
    
    @FXML
    private void handleBuyGame() {
        // 实现购买逻辑
    }
    
    @FXML
    private void handleDownload() {
        // 实现下载逻辑
    }
    
    @FXML
    private void handleStartGame() {
        // 实现启动游戏逻辑
    }
} 
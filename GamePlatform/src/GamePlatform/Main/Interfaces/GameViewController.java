package GamePlatform.Main.Interfaces;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import GamePlatform.Utility.LanguageUtil;
import GamePlatform.Game.GameLauncher;

public class GameViewController {
    @FXML private Label titleLabel;
    @FXML private ImageView gameIcon;
    @FXML private TextArea gameDescription;
    @FXML private Button buyButton;
    @FXML private Button startButton;
    
    @FXML
    private void initialize() {
        setLanguage(LanguageUtil.isEnglish());
    }
    
    private void setLanguage(boolean english) {
        if (english) {
            buyButton.setText("Buy Game");
            startButton.setText("Start Game");
        } else {
            buyButton.setText("购买游戏");
            startButton.setText("开始游戏");
        }
    }
    
    @FXML
    private void handleBuyGame() {
        // 实现购买逻辑
    }
    
    @FXML
    private void handleStartGame() {
        GameLauncher.launchGame(titleLabel.getText());
    }
    
    public void setGameInfo(String title, String description) {
        titleLabel.setText(title);
        gameDescription.setText(description);
    }
} 
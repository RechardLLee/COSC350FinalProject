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
        String gameName = getGameFileName(titleLabel.getText());
        GameLauncher.downloadGame(gameName);
    }
    
    @FXML
    private void handleStartGame() {
        String gameName = getGameFileName(titleLabel.getText());
        GameLauncher.launchGame(gameName);
    }
    
    private String getGameFileName(String gameTitle) {
        // 将游戏标题转换为对应的文件名
        switch(gameTitle.toLowerCase()) {
            case "snake":
            case "贪吃蛇":
                return "snake_game";
            case "hanoi tower":
            case "汉诺塔":
                return "hanoi_tower_game";
            case "guess number":
            case "猜数字":
                return "guess_number_game";
            default:
                return gameTitle.toLowerCase().replace(" ", "_");
        }
    }
    
    public void setGameInfo(String title, String description) {
        titleLabel.setText(title);
        gameDescription.setText(description);
    }
} 
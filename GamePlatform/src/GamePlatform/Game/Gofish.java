package GamePlatform.Game;

import GamePlatform.Game.GoFish.GoFish;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

public class Gofish extends BaseGame {
    private Stage goFishStage;
    
    public Gofish() {
        super("Go Fish");
        try {
            // 初始化JavaFX
            new JFXPanel();
            
            // 设置空白JFrame
            setUndecorated(true);
            setSize(0, 0);
            setLocation(-100, -100);
            setVisible(false);
            
            // 在JavaFX线程中启动游戏
            Platform.runLater(() -> {
                try {
                    GoFish goFishGame = new GoFish();
                    goFishStage = new Stage();
                    goFishGame.start(goFishStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void dispose() {
        Platform.runLater(() -> {
            if (goFishStage != null) {
                goFishStage.close();
            }
        });
        super.dispose();
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Gofish();
        });
    }
}

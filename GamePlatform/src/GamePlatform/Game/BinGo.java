package GamePlatform.Game;

import javax.swing.JFrame;
import GamePlatform.Game.bingo.BingoRunner;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class BinGo extends JFrame {
    private BingoRunner bingoGame;
    
    public BinGo() {
        try {
            // 初始化JavaFX
            new JFXPanel(); // 这会初始化JavaFX环境
            
            // 在JavaFX线程中启动游戏
            Platform.runLater(() -> {
                try {
                    bingoGame = new BingoRunner();
                    bingoGame.start(new javafx.stage.Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
            // 设置JFrame属性
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(0, 0);
            setLocationRelativeTo(null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new BinGo();
        });
    }
}

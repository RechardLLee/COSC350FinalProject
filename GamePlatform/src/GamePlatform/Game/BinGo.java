package GamePlatform.Game;

import javax.swing.JFrame;
import GamePlatform.Game.bingo.BingoRunner;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BinGo extends JFrame {
    private BingoRunner bingoGame;
    
    public BinGo() {
        try {
            // 初始化JavaFX
            new JFXPanel();
            
            // 在JavaFX线程中启动游戏
            Platform.runLater(() -> {
                try {
                    Stage stage = new javafx.stage.Stage();
                    bingoGame = new BingoRunner();
                    
                    // 添加窗口关闭事件处理
                    stage.setOnCloseRequest((WindowEvent event) -> {
                        dispose();
                    });
                    
                    bingoGame.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
            // 设置JFrame属性但不可见
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(1, 1);
            setLocationRelativeTo(null);
            setVisible(false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new BinGo();
        });
    }
}

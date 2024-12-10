package GamePlatform.Game;

import javax.swing.JFrame;
import GamePlatform.Game.BlackJackGui.BlackJackGame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BlackJack extends BaseGame {
    private BlackJackGame blackJackGame;
    
    public BlackJack() {
        super("Black Jack");
        try {
            // 创建游戏实例但不显示
            blackJackGame = new BlackJackGame();
            
            // 不创建任何可见窗口
            setUndecorated(true);
            setSize(0, 0);
            setLocation(-100, -100);
            setVisible(false);
            
            // 显示游戏窗口并添加关闭监听器
            blackJackGame.showGame();
            blackJackGame.frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // 当游戏窗口关闭时，同时关闭主窗口
                    dispose();
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void dispose() {
        if (blackJackGame != null && blackJackGame.frame != null) {
            blackJackGame.frame.dispose();  // 关闭游戏窗口
        }
        super.dispose();  // 关闭主窗口
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new BlackJack();  // 不需要setVisible(true)
        });
    }
}

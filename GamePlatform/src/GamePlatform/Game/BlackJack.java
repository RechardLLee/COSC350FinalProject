package GamePlatform.Game;

import javax.swing.JFrame;
import GamePlatform.Game.BlackJackGui.BlackJack;

public class BlackJack extends JFrame {
    private BlackJack blackJackGame;
    
    public BlackJack() {
        try {
            // 创建21点游戏实例
            blackJackGame = new BlackJack();
            
            // 设置JFrame属性
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
        if (blackJackGame != null) {
            blackJackGame.frame.dispose();
        }
        super.dispose();
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new BlackJack();
        });
    }
}

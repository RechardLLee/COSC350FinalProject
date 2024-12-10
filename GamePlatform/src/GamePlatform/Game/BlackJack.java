package GamePlatform.Game;

import javax.swing.JFrame;
import GamePlatform.Game.BlackJackGui.BlackJackGame;

public class BlackJack extends BaseGame {
    private BlackJackGame blackJackGame;
    
    public BlackJack() {
        super("Black Jack");
        try {
            blackJackGame = new BlackJackGame();
            
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
        if (blackJackGame != null && blackJackGame.frame != null) {
            blackJackGame.frame.dispose();
        }
        super.dispose();
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new BlackJack().setVisible(true);
        });
    }
}

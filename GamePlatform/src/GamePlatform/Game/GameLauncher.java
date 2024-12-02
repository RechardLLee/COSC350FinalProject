package GamePlatform.Game;

import javax.swing.*;
import GamePlatform.Utility.LanguageUtil;

public class GameLauncher {
    public static void launchGame(String gameName) {
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame game = null;
                
                // 尝试通过类名加载游戏
                try {
                    Class<?> gameClass = Class.forName(gameName);
                    game = (JFrame) gameClass.getDeclaredConstructor().newInstance();
                } catch (ClassNotFoundException e) {
                    // 如果找不到类,尝试使用内置游戏
                    switch(gameName.toLowerCase()) {
                        case "snake":
                        case "贪吃蛇":
                            game = new SnakeGame();
                            break;
                            
                        case "hanoi tower":
                        case "汉诺塔":
                            game = new HanoiTowerGame();
                            break;
                            
                        case "guess number":
                        case "猜数字":
                            game = new GuessNumberGame();
                            break;
                            
                        default:
                            showErrorMessage(
                                LanguageUtil.isEnglish() ? "Error" : "错误",
                                LanguageUtil.isEnglish() ? 
                                    "Unknown game: " + gameName :
                                    "未知游戏：" + gameName
                            );
                            return;
                    }
                }
                
                if (game != null) {
                    game.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    game.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage(
                    LanguageUtil.isEnglish() ? "Launch Failed" : "启动失败",
                    e.getMessage()
                );
            }
        });
    }
    
    private static void showErrorMessage(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
            );
        });
    }
} 
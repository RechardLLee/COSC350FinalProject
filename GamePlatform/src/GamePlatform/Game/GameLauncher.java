package GamePlatform.Game;

import javax.swing.*;
import GamePlatform.Utility.LanguageUtil;

public class GameLauncher {
    public static void launchGame(String gameName) {
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame game = null;
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
                        
                    case "memory game":
                        game = new MemoryGame();
                        break;
                        
                    case "slot machine":
                        game = new SlotMachine();
                        break;
                        
                    case "roulette":
                        game = new RouletteGame();
                        break;
                        
                    case "tic tac toe":
                        game = new TicTacToe();
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
public class Snake {
    private long startTime;
    private int score;
    
    public void startGame() {
        startTime = System.currentTimeMillis();
        score = 0;
    }
    
    public void gameOver() {
        int playTime = (int)((System.currentTimeMillis() - startTime) / 1000);
        GameAPI.recordGameScore("Snake", username, score, playTime);
        
        // 显示游戏统计
        Map<String, Object> stats = GameAPI.getGameStats("Snake");
        showGameStats(stats);
    }
    
    private void showGameStats(Map<String, Object> stats) {
        String message = String.format(
            "Game Over!\n" +
            "Your Score: %d\n" +
            "Highest Score: %d\n" +
            "Total Players: %d\n" +
            "Average Score: %.2f",
            score,
            stats.get("highestScore"),
            stats.get("totalPlayers"),
            stats.get("averageScore")
        );
        JOptionPane.showMessageDialog(null, message);
    }
} 
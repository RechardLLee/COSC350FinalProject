import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameLauncher {
    private static final String GAMES_DIR = "games/exe";
    
    private static void createDirectories() throws IOException {
        String currentDir = System.getProperty("user.dir");
        Path gamesPath = Paths.get(currentDir, GAMES_DIR);
        
        if (!Files.exists(gamesPath)) {
            Files.createDirectories(gamesPath);
        }
        
        System.out.println("Current directory: " + currentDir);
        System.out.println("Games directory: " + gamesPath);
    }
    
    public static void downloadGame(String gameName) {
        try {
            createDirectories();
            
            String currentDir = System.getProperty("user.dir");
            Path sourceFile = Paths.get(currentDir, "game", gameName + ".exe");
            Path targetFile = Paths.get(currentDir, GAMES_DIR, gameName + ".exe");
            
            System.out.println("Copying from: " + sourceFile);
            System.out.println("Copying to: " + targetFile);
            
            if (!Files.exists(sourceFile)) {
                showMessage(AlertType.ERROR,
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? 
                        "Source game file not found: " + sourceFile :
                        "源游戏文件不存在: " + sourceFile
                );
                return;
            }
            
            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            showMessage(AlertType.INFORMATION,
                LanguageUtil.isEnglish() ? "Download Success" : "下载成功",
                LanguageUtil.isEnglish() ? 
                    "Game has been downloaded successfully" :
                    "游戏已成功下载"
            );
            
        } catch (IOException e) {
            showMessage(AlertType.ERROR,
                LanguageUtil.isEnglish() ? "Download Failed" : "下载失败",
                LanguageUtil.isEnglish() ? 
                    "Failed to download game: " + e.getMessage() :
                    "下载游戏失败: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }
    
    public static void launchGame(String gameName) {
        try {
            String currentDir = System.getProperty("user.dir");
            Path gamePath = Paths.get(currentDir, GAMES_DIR, gameName + ".exe");
            System.out.println("Game path: " + gamePath);
            
            if (!Files.exists(gamePath)) {
                showMessage(AlertType.ERROR,
                    LanguageUtil.isEnglish() ? "Error" : "错误",
                    LanguageUtil.isEnglish() ? 
                        "Game file not found. Please download the game first" :
                        "游戏文件不存在，请先下载游戏"
                );
                return;
            }
            
            ProcessBuilder pb = new ProcessBuilder(gamePath.toString());
            pb.directory(new File(currentDir));
            pb.redirectErrorStream(true);
            
            System.out.println("Launching game: " + pb.command());
            
            Process p = pb.start();
            
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(p.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Game output: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            
        } catch (IOException e) {
            showMessage(AlertType.ERROR,
                LanguageUtil.isEnglish() ? "Launch Failed" : "启动失败",
                LanguageUtil.isEnglish() ? 
                    "Failed to launch game: " + e.getMessage() :
                    "启动游戏失败: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }
    
    private static void showMessage(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
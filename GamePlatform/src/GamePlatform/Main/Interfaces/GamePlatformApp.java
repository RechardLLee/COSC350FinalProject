package GamePlatform.Main.Interfaces;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import GamePlatform.Utility.LanguageUtil;
import java.net.URL;

public class GamePlatformApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 打印当前工作目录
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        
        // 尝试多个可能的路径
        URL loginFxml = null;
        String[] possiblePaths = {
            "GamePlatform/User/Management/LoginView.fxml",
            "/GamePlatform/User/Management/LoginView.fxml",
            "src/GamePlatform/User/Management/LoginView.fxml",
            "/src/GamePlatform/User/Management/LoginView.fxml"
        };
        
        for (String path : possiblePaths) {
            loginFxml = getClass().getClassLoader().getResource(path);
            System.out.println("Trying path: " + path + " -> " + loginFxml);
            if (loginFxml != null) break;
        }
        
        if (loginFxml == null) {
            throw new IllegalStateException("Cannot find LoginView.fxml. Please check the file location and classpath settings.");
        }
        
        FXMLLoader loader = new FXMLLoader(loginFxml);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle(LanguageUtil.isEnglish() ? "Login" : "登录");
        primaryStage.setScene(scene);
        
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 
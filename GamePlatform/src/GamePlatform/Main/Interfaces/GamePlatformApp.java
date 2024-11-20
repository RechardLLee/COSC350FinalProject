package GamePlatform.Main.Interfaces;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import GamePlatform.Utility.LanguageUtil;

public class GamePlatformApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle(LanguageUtil.isEnglish() ? "Login" : "登录");
        primaryStage.setScene(scene);
        
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.centerOnScreen();
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 
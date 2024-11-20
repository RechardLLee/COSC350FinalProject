import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GamePlatformApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle(LanguageUtil.isEnglish() ? "Login" : "登录");
        primaryStage.setScene(scene);
        
        // 设置最小窗口大小
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // 设置默认窗口大小
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        
        // 设置窗口居中
        primaryStage.centerOnScreen();
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 
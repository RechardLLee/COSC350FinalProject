package GamePlatform.Game.GoFish;

/** This is a prototype of Go Fish
 * @author Morgan Eckert
 * @version 11/3/2024
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import GamePlatform.User.Management.UserSession;

public class GoFish extends Application {

	private static final String GAME_NAME = "Go Fish";
	private String currentPlayer;
	
	@Override
	public void start(Stage stage) throws Exception {
		// 获取当前用户
		currentPlayer = UserSession.getCurrentUser();
		
		FXMLLoader load = new FXMLLoader(getClass().getResource("GoFish.fxml"));
		stage.setScene(new Scene(load.load()));
		stage.setTitle("Go Fish - Player: " + currentPlayer);
		stage.show();
		
		// 获取controller并设置当前玩家
		GoFishController controller = load.getController();
		controller.setCurrentPlayer(currentPlayer);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
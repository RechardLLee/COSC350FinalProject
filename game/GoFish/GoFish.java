/** This is a prototype of Go Fish
 * @author Morgan Eckert
 * @version 11/3/2024
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GoFish extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader load = new FXMLLoader(getClass().getResource("GoFish.fxml"));
		stage.setScene(new Scene(load.load()));
		stage.setTitle("Go Fish");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
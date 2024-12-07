package GamePlatform.Game.bingo;

// Main application class that loads and displays the GUI
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


//To open Bingo.fmxl
public class BingoRunner extends Application{

	@Override
	public void start(Stage stage) throws Exception {
	   Parent root = FXMLLoader.load(getClass().getResource("/GamePlatform/Game/bingo/Bingo.fxml"));

	   Scene scene = new Scene(root); // attach scene graph to scene
	   stage.setTitle("Bingo"); // Change the Title Bar
	   stage.setScene(scene); // attach scene to stage
	   stage.show(); // display the stage
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}
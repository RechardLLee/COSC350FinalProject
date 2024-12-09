// Main application class that loads and displays the GUI
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


//To open Bingo.fmxl
public class SinglePlayerBingoRunner extends Application{

	@Override
	public void start(Stage stage) throws Exception {
	   Parent root = 
	      // FXMLLoader.load(getClass().getResource("SinglePlayerBingo.fxml")); //Change the .fxml file
	      FXMLLoader.load(getClass().getResource("Bingo.fxml")); //Change the .fxml file

	   Scene scene = new Scene(root); // attach scene graph to scene
	   stage.setTitle("Bingo"); // Change the Title Bar
	   stage.setScene(scene); // attach scene to stage
	   stage.show(); // display the stage
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}
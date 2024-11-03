// Main application class that loads and displays the GUI
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


//To open Bingo.fmxl
public class testRunner extends Application{

	@Override
	public void start(Stage stage) throws Exception {
	   Parent root = 
	      FXMLLoader.load(getClass().getResource("test.fxml")); //Change the .fxml file

	   Scene scene = new Scene(root); // attach scene graph to scene
	   stage.setTitle("Test"); // Change the Title Bar
	   stage.setScene(scene); // attach scene to stage
	   stage.show(); // display the stage
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}
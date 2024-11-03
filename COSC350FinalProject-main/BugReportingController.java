/**
 * Sample Skeleton for 'reportBug.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.collections.FXCollections;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BugReportingController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="submitAction"
    private Button submitAction; // Value injected by FXMLLoader

    @FXML // fx:id="bugChoiceBox"
    private ChoiceBox<String> bugChoiceBox;
     // = new ChoiceBox(FXCollections.observableArrayList("First", "Second")); // Value injected by FXMLLoader

    // bugChoiceBox.setItems(FXCollections.observableArrayList("first", "Second"));
    @FXML // fx:id="bugDescriptionTextArea"
    private TextArea bugDescriptionTextArea; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        bugChoiceBox.setItems(FXCollections.observableArrayList("Gameplay", "Account Access", "Home Page Function", "Other"));

        assert bugChoiceBox != null : "fx:id=\"bugChoiceBox\" was not injected: check your FXML file 'reportBug.fxml'.";
        assert bugDescriptionTextArea != null : "fx:id=\"bugDescriptionTextArea\" was not injected: check your FXML file 'reportBug.fxml'.";
        assert submitAction != null : "fx:id=\"submitAction\" was not injected: check your FXML file 'reportBug.fxml'.";
        
    }
}
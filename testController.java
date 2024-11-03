/**
 * Sample Skeleton for 'test.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.math.BigDecimal;



public class TestController {

    private String userName;

    private BigDecimal balance;

    

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="usernameTextField"
    private TextField usernameTextField; // Value injected by FXMLLoader

    @FXML // fx:id="passwordField"
    private PasswordField passwordField; // Value injected by FXMLLoader

    @FXML // fx:id="createAccountButton"
    private Button createAccountButton; // Value injected by FXMLLoader

    @FXML // fx:id="reportBugMenu"
    private MenuItem reportBugMenu; // Value injected by FXMLLoader

    @FXML // fx:id="usernameDisplayTextField"
    private TextField usernameDisplayTextField; // Value injected by FXMLLoader

    @FXML // fx:id="signInButton"
    private Button signInButton; // Value injected by FXMLLoader

    @FXML // fx:id="reviewMenu"
    private MenuItem reviewMenu; // Value injected by FXMLLoader

    @FXML // fx:id="bingoPlayButton"
    private Button bingoPlayButton; // Value injected by FXMLLoader

    @FXML // fx:id="developerLoginButton"
    private Button developerLoginButton; // Value injected by FXMLLoader

    @FXML
    void bingoPlayAction(ActionEvent event) {
        // try {
        //    Stage stage = new Stage();
        //    Parent root = 
        //      FXMLLoader.load(getClass().getResource("Bingo/Bingo.fxml")); //Change the .fxml file

        //   Scene scene = new Scene(root); // attach scene graph to scene
        //   stage.setTitle(""); // Change the Title Bar
        //   stage.setScene(scene); // attach scene to stage
        //   stage.show(); // display the stage;
        // }
        // catch(IOException e) {
        //   e.printStackTrace();
        // }
    }

    @FXML
    void createAccountAction(ActionEvent event) {
        int account = 1;
    }

    @FXML
    void reportBugAction(ActionEvent event) {
        // String[] test = {};
        // Stage stage = new Stage();
        // BugReportRunner runner = new BugReportRunner();
        // runner.main(test);
        // usernameTextField.setText("this ran");

        try {
           Stage stage = new Stage();
           Parent root = 
             FXMLLoader.load(getClass().getResource("reportBug.fxml")); //Change the .fxml file

          Scene scene = new Scene(root); // attach scene graph to scene
          stage.setTitle(""); // Change the Title Bar
          stage.setScene(scene); // attach scene to stage
          stage.show(); // display the stage;
        }
        catch(IOException e) {
          e.printStackTrace();
        }

    }

     @FXML
    void signInAction(ActionEvent event) {

        String text = passwordField.getText();
        usernameDisplayTextField.setText(text);
    }

    @FXML
    void reviewAction(ActionEvent event) {
        try {
           Stage stage = new Stage();
           Parent root = 
             FXMLLoader.load(getClass().getResource("Review.fxml")); //Change the .fxml file

          Scene scene = new Scene(root); // attach scene graph to scene
          stage.setTitle(""); // Change the Title Bar
          stage.setScene(scene); // attach scene to stage
          stage.show(); // display the stage;
        }
        catch(IOException e) {
          e.printStackTrace();
        }
    }

    @FXML
    void developerLoginAction(ActionEvent event) {
        try {
           Stage stage = new Stage();
           Parent root = 
             FXMLLoader.load(getClass().getResource("developerLogin.fxml")); //Change the .fxml file

          Scene scene = new Scene(root); // attach scene graph to scene
          stage.setTitle(""); // Change the Title Bar
          stage.setScene(scene); // attach scene to stage
          stage.show(); // display the stage;
        }
        catch(IOException e) {
          e.printStackTrace();
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert usernameTextField != null : "fx:id=\"usernameTextField\" was not injected: check your FXML file 'test.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'test.fxml'.";
        assert createAccountButton != null : "fx:id=\"createAccountButton\" was not injected: check your FXML file 'test.fxml'.";
        assert signInButton != null : "fx:id=\"signInButton\" was not injected: check your FXML file 'test.fxml'.";
        assert reportBugMenu != null : "fx:id=\"reportBugMenu\" was not injected: check your FXML file 'test.fxml'.";
        assert reviewMenu != null : "fx:id=\"reviewMenu\" was not injected: check your FXML file 'test.fxml'.";
        assert usernameDisplayTextField != null : "fx:id=\"usernameDisplayTextField\" was not injected: check your FXML file 'test.fxml'.";
        assert developerLoginButton != null : "fx:id=\"developerLoginButton\" was not injected: check your FXML file 'test.fxml'.";
        assert bingoPlayButton != null : "fx:id=\"bingoPlayButton\" was not injected: check your FXML file 'test.fxml'.";

    }
}

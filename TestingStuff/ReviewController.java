/**
 * Sample Skeleton for 'Review.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class ReviewController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="ratingSlider"
    private Slider ratingSlider; // Value injected by FXMLLoader

    @FXML // fx:id="reviewTextField"
    private TextArea reviewTextField; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML
    void submitAction(ActionEvent event) {
        double i = ratingSlider.getValue();
        reviewTextField.setText(String.valueOf(i));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert ratingSlider != null : "fx:id=\"ratingSlider\" was not injected: check your FXML file 'Review.fxml'.";
        assert reviewTextField != null : "fx:id=\"reviewTextField\" was not injected: check your FXML file 'Review.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'Review.fxml'.";

    }
}

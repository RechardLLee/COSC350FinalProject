/**
 * Sample Skeleton for 'Bingo.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.animation.PauseTransition;
import java.time.Duration;

import java.util.*;
import java.io.*;



public class BingoFXController {

    StandardBingo bingo = new StandardBingo();

    //for displaying patterns
    private int patternIndex;

    //for round ordering
    private Integer roundIndex;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="callButton"
    private Button callButton; // Value injected by FXMLLoader

    @FXML // fx:id="callTextField"
    private TextField callTextField; // Value injected by FXMLLoader


    @FXML // fx:id="refreshScoresButton"
    private Button refreshScoresButton; // Value injected by FXMLLoader

    @FXML // fx:id="playerNameTextField"
    private TextField playerNameTextField; // Value injected by FXMLLoader

    @FXML // fx:id="pointsTextField"
    private TextField pointsTextField; // Value injected by FXMLLoader

    @FXML // fx:id="BTextArea"
    private TextArea BTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="ITextArea"
    private TextArea ITextArea; // Value injected by FXMLLoader

    @FXML // fx:id="NTextArea"
    private TextArea NTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="GTextArea"
    private TextArea GTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="OTextArea"
    private TextArea OTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="scoresTextArea"
    private TextArea scoresTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="scoreButton"
    private Button scoreButton; // Value injected by FXMLLoader

    @FXML // fx:id="nextRoundButton"
    private Button nextRoundButton; // Value injected by FXMLLoader

    @FXML // fx:id="nextRoundTextField"
    private TextField nextRoundTextField; // Value injected by FXMLLoader

    @FXML // fx:id="bingoPatternTextArea"
    private TextArea bingoPatternTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="gameTypeTextArea"
    private TextField gameTypeTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="bingoPatternTextField"
    private TextField bingoPatternTextField; // Value injected by FXMLLoader

    @FXML // fx:id="showPatternButton"
    private Button showPatternButton; // Value injected by FXMLLoader

    @FXML // fx:id="loadScoresButton"
    private Button loadScoresButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="helpButton"
    private Button helpButton; // Value injected by FXMLLoader

    @FXML // display pattern of winning bingo
    void showPatternAction(ActionEvent event) {
        ArrayList<String[]> list = bingo.readPattern(gameTypeTextArea.getText().toString());
        bingoPatternTextField.setText(list.get(patternIndex)[0]);
        patternIndex++;
        bingoPatternTextArea.setText(bingo.makePattern(list.get(patternIndex)));
        patternIndex++;
        if(patternIndex == list.size()){
            patternIndex = 0;
        }
    }


    @FXML //call a number and update called numbers
    void callAction(ActionEvent event) {
        callTextField.setText(bingo.call());
        BTextArea.setText(bingo.printB());
        ITextArea.setText(bingo.printI());
        NTextArea.setText(bingo.printN());
        GTextArea.setText(bingo.printG());
        OTextArea.setText(bingo.printO());
    }

    @FXML //display help text
    void helpAction(ActionEvent event) {
        scoresTextArea.setText(bingo.displayHelp());
    }

    @FXML //load scores from previous game
    void loadScoresAction(ActionEvent event)throws IOException, ClassNotFoundException{
        bingo.readScores();
        scoresTextArea.setText(bingo.printScores());
    }
    @FXML //go to next round
    void nextRoundAction(ActionEvent event) {
        if(roundIndex == 1){
            gameTypeTextArea.setText(nextRoundTextField.getText());
            nextRoundTextField.setText(bingo.pickThirdRound());
            roundIndex++;
        }
        else if(roundIndex == 2){
            gameTypeTextArea.setText(nextRoundTextField.getText());
            nextRoundTextField.setText("Game Over");
            roundIndex++;
        }
        else if(roundIndex == 3){
            gameTypeTextArea.setText(bingo.pickFirstRound());
            nextRoundTextField.setText(bingo.pickSecondRound());
            roundIndex = 1;
            bingo.newGame();
            BTextArea.setText(bingo.printB());
            ITextArea.setText(bingo.printI());
            NTextArea.setText(bingo.printN());
            GTextArea.setText(bingo.printG());
            OTextArea.setText(bingo.printO());
        }
        pointsTextField.setText(roundIndex.toString());
        patternIndex = 0;
        ArrayList<String[]> list = bingo.readPattern(gameTypeTextArea.getText().toString());
        bingoPatternTextField.setText(list.get(patternIndex)[0]);
        patternIndex++;
        bingoPatternTextArea.setText(bingo.makePattern(list.get(patternIndex)));
        patternIndex++;
        if(patternIndex == list.size()){
            patternIndex = 0;
        }
    }

    @FXML //update the scores and save them to file
    void refreshScoresAction(ActionEvent event) throws IOException, ClassNotFoundException{
        bingo.refreshScores();
        scoresTextArea.setText(bingo.printScores());
        playerNameTextField.setText("");
        bingo.saveScores();
    }

    @FXML //score for given player and points
    void scoreAction(ActionEvent event) {
        bingo.score(playerNameTextField.getText(), Integer.parseInt(pointsTextField.getText()));
        playerNameTextField.clear();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        roundIndex = 1;
        gameTypeTextArea.setText(bingo.pickFirstRound());
        nextRoundTextField.setText(bingo.pickSecondRound());
        ArrayList<String[]> list = bingo.readPattern(gameTypeTextArea.getText().toString());
        bingoPatternTextField.setText(list.get(patternIndex)[0]);
        patternIndex++;
        bingoPatternTextArea.setText(bingo.makePattern(list.get(patternIndex)));
        patternIndex++;
        assert callButton != null : "fx:id=\"callButton\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert callTextField != null : "fx:id=\"callTextField\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert refreshScoresButton != null : "fx:id=\"refreshScoresButton\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert playerNameTextField != null : "fx:id=\"playerNameTextField\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert pointsTextField != null : "fx:id=\"pointsTextField\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert BTextArea != null : "fx:id=\"BTextArea\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert ITextArea != null : "fx:id=\"ITextArea\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert NTextArea != null : "fx:id=\"NTextArea\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert GTextArea != null : "fx:id=\"GTextArea\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert OTextArea != null : "fx:id=\"OTextArea\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert scoresTextArea != null : "fx:id=\"scoresTextArea\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert scoreButton != null : "fx:id=\"scoreButton\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert nextRoundButton != null : "fx:id=\"nextRoundButton\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert nextRoundTextField != null : "fx:id=\"nextRoundTextField\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert bingoPatternTextArea != null : "fx:id=\"bingoPatternTextArea\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert gameTypeTextArea != null : "fx:id=\"gameTypeTextArea\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert bingoPatternTextField != null : "fx:id=\"bingoPatternTextField\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert showPatternButton != null : "fx:id=\"showPatternButton\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert loadScoresButton != null : "fx:id=\"loadScoresButton\" was not injected: check your FXML file 'Bingo.fxml'.";
        assert helpButton != null : "fx:id=\"helpButton\" was not injected: check your FXML file 'Bingo.fxml'.";
    }
}

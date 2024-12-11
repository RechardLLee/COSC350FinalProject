/**
 * Sample Skeleton for 'CopyBingo.fxml' Controller Class
 */
package GamePlatform.Game.bingo;

import java.util.*;
import java.io.*;
import javafx.scene.paint.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

// 添加必要的导入
import GamePlatform.Database.DatabaseService;
import GamePlatform.Main.Interfaces.MainController;
import GamePlatform.User.Management.UserSession;
import GamePlatform.Game.GameStats;
import javafx.application.Platform;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 添加文件操作相关的导入
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class BingoController{


    StandardBingo bingo = new StandardBingo();

    //for displaying patterns
    private int patternIndex;

    //for round ordering
    private Integer roundIndex;

    //to hold player card
    private Text[] playerCard;

    private Circle[] playerCircles;

    private Rectangle[] c1Rectangles;

    private Rectangle[] c2Rectangles;

    private Rectangle[] c3Rectangles;

    private Rectangle[] c4Rectangles;

    private Rectangle[] c5Rectangles;

    private Rectangle[] c6Rectangles;

    private Rectangle[] c7Rectangles;

    private Rectangle[] c8Rectangles;

    private Card card;

    private Card c1Card;

    private Card c2Card;

    private Card c3Card;

    private Card c4Card;

    private Card c5Card;

    private Card c6Card;

    private Card c7Card;

    private Card c8Card;

    private Card[] cards;

    private Card[] cpuCards;

    private Circle[] displayCard;

    private String patternType;

    // private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    // private Runnable task = () -> showPatternAction();

    ScheduledExecutorService scheduler =   Executors.newSingleThreadScheduledExecutor();

    Runnable task = () -> showPatternAction();  

    ScheduledFuture<?> executor;  

    private Rectangle[][] cpuRectangles;


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="B1Circle"
    private Circle B1Circle; // Value injected by FXMLLoader

    @FXML // fx:id="B2Circle"
    private Circle B2Circle; // Value injected by FXMLLoader

    @FXML // fx:id="B3Circle"
    private Circle B3Circle; // Value injected by FXMLLoader

    @FXML // fx:id="B4Circle"
    private Circle B4Circle; // Value injected by FXMLLoader

    @FXML // fx:id="B5Circle"
    private Circle B5Circle; // Value injected by FXMLLoader

    @FXML // fx:id="I1Circle"
    private Circle I1Circle; // Value injected by FXMLLoader

    @FXML // fx:id="I2Circle"
    private Circle I2Circle; // Value injected by FXMLLoader

    @FXML // fx:id="I3Circle"
    private Circle I3Circle; // Value injected by FXMLLoader

    @FXML // fx:id="I4Circle"
    private Circle I4Circle; // Value injected by FXMLLoader

    @FXML // fx:id="I5Circle"
    private Circle I5Circle; // Value injected by FXMLLoader

    @FXML // fx:id="N1Circle"
    private Circle N1Circle; // Value injected by FXMLLoader

    @FXML // fx:id="N2Circle"
    private Circle N2Circle; // Value injected by FXMLLoader

    @FXML // fx:id="N3Circle"
    private Circle N3Circle; // Value injected by FXMLLoader

    @FXML // fx:id="N4Circle"
    private Circle N4Circle; // Value injected by FXMLLoader

    @FXML // fx:id="N5Circle"
    private Circle N5Circle; // Value injected by FXMLLoader

    @FXML // fx:id="G1Circle"
    private Circle G1Circle; // Value injected by FXMLLoader

    @FXML // fx:id="G2Circle"
    private Circle G2Circle; // Value injected by FXMLLoader

    @FXML // fx:id="G3Circle"
    private Circle G3Circle; // Value injected by FXMLLoader

    @FXML // fx:id="G4Circle"
    private Circle G4Circle; // Value injected by FXMLLoader

    @FXML // fx:id="G5Circle"
    private Circle G5Circle; // Value injected by FXMLLoader

    @FXML // fx:id="O1Circle"
    private Circle O1Circle; // Value injected by FXMLLoader

    @FXML // fx:id="O2Circle"
    private Circle O2Circle; // Value injected by FXMLLoader

    @FXML // fx:id="O3Circle"
    private Circle O3Circle; // Value injected by FXMLLoader

    @FXML // fx:id="O4Circle"
    private Circle O4Circle; // Value injected by FXMLLoader

    @FXML // fx:id="O5Circle"
    private Circle O5Circle; // Value injected by FXMLLoader

    @FXML // fx:id="B1Text"
    private Text B1Text; // Value injected by FXMLLoader

    @FXML // fx:id="B2Text"
    private Text B2Text; // Value injected by FXMLLoader

    @FXML // fx:id="B3Text"
    private Text B3Text; // Value injected by FXMLLoader

    @FXML // fx:id="B4Text"
    private Text B4Text; // Value injected by FXMLLoader

    @FXML // fx:id="B5Text"
    private Text B5Text; // Value injected by FXMLLoader

    @FXML // fx:id="I1Text"
    private Text I1Text; // Value injected by FXMLLoader

    @FXML // fx:id="I2Text"
    private Text I2Text; // Value injected by FXMLLoader

    @FXML // fx:id="I3Text"
    private Text I3Text; // Value injected by FXMLLoader

    @FXML // fx:id="I4Text"
    private Text I4Text; // Value injected by FXMLLoader

    @FXML // fx:id="I5Text"
    private Text I5Text; // Value injected by FXMLLoader

    @FXML // fx:id="N1Text"
    private Text N1Text; // Value injected by FXMLLoader

    @FXML // fx:id="N2Text"
    private Text N2Text; // Value injected by FXMLLoader

    @FXML // fx:id="N3Text"
    private Text N3Text; // Value injected by FXMLLoader

    @FXML // fx:id="N4Text"
    private Text N4Text; // Value injected by FXMLLoader

    @FXML // fx:id="N5Text"
    private Text N5Text; // Value injected by FXMLLoader

    @FXML // fx:id="G1Text"
    private Text G1Text; // Value injected by FXMLLoader

    @FXML // fx:id="G2Text"
    private Text G2Text; // Value injected by FXMLLoader

    @FXML // fx:id="G3Text"
    private Text G3Text; // Value injected by FXMLLoader

    @FXML // fx:id="G4Text"
    private Text G4Text; // Value injected by FXMLLoader

    @FXML // fx:id="G5Text"
    private Text G5Text; // Value injected by FXMLLoader

    @FXML // fx:id="O1Text"
    private Text O1Text; // Value injected by FXMLLoader

    @FXML // fx:id="O2Text"
    private Text O2Text; // Value injected by FXMLLoader

    @FXML // fx:id="O3Text"
    private Text O3Text; // Value injected by FXMLLoader

    @FXML // fx:id="O4Text"
    private Text O4Text; // Value injected by FXMLLoader

    @FXML // fx:id="O5Text"
    private Text O5Text; // Value injected by FXMLLoader

      @FXML
    private Circle displayB1Circle;

    @FXML
    private Circle displayB2Circle;

    @FXML
    private Circle displayB3Circle;

    @FXML
    private Circle displayB4Circle;

    @FXML
    private Circle displayB5Circle;

    @FXML
    private Circle displayI1Circle;

    @FXML
    private Circle displayI2Circle;

    @FXML
    private Circle displayI3Circle;

    @FXML
    private Circle displayI4Circle;

    @FXML
    private Circle displayI5Circle;

    @FXML
    private Circle displayN1Circle;

    @FXML
    private Circle displayN2Circle;

    @FXML
    private Circle displayN3Circle;

    @FXML
    private Circle displayN4Circle;

    @FXML
    private Circle displayN5Circle;

    @FXML
    private Circle displayG1Circle;

    @FXML
    private Circle displayG2Circle;

    @FXML
    private Circle displayG3Circle;

    @FXML
    private Circle displayG4Circle;

    @FXML
    private Circle displayG5Circle;

    @FXML
    private Circle displayO1Circle;

    @FXML
    private Circle displayO2Circle;

    @FXML
    private Circle displayO3Circle;

    @FXML
    private Circle displayO4Circle;

    @FXML
    private Circle displayO5Circle;

    @FXML
    private Rectangle C1B1Rect;

    @FXML
    private Rectangle C1B2Rect;

    @FXML
    private Rectangle C1B3Rect;

    @FXML
    private Rectangle C1B4Rect;

    @FXML
    private Rectangle C1B5Rect;

    @FXML
    private Rectangle C1I1Rect;

    @FXML
    private Rectangle C1I2Rect;

    @FXML
    private Rectangle C1I3Rect;

    @FXML
    private Rectangle C1I4Rect;

    @FXML
    private Rectangle C1I5Rect;

    @FXML
    private Rectangle C1N1Rect;

    @FXML
    private Rectangle C1N2Rect;

    @FXML
    private Rectangle C1N4Rect;

    @FXML
    private Rectangle C1N5Rect;

    @FXML
    private Rectangle C1G1Rect;

    @FXML
    private Rectangle C1G2Rect;

    @FXML
    private Rectangle C1G3Rect;

    @FXML
    private Rectangle C1G4Rect;

    @FXML
    private Rectangle C1G5Rect;

    @FXML
    private Rectangle C1O1Rect;

    @FXML
    private Rectangle C1O2Rect;

    @FXML
    private Rectangle C1O3Rect;

    @FXML
    private Rectangle C1O4Rect;

    @FXML
    private Rectangle C1O5Rect;

    @FXML // fx:id="C2B1Rect"
    private Rectangle C2B1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2B2Rect"
    private Rectangle C2B2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2B3Rect"
    private Rectangle C2B3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2B4Rect"
    private Rectangle C2B4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2B5Rect"
    private Rectangle C2B5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2I1Rect"
    private Rectangle C2I1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2I2Rect"
    private Rectangle C2I2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2I3Rect"
    private Rectangle C2I3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2I4Rect"
    private Rectangle C2I4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2I5Rect"
    private Rectangle C2I5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2N1Rect"
    private Rectangle C2N1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2N2Rect"
    private Rectangle C2N2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2N4Rect"
    private Rectangle C2N4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2N5Rect"
    private Rectangle C2N5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2G1Rect"
    private Rectangle C2G1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2G2Rect"
    private Rectangle C2G2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2G3Rect"
    private Rectangle C2G3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2G4Rect"
    private Rectangle C2G4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2G5Rect"
    private Rectangle C2G5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2O1Rect"
    private Rectangle C2O1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2O2Rect"
    private Rectangle C2O2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2O3Rect"
    private Rectangle C2O3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2O4Rect"
    private Rectangle C2O4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C2O5Rect"
    private Rectangle C2O5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3B1Rect"
    private Rectangle C3B1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3B2Rect"
    private Rectangle C3B2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3B3Rect"
    private Rectangle C3B3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3B4Rect"
    private Rectangle C3B4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3B5Rect"
    private Rectangle C3B5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3I1Rect"
    private Rectangle C3I1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3I2Rect"
    private Rectangle C3I2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3I3Rect"
    private Rectangle C3I3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3I4Rect"
    private Rectangle C3I4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3I5Rect"
    private Rectangle C3I5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3N1Rect"
    private Rectangle C3N1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3N2Rect"
    private Rectangle C3N2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3N3Rect"
    private Rectangle C3N3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3N4Rect"
    private Rectangle C3N4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3N5Rect"
    private Rectangle C3N5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3G1Rect"
    private Rectangle C3G1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3G2Rect"
    private Rectangle C3G2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3G3Rect"
    private Rectangle C3G3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3G4Rect"
    private Rectangle C3G4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3G5Rect"
    private Rectangle C3G5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3O1Rect"
    private Rectangle C3O1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3O2Rect"
    private Rectangle C3O2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3O3Rect"
    private Rectangle C3O3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3O4Rect"
    private Rectangle C3O4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C3O5Rect"
    private Rectangle C3O5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4B1Rect"
    private Rectangle C4B1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4B2Rect"
    private Rectangle C4B2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4B3Rect"
    private Rectangle C4B3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4B4Rect"
    private Rectangle C4B4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4B5Rect"
    private Rectangle C4B5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4I1Rect"
    private Rectangle C4I1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4I2Rect"
    private Rectangle C4I2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4I3Rect"
    private Rectangle C4I3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4I4Rect"
    private Rectangle C4I4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4I5Rect"
    private Rectangle C4I5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4N1Rect"
    private Rectangle C4N1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4N2Rect"
    private Rectangle C4N2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4N4Rect"
    private Rectangle C4N4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4N5Rect"
    private Rectangle C4N5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4G1Rect"
    private Rectangle C4G1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4G2Rect"
    private Rectangle C4G2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4G3Rect"
    private Rectangle C4G3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4G4Rect"
    private Rectangle C4G4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4G5Rect"
    private Rectangle C4G5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4O1Rect"
    private Rectangle C4O1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4O2Rect"
    private Rectangle C4O2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4O3Rect"
    private Rectangle C4O3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4O4Rect"
    private Rectangle C4O4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C4O5Rect"
    private Rectangle C4O5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5B1Rect"
    private Rectangle C5B1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5B2Rect"
    private Rectangle C5B2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5B3Rect"
    private Rectangle C5B3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5B4Rect"
    private Rectangle C5B4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5B5Rect"
    private Rectangle C5B5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5I1Rect"
    private Rectangle C5I1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5I2Rect"
    private Rectangle C5I2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5I3Rect"
    private Rectangle C5I3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5I4Rect"
    private Rectangle C5I4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5I5Rect"
    private Rectangle C5I5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5N1Rect"
    private Rectangle C5N1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5N2Rect"
    private Rectangle C5N2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5N4Rect"
    private Rectangle C5N4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5N4Rect"
    private Rectangle C5N5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5G1Rect"
    private Rectangle C5G1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5G2Rect"
    private Rectangle C5G2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5G3Rect"
    private Rectangle C5G3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5G4Rect"
    private Rectangle C5G4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5G5Rect"
    private Rectangle C5G5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5O1Rect"
    private Rectangle C5O1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5O2Rect"
    private Rectangle C5O2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5O3Rect"
    private Rectangle C5O3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5O4Rect"
    private Rectangle C5O4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C5O5Rect"
    private Rectangle C5O5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6B1Rect"
    private Rectangle C6B1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6B2Rect"
    private Rectangle C6B2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6B3Rect"
    private Rectangle C6B3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6B4Rect"
    private Rectangle C6B4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6B5Rect"
    private Rectangle C6B5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6I1Rect"
    private Rectangle C6I1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6I2Rect"
    private Rectangle C6I2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6I3Rect"
    private Rectangle C6I3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6I4Rect"
    private Rectangle C6I4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6I5Rect"
    private Rectangle C6I5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6N1Rect"
    private Rectangle C6N1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6N2Rect"
    private Rectangle C6N2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6N4Rect"
    private Rectangle C6N4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6N5Rect"
    private Rectangle C6N5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6G1Rect"
    private Rectangle C6G1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6G2Rect"
    private Rectangle C6G2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6G3Rect"
    private Rectangle C6G3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6G4Rect"
    private Rectangle C6G4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6G5Rect"
    private Rectangle C6G5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6O1Rect"
    private Rectangle C6O1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6O2Rect"
    private Rectangle C6O2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6O3Rect"
    private Rectangle C6O3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6O4Rect"
    private Rectangle C6O4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C6O5Rect"
    private Rectangle C6O5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7B1Rect"
    private Rectangle C7B1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7B2Rect"
    private Rectangle C7B2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7B3Rect"
    private Rectangle C7B3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7B4Rect"
    private Rectangle C7B4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7B5Rect"
    private Rectangle C7B5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7I1Rect"
    private Rectangle C7I1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7I2Rect"
    private Rectangle C7I2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7I3Rect"
    private Rectangle C7I3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7I4Rect"
    private Rectangle C7I4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7I5Rect"
    private Rectangle C7I5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7N1Rect"
    private Rectangle C7N1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7N2Rect"
    private Rectangle C7N2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7N4Rect"
    private Rectangle C7N4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7N5Rect"
    private Rectangle C7N5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7G1Rect"
    private Rectangle C7G1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7G2Rect"
    private Rectangle C7G2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7G3Rect"
    private Rectangle C7G3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7G4Rect"
    private Rectangle C7G4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7G5Rect"
    private Rectangle C7G5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7O1Rect"
    private Rectangle C7O1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7O2Rect"
    private Rectangle C7O2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7O3Rect"
    private Rectangle C7O3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7O4Rect"
    private Rectangle C7O4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C7O5Rect"
    private Rectangle C7O5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8B1Rect"
    private Rectangle C8B1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8B2Rect"
    private Rectangle C8B2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8B3Rect"
    private Rectangle C8B3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8B4Rect"
    private Rectangle C8B4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8B5Rect"
    private Rectangle C8B5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8I1Rect"
    private Rectangle C8I1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8I2Rect"
    private Rectangle C8I2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8I3Rect"
    private Rectangle C8I3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8I4Rect"
    private Rectangle C8I4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8I5Rect"
    private Rectangle C8I5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8N1Rect"
    private Rectangle C8N1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8N2Rect"
    private Rectangle C8N2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8N4Rect"
    private Rectangle C8N4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8N5Rect"
    private Rectangle C8N5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8G1Rect"
    private Rectangle C8G1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8G2Rect"
    private Rectangle C8G2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8G3Rect"
    private Rectangle C8G3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8G4Rect"
    private Rectangle C8G4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8G5Rect"
    private Rectangle C8G5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8O1Rect"
    private Rectangle C8O1Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8O2Rect"
    private Rectangle C8O2Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8O3Rect"
    private Rectangle C8O3Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8O4Rect"
    private Rectangle C8O4Rect; // Value injected by FXMLLoader

    @FXML // fx:id="C8O5Rect"
    private Rectangle C8O5Rect; // Value injected by FXMLLoader

    @FXML // fx:id="nextRoundTextField"
    private TextField nextRoundTextField; // Value injected by FXMLLoader

    @FXML // fx:id="winningsTextField"
    private TextField winningsTextField; // Value injected by FXMLLoader

    @FXML // fx:id="gameTypeTextArea"
    private TextArea gameTypeTextArea; // Value injected by FXMLLoader

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

    @FXML // fx:id="callButton"
    private Button callButton; // Value injected by FXMLLoader

    @FXML // fx:id="callTextField"
    private TextField callTextField; // Value injected by FXMLLoader

    @FXML // fx:id="bingoButton"
    private Button bingoButton; // Value injected by FXMLLoader

    @FXML // fx:id="showPatternButton"
    private Button showPatternButton; // Value injected by FXMLLoader

    @FXML // fx:id="balanceTextField"
    private TextField balanceTextField; // Value injected by FXMLLoader

    @FXML // fx:id="betTextField"
    private TextField betTextField; // Value injected by FXMLLoader

    @FXML // fx:id="plusButton"
    private Button plusButton; // Value injected by FXMLLoader

    @FXML // fx:id="minusButton"
    private Button minusButton; // Value injected by FXMLLoader

    // 添加用户相关字段
    private String username = UserSession.getInstance().getUsername();
    private int totalBet = 0;    // 总下注金额
    private int totalWin = 0;    // 总赢得金额
    private int netProfit = 0;   // 净盈亏
    
    private void startDisplay(){
        // ScheduledFuture<?>
        executor = scheduler.scheduleAtFixedRate(task,0, 1, SECONDS);
    }

    private void stopDisplay(){
        Runnable canceller = () -> {
            executor.cancel(true);
        };
        scheduler.schedule(canceller, 0, SECONDS);

    }


    private void setCards(Text[] text){
        card = new Card();
        c1Card = new Card();
        c2Card = new Card();
        c3Card = new Card();
        c4Card = new Card();
        c5Card = new Card();
        c6Card = new Card();
        c7Card = new Card();
        c8Card = new Card();
        for(int i = 0; i < text.length; i++){
            text[i].setText((card.getCard().get(i)).toString());
        }
        cpuCards = new Card[]{c1Card, c2Card, c3Card, c4Card, c5Card, c6Card, c7Card, c8Card};
    }

    private void markCards(int call){
        card.markCall(call);
        c1Card.markCall(call);
        c2Card.markCall(call);
        c3Card.markCall(call);
        c4Card.markCall(call);
        c5Card.markCall(call);
        c6Card.markCall(call);
        c7Card.markCall(call);
        c8Card.markCall(call);
    }

    private boolean checkCPUBingo(){
        return (c1Card.checkBingo(patternType) || c2Card.checkBingo(patternType) || c3Card.checkBingo(patternType) ||
            c4Card.checkBingo(patternType) || c5Card.checkBingo(patternType) || c6Card.checkBingo(patternType) ||
            c7Card.checkBingo(patternType) || c8Card.checkBingo(patternType));
    }

    private void cpuMarkCall(Rectangle[] c, int i){
        c[i].setFill(Color.BLUE);
    }

    private void cpuMarkCalls(int c){
        for(int i = 0; i < cpuCards.length; i++){
            if(cpuCards[i].check(c)){
                cpuMarkCall(cpuRectangles[i], cpuCards[i].getIndexOfCall(c));
            }
        }
        // for(Rectangle[] c : cpuCards){
        //     cpuMarkCall(c, call);
        // }
    }

    private void rewardBingo(){
        balanceTextField.setText(Integer.toString(Integer.valueOf(betTextField.getText()) * 3 * roundIndex));
        winningsTextField.setText(Integer.toString(Integer.valueOf(winningsTextField.getText()) + Integer.valueOf(betTextField.getText())));
    }
    // private Boolean checkBingo();

    private void setCircles(){
        for(Circle circle : playerCircles){
            circle.setFill(Color.WHITE);
        }
    }

    private void setRectangles(){
        for(Rectangle[] rectlist : cpuRectangles){
            for(Rectangle rect : rectlist){
                rect.setFill(Color.WHITE);
            }
        }
    }

    private void displayPattern(boolean[] patternCard){
        for(Circle c : displayCard){
            c.setFill(Color.WHITE);
        }
        for(int i = 0; i < patternCard.length; i++){
            if(patternCard[i]){
                displayCard[i].setFill(Color.BLUE);
                // if(patternType.equals())
            }
        }
    }

    @FXML
    void B1Action(MouseEvent event) {
        B1Circle.setFill(Color.BLUE);
    }

    @FXML
    void B2Action(MouseEvent event) {
        B2Circle.setFill(Color.BLUE);
    }

    @FXML
    void B3Action(MouseEvent event) {
        B3Circle.setFill(Color.BLUE);
    }

    @FXML
    void B4Action(MouseEvent event) {
        B4Circle.setFill(Color.BLUE);
    }

    @FXML
    void B5Action(MouseEvent event) {
        B5Circle.setFill(Color.BLUE);
    }

    @FXML
    void G1Action(MouseEvent event) {
        G1Circle.setFill(Color.BLUE);
    }

    @FXML
    void G2Action(MouseEvent event) {
        G2Circle.setFill(Color.BLUE);
    }

    @FXML
    void G3Action(MouseEvent event) {
        G3Circle.setFill(Color.BLUE);
    }

    @FXML
    void G4Action(MouseEvent event) {
        G4Circle.setFill(Color.BLUE);
    }

    @FXML
    void G5Action(MouseEvent event) {
        G5Circle.setFill(Color.BLUE);
    }

    @FXML
    void I1Action(MouseEvent event) {
        I1Circle.setFill(Color.BLUE);
    }

    @FXML
    void I2Action(MouseEvent event) {
        I2Circle.setFill(Color.BLUE);
    }

    @FXML
    void I3Action(MouseEvent event) {
        I3Circle.setFill(Color.BLUE);
    }

    @FXML
    void I4Action(MouseEvent event) {
        I4Circle.setFill(Color.BLUE);
    }

    @FXML
    void I5Action(MouseEvent event) {
        I5Circle.setFill(Color.BLUE);
    }

    @FXML
    void N1Action(MouseEvent event) {
        N1Circle.setFill(Color.BLUE);
    }

    @FXML
    void N2Action(MouseEvent event) {
        N2Circle.setFill(Color.BLUE);
    }

    @FXML
    void N3Action(MouseEvent event) {
        N3Circle.setFill(Color.BLUE);
    }

    @FXML
    void N4Action(MouseEvent event) {
        N4Circle.setFill(Color.BLUE);
    }

    @FXML
    void N5Action(MouseEvent event) {
        N5Circle.setFill(Color.BLUE);
    }

    @FXML
    void O1Action(MouseEvent event) {
        O1Circle.setFill(Color.BLUE);
    }

    @FXML
    void O2Action(MouseEvent event) {
        O2Circle.setFill(Color.BLUE);
    }

    @FXML
    void O3Action(MouseEvent event) {
        O3Circle.setFill(Color.BLUE);
    }

    @FXML
    void O4Action(MouseEvent event) {
        O4Circle.setFill(Color.BLUE);
    }

    @FXML
    void O5Action(MouseEvent event) {
        O5Circle.setFill(Color.BLUE);
    }

    void nextRoundAction() {
        if(roundIndex == 1){
            stopDisplay();
            gameTypeTextArea.setText(nextRoundTextField.getText());
            patternType = gameTypeTextArea.getText();
            nextRoundTextField.setText(bingo.pickThirdRound());
            roundIndex++;
            startDisplay();
        }
        else if(roundIndex == 2){
            stopDisplay();
            gameTypeTextArea.setText(nextRoundTextField.getText());
            patternType = gameTypeTextArea.getText();
            nextRoundTextField.setText("Game Over");
            roundIndex++;
            startDisplay();
        }
        else if(roundIndex == 3){
            stopDisplay();
            gameTypeTextArea.setText(bingo.pickFirstRound());
            patternType = gameTypeTextArea.getText();
            nextRoundTextField.setText(bingo.pickSecondRound());
            roundIndex = 1;
            bingo.newGame();
            BTextArea.setText(bingo.printB());
            ITextArea.setText(bingo.printI());
            NTextArea.setText(bingo.printN());
            GTextArea.setText(bingo.printG());
            OTextArea.setText(bingo.printO());
            setCards(playerCard);
            setCircles();
            setRectangles();
            card.clearMarked();
            startDisplay();
        }
        // pointsTextField.setText(roundIndex.toString());
        // patternIndex = 0;
        // // ArrayList<String[]> list = bingo.readPattern(gameTypeTextArea.getText().toString());
        // // bingoPatternTextField.setText(list.get(patternIndex)[0]);
        // patternIndex++;
        // // bingoPatternTextArea.setText(bingo.makePattern(list.get(patternIndex)));
        // patternIndex++;
        // if(patternIndex == list.size()){
        //     patternIndex = 0;
        // }


        // ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        // Runnable task = () -> showPatternAction();


        // // Schedule the task to run every 5 seconds
        // executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }
    @FXML 
    void callAction(ActionEvent event) {
        callTextField.setText(bingo.call());
        markCards(bingo.getLastCall());
        cpuMarkCalls(bingo.getLastCall());
        updateCalledNumbers();
        
        boolean cpuBingo = checkCPUBingo();
        boolean playerBingo = card.checkBingo(patternType);
        
        if (cpuBingo || playerBingo) {
            int betAmount = Integer.parseInt(betTextField.getText());
            int currentBalance = DatabaseService.getUserMoney(username);
            
            String message;
            if (cpuBingo && playerBingo) {
                // 平局 - 返还一半赌注
                int returnAmount = betAmount / 2;
                DatabaseService.updateUserMoney(username, currentBalance - betAmount + returnAmount);
                netProfit = -betAmount + returnAmount;
                message = "It's a tie!\nYou get half your bet back.";
            } else if (cpuBingo) {
                // CPU赢 - 输掉全部赌���
                DatabaseService.updateUserMoney(username, currentBalance - betAmount);
                netProfit = -betAmount;
                message = "CPU got BINGO!\nYou lost your bet.";
            } else {
                // 玩家赢 - 获得3倍赌注
                int winAmount = betAmount * 3;
                DatabaseService.updateUserMoney(username, currentBalance + winAmount - betAmount);
                netProfit = winAmount - betAmount;
                message = "You got BINGO!\nYou won " + winAmount + " credits!";
            }
            
            winningsTextField.setText(Integer.toString(netProfit));
            
            if (netProfit != 0) {
                saveScore(netProfit);
            }
            
            updateBalance();
            
            // 替换 JOptionPane 为 JavaFX Alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("BINGO!");
            alert.setHeaderText(null);
            alert.setContentText(message + "\nProceed to next round?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                nextRoundAction();
            } else {
                // 如果选择不继续，关闭游戏
                Platform.runLater(() -> {
                    Stage stage = (Stage) callButton.getScene().getWindow();
                    stage.close();
                });
            }
        }
    }

    @FXML
    void bingoButtonAction(ActionEvent event) {
        if(card.checkBingo(patternType)) {
            int betAmount = Integer.parseInt(betTextField.getText());
            int winAmount = betAmount * 3 * roundIndex;
            int currentBalance = DatabaseService.getUserMoney(username);
            
            // 更新余额和收益
            currentBalance = currentBalance + winAmount - betAmount;
            DatabaseService.updateUserMoney(username, currentBalance);
            
            netProfit = winAmount - betAmount;
            winningsTextField.setText(Integer.toString(netProfit));
            
            if (netProfit != 0) {
                saveScore(netProfit);
            }
            
            updateBalance();
            
            // 显示获胜消息和确认对话框
            String message = "You got BINGO!\nYou won " + winAmount + " credits!";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("BINGO!");
            alert.setHeaderText(null);
            alert.setContentText(message + "\nProceed to next round?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                nextRoundAction();
            } else {
                // 如果选择不继续，关闭游戏
                Platform.runLater(() -> {
                    Stage stage = (Stage) callButton.getScene().getWindow();
                    stage.close();
                });
            }
        }
    }

    void showPatternAction() {
        boolean[][] patterns = card.getPattern(patternType);
        displayPattern(patterns[patternIndex]);
        patternIndex++;
        if(patternIndex == patterns.length){
            patternIndex = 0;
        }
    }

    @FXML
    void plusAction(ActionEvent event) {
        int currentBalance = DatabaseService.getUserMoney(username);
        int currentBet = Integer.parseInt(betTextField.getText());
        if(currentBet + 10 <= currentBalance) {
            betTextField.setText(Integer.toString(currentBet + 10));
        }
    }

    @FXML
    void minusAction(ActionEvent event) {
        int currentBet = Integer.parseInt(betTextField.getText());
        if(currentBet - 10 >= 10) { // 最小下注10
            betTextField.setText(Integer.toString(currentBet - 10));
        }
    }

    // 修改 initialize 方法，添加游戏状态控制
    private boolean gameInProgress = false;

    @FXML
    void initialize() {
        try {
            roundIndex = 1;
            gameTypeTextArea.setText(bingo.pickFirstRound());
            patternType = gameTypeTextArea.getText();
            nextRoundTextField.setText(bingo.pickSecondRound());
            
            // 初始化所有组件
            card = new Card();
            c1Card = new Card();
            c2Card = new Card();
            c3Card = new Card();
            c4Card = new Card();
            c5Card = new Card();
            c6Card = new Card();
            c7Card = new Card();
            c8Card = new Card();
            
            // 初始化数组
            c1Rectangles = new Rectangle[]{C1B1Rect, C1B2Rect, C1B3Rect, C1B4Rect, C1B5Rect, C1I1Rect, C1I2Rect, C1I3Rect, C1I4Rect, C1I5Rect, C1N1Rect, C1N2Rect, C1N4Rect, C1N5Rect, C1G1Rect, C1G2Rect, C1G3Rect, C1G4Rect, C1G5Rect, C1O1Rect, C1O2Rect, C1O3Rect, C1O4Rect, C1O5Rect};
            c2Rectangles = new Rectangle[]{C2B1Rect, C2B2Rect, C2B3Rect, C2B4Rect, C2B5Rect, C2I1Rect, C2I2Rect, C2I3Rect, C2I4Rect, C2I5Rect, C2N1Rect, C2N2Rect, C2N4Rect, C2N5Rect, C2G1Rect, C2G2Rect, C2G3Rect, C2G4Rect, C2G5Rect, C2O1Rect, C2O2Rect, C2O3Rect, C2O4Rect, C2O5Rect};
            c3Rectangles = new Rectangle[]{C3B1Rect, C3B2Rect, C3B3Rect, C3B4Rect, C3B5Rect, C3I1Rect, C3I2Rect, C3I3Rect, C3I4Rect, C3I5Rect, C3N1Rect, C3N2Rect, C3N4Rect, C3N5Rect, C3G1Rect, C3G2Rect, C3G3Rect, C3G4Rect, C3G5Rect, C3O1Rect, C3O2Rect, C3O3Rect, C3O4Rect, C3O5Rect};
            c4Rectangles = new Rectangle[]{C4B1Rect, C4B2Rect, C4B3Rect, C4B4Rect, C4B5Rect, C4I1Rect, C4I2Rect, C4I3Rect, C4I4Rect, C4I5Rect, C4N1Rect, C4N2Rect, C4N4Rect, C4N5Rect, C4G1Rect, C4G2Rect, C4G3Rect, C4G4Rect, C4G5Rect, C4O1Rect, C4O2Rect, C4O3Rect, C4O4Rect, C4O5Rect};
            c5Rectangles = new Rectangle[]{C5B1Rect, C5B2Rect, C5B3Rect, C5B4Rect, C5B5Rect, C5I1Rect, C5I2Rect, C5I3Rect, C5I4Rect, C5I5Rect, C5N1Rect, C5N2Rect, C5N4Rect, C5N5Rect, C5G1Rect, C5G2Rect, C5G3Rect, C5G4Rect, C5G5Rect, C5O1Rect, C5O2Rect, C5O3Rect, C5O4Rect, C5O5Rect};
            c6Rectangles = new Rectangle[]{C6B1Rect, C6B2Rect, C6B3Rect, C6B4Rect, C6B5Rect, C6I1Rect, C6I2Rect, C6I3Rect, C6I4Rect, C6I5Rect, C6N1Rect, C6N2Rect, C6N4Rect, C6N5Rect, C6G1Rect, C6G2Rect, C6G3Rect, C6G4Rect, C6G5Rect, C6O1Rect, C6O2Rect, C6O3Rect, C6O4Rect, C6O5Rect};
            c7Rectangles = new Rectangle[]{C7B1Rect, C7B2Rect, C7B3Rect, C7B4Rect, C7B5Rect, C7I1Rect, C7I2Rect, C7I3Rect, C7I4Rect, C7I5Rect, C7N1Rect, C7N2Rect, C7N4Rect, C7N5Rect, C7G1Rect, C7G2Rect, C7G3Rect, C7G4Rect, C7G5Rect, C7O1Rect, C7O2Rect, C7O3Rect, C7O4Rect, C7O5Rect};
            c8Rectangles = new Rectangle[]{C8B1Rect, C8B2Rect, C8B3Rect, C8B4Rect, C8B5Rect, C8I1Rect, C8I2Rect, C8I3Rect, C8I4Rect, C8I5Rect, C8N1Rect, C8N2Rect, C8N4Rect, C8N5Rect, C8G1Rect, C8G2Rect, C8G3Rect, C8G4Rect, C8G5Rect, C8O1Rect, C8O2Rect, C8O3Rect, C8O4Rect, C8O5Rect};
            cpuRectangles = new Rectangle[][]{c1Rectangles, c2Rectangles, c3Rectangles, c4Rectangles, c5Rectangles, c6Rectangles, c7Rectangles, c8Rectangles};
            cards = new Card[]{card, c1Card, c2Card, c3Card, c4Card, c5Card, c6Card, c7Card, c8Card};
            cpuCards = new Card[]{c1Card, c2Card, c3Card, c4Card, c5Card, c6Card, c7Card, c8Card};
            playerCard = new Text[]{B1Text, B2Text, B3Text, B4Text, B5Text, I1Text, I2Text, I3Text, I4Text, I5Text, N1Text, N2Text, N4Text, N5Text, G1Text, G2Text, G3Text, G4Text, G5Text, O1Text, O2Text, O3Text, O4Text, O5Text};
            playerCircles = new Circle[]{B1Circle, B2Circle, B3Circle, B4Circle, B5Circle, I1Circle, I2Circle, I3Circle, I4Circle, I5Circle, N1Circle, N2Circle, N3Circle, N4Circle, N5Circle, G1Circle, G2Circle, G3Circle, G4Circle, G5Circle, O1Circle, O2Circle, O3Circle, O4Circle, O5Circle};
            
            // 设置卡片和显示
            setCards(playerCard);
            displayCard = new Circle[]{displayB1Circle, displayB2Circle, displayB3Circle, displayB4Circle, displayB5Circle, displayI1Circle, displayI2Circle, displayI3Circle, displayI4Circle, displayI5Circle, displayN1Circle, displayN2Circle, displayN4Circle, displayN5Circle, displayG1Circle, displayG2Circle, displayG3Circle, displayG4Circle, displayG5Circle, displayO1Circle, displayO2Circle, displayO3Circle, displayO4Circle, displayO5Circle};
            startDisplay();
            
            // ���置初始下注金额和净收益
            betTextField.setText("10");
            netProfit = 0;
            winningsTextField.setText("0");
            updateBalance();
            
            // 设置窗口标题 - 移到Platform.runLater中
            Platform.runLater(() -> {
                Stage stage = (Stage) balanceTextField.getScene().getWindow();
                if (stage != null) {
                    stage.setTitle("Bingo - Player: " + username);
                    stage.setOnCloseRequest(event -> {
                        if (netProfit != 0) {
                            saveScore(netProfit);
                        }
                        scheduler.shutdown();
                    });
                }
            });
            
            // 添加游戏状态监听
            callButton.setOnAction(e -> {
                if (!gameInProgress) {
                    gameInProgress = true;
                    // 禁用下注相关控件
                    betTextField.setEditable(false);
                    plusButton.setDisable(true);
                    minusButton.setDisable(true);
                }
                callAction(new ActionEvent());
            });
            
            // 在每轮开始时重置游戏状态
            gameInProgress = false;
            betTextField.setEditable(true);
            plusButton.setDisable(false);
            minusButton.setDisable(false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 修改updateBalance方法
    private void updateBalance() {
        if (username != null) {
            int currentBalance = DatabaseService.getUserMoney(username);
            Platform.runLater(() -> {
                if (balanceTextField != null) {
                    balanceTextField.setText(Integer.toString(currentBalance));
                    MainController.getInstance().updateBalance();
                }
            });
        }
    }
    
    // 添加保存分数的方法
    private void saveScore(int score) {
        try {
            // 保存到数据库
            String sql = "INSERT INTO game_records (username, game_name, score, play_date) VALUES (?, ?, ?, ?)";
            Connection conn = DatabaseService.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, username);
            pstmt.setString(2, "Bingo");
            pstmt.setInt(3, score);
            pstmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            
            // 保存到文本文件
            try {
                File recordsDir = new File("game_records");
                if (!recordsDir.exists()) {
                    recordsDir.mkdir();
                }
                
                String record = String.format("%s,%d,%s\n", 
                    username, 
                    score, 
                    new Date().toString()
                );
                
                Files.write(
                    Paths.get("game_records/Bingo.txt"), 
                    record.getBytes(), 
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.APPEND
                );
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 更新已叫号码显示
    private void updateCalledNumbers() {
        BTextArea.setText(bingo.printB());
        ITextArea.setText(bingo.printI());
        NTextArea.setText(bingo.printN());
        GTextArea.setText(bingo.printG());
        OTextArea.setText(bingo.printO());
    }
}

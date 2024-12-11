package GamePlatform.Game.GoFish;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.InputStream;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.Random;
import GamePlatform.Game.GameRecordManager;
import GamePlatform.Game.GameStats;
import GamePlatform.Game.GameRecord;
import javafx.application.Platform;
import javax.swing.JOptionPane;

public class GoFishController {
   @FXML private HBox playerHand;
   @FXML private HBox robotHand;
   @FXML private Label messageArea;
   @FXML private Label selectedCardLabel;

   private Deck newDeck;
   private List<Card> myHand = new ArrayList<>();
   private List<Card> botHand = new ArrayList<>();
   private List<Card> myMatches = new ArrayList<>();
   private List<Card> botMatches = new ArrayList<>();
   private boolean isPlayerTurn = true;
   private String currentPlayer;
   private static final String GAME_NAME = "Go Fish";
   private boolean gameRunning = false;

   @FXML
   public void initialize() {
      gameRunning = false;
      newDeck = new Deck();
      newDeck.shuffle();

      // Deal 14 cards alternately between player and bot
      for (int i = 0; i < 14; i++) {
         Card drawn = newDeck.draw();
         if (i % 2 == 0) {
            myHand.add(drawn);
         } else {
            botHand.add(drawn);
         }
      }
      update();
   }

   private void checkMatch() {
      List<Card> playerMatches = new ArrayList<>();

      for (int i = 0; i < myHand.size(); i++){
         Card card1 = myHand.get(i);
         for (int j = i +1; j < myHand.size(); j++) {
            Card card2 = myHand.get(j);
            if (card1.getRank().equals(card2.getRank())) {
               playerMatches.add(card1);
               playerMatches.add(card2);
            }
         }
      }
      if (!playerMatches.isEmpty()) {
         myHand.removeAll(playerMatches);
         myMatches.addAll(playerMatches);
         update();
      }

      List<Card> otherMatches = new ArrayList<>();
      for (int i = 0; i< botHand.size(); i++) {
         Card card1 = botHand.get(i);
         for (int j = i + 1; j < botHand.size(); j++) {
            Card card2 = botHand.get(j);
            if (card1.getRank().equals(card2.getRank())) {
               otherMatches.add(card1);
               otherMatches.add(card2);
            }
         }
      }
      if (!otherMatches.isEmpty()) {
         botHand.removeAll(otherMatches);
         botMatches.addAll(otherMatches);
         update();
      }
   }

   private void update() {
      playerHand.getChildren().clear();
      for (Card card : myHand) {
         try {
            // 修改图片路径格式以匹配实际文件名
            String cardName = card.getRank().toString().toLowerCase() + "_of_" + 
                            card.getSuit().toString().toLowerCase();
            String imagePath = "/GamePlatform/Game/GoFish/cards/" + cardName + ".png";
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                throw new IllegalArgumentException("Image not found: " + imagePath);
            }
            ImageView cardImage = new ImageView(new Image(imageStream));
            cardImage.setFitHeight(100);
            cardImage.setFitWidth(70);

            // Add click event listener
            cardImage.setOnMouseClicked(event -> myTurn(card));
            playerHand.getChildren().add(cardImage);

         } catch (Exception e) {
            messageArea.setText("Card image not found: " + card);
            e.printStackTrace();
         }
      }

      // Load the card backs for the bot's hand
      robotHand.getChildren().clear();
      for (int i = 0; i < botHand.size(); i++) {
         try {
            String backImagePath = "/GamePlatform/Game/GoFish/card_back.png";
            InputStream imageStream = getClass().getResourceAsStream(backImagePath);
            if (imageStream == null) {
                throw new IllegalArgumentException("Card back image not found");
            }
            ImageView cardBack = new ImageView(new Image(imageStream));
            cardBack.setFitHeight(100);
            cardBack.setFitWidth(70);
            robotHand.getChildren().add(cardBack);
         } catch (Exception e) {
            messageArea.setText("Card back image missing.");
            e.printStackTrace();
         }
      }
      
      if (isGameOver()) {
         gameOver();
      }
   }

   private void myTurn(Card selectedCard) {
      if (!isPlayerTurn) {
         messageArea.setText("Bot's turn");
         return;
      }

      selectedCardLabel.setText("You selected: " + selectedCard);

      boolean botHasMatch = false;
      Card matchingCard = null;

      for (Card card : botHand) {
         if (card.getRank().equals(selectedCard.getRank())) {
            botHasMatch = true;
            matchingCard = card;
            break;
         }
      }

      if (botHasMatch) {
         messageArea.setText("You found a match. Pick another card!");
         myMatches.add(selectedCard);
         myMatches.add(matchingCard);

         myHand.remove(selectedCard);
         botHand.remove(matchingCard);
         update();
      } else {
         messageArea.setText("Go Fish!");
         if (newDeck.size() > 0) {
            myHand.add(newDeck.draw());
            checkMatch();
            update();
            isPlayerTurn = false;
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(e -> botTurn());
            delay.play();
         }
      }
   }

   private void botTurn() {
      boolean foundMatch;

      do {
         foundMatch = false;
         Random random = new Random();
         Card randomCard = botHand.get(random.nextInt(botHand.size()));
         for (Card playerCard : myHand) {
            if (randomCard.getRank().equals(playerCard.getRank())){
               messageArea.setText("Bot found a match.");

               botHand.remove(randomCard);
               myHand.remove(playerCard);
               botMatches.add(randomCard);
               botMatches.add(playerCard);

               update();
               foundMatch = true;
               break;
            }
         }
      } while (foundMatch);

      messageArea.setText("Bot drew a card. Your turn!");
      if (newDeck.size() > 0) {
         botHand.add(newDeck.draw());
         update();
      }
      isPlayerTurn = true;
   }

   private boolean isGameOver() {
      return myHand.isEmpty() || botHand.isEmpty() || newDeck.size() == 0;
   }

   private String winner() {
      int playerMatchesCount = myMatches.size() / 2;
      int botMatchesCount = botMatches.size() / 2;
      
      String result;
      int finalScore = playerMatchesCount * 10;
      
      if (playerMatchesCount > botMatchesCount) {
         result = "Game Over! You win with " + playerMatchesCount + " matches!";
      } else if (botMatchesCount > playerMatchesCount) {
         result = "Game Over! Bot wins with " + botMatchesCount + " matches!";
      } else {
         result = "Game Over! It's a tie!";
      }
      
      // 保存游戏记录
      GameRecordManager.saveGameRecord(currentPlayer, GAME_NAME, finalScore);
      
      // 显示历史最高分
      showHighScores();
      
      return result;
   }

   private void showHighScores() {
      List<GameRecord> records = GameRecordManager.loadGameRecords(GAME_NAME);
      
      // 找出最高分
      int highestScore = 0;
      String topPlayer = "";
      for (GameRecord record : records) {
         if (record.getScore() > highestScore) {
            highestScore = record.getScore();
            topPlayer = record.getPlayer();
         }
      }
      
      // 更新消息区域显示最高分
      String highScoreMessage = String.format("\nAll-time high score: %d by %s", 
                                            highestScore, topPlayer);
      messageArea.setText(messageArea.getText() + highScoreMessage);
   }

   private void gameOver() {
      gameRunning = false;
      
      int playerMatchesCount = myMatches.size() / 2;
      int finalScore = playerMatchesCount * 10;
      
      // 保存分数
      if (finalScore > 0) {
         GameRecordManager.saveGameRecord(currentPlayer, GAME_NAME, finalScore);
      }
      
      String message = winner();
      
      int option = JOptionPane.showConfirmDialog(
         null,
         message + "\nWould you like to play again?",
         "Game Over",
         JOptionPane.YES_NO_OPTION
      );
      
      if (option == JOptionPane.YES_OPTION) {
         newGame();
      } else {
         Platform.exit();
      }
   }

   @FXML
   private void newGame() {
    // Reset everything to start a new game
      myHand.clear();
      botHand.clear();
      myMatches.clear();
      botMatches.clear();
      newDeck = new Deck();
      newDeck.shuffle();
      isPlayerTurn = true;

      // Deal 14 cards alternately between player and bot
      for (int i = 0; i < 14; i++) {
         Card drawn = newDeck.draw();
         if (i % 2 == 0) {
            myHand.add(drawn);
         } else {
            botHand.add(drawn);
         }
      }

      update();
      messageArea.setText("New game started. Your turn.");
      gameRunning = true;
   }

   @FXML
   private void exit() {
      System.exit(0);
   }

   public void setCurrentPlayer(String player) {
      this.currentPlayer = player;
   }
}
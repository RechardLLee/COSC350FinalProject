/** This is the prototype for the Deck class
 * @author Morgan Eckert
 * @version 11/20/2024
*/

package GamePlatform.Game.GoFish;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;


public class Deck {
	private static final SecureRandom random = new SecureRandom();
	private static final int NUM_OF_CARDS  = 52; // constant 52 for size of deck
	private List<Card> deck = new ArrayList<>(NUM_OF_CARDS);
	private int current = 0; // index of next card


	public Deck() {

		for (Suit suit : Suit.values()){
			for (Rank rank : Rank.values()){
				deck.add(new Card(rank, suit));
			}
		}
	}

	// shuffle method
	public void shuffle(){
		Collections.shuffle(deck);
	}

	// size method
	public int size(){
		return deck.size();
	}

	// draw hand method
	public Card draw() {
		if (current < deck.size()){
			return deck.get(current++);
		}
		else {
			return null;
		}
	}

}
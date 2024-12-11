package GamePlatform.Game.GoFish;

/** This is the Card class
@author Morgan Eckert
@version 11/3/2024
*/

// class for a single card
public class Card {
	private final Rank rank; // will be declared as enums
	private final Suit suit;

	// contructor
	public Card(Rank cardRank, Suit cardSuit) {
		this.rank = cardRank;
		this.suit = cardSuit;
	}

	// getters
	public Rank getRank(){
		return rank;
	}

	public Suit getSuit(){
		return suit;
	}

	// string representation
	public String toString() {
		return rank + " of " + suit;
	}
}

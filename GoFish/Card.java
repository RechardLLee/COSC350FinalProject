/** This is the Card class
@author Morgan Eckert
@version 11/3/2024
*/

// class for a single card
public class Card {
	private final Deck.Rank rank; // will be declared as enums
	private final Deck.Suit suit;

	// contructor
	public Card(Deck.Rank cardRank, Deck.Suit cardSuit) {
		this.rank = cardRank;
		this.suit = cardSuit;
	}

	// getters
	public Deck.Rank getRank(){
		return rank;
	}

	public Deck.Suit getSuit(){
		return suit;
	}

	// string representation
	public String toString() {
		return rank + " of " + suit;
	}
}

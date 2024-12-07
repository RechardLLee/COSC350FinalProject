import java.util.*;
public class BingoTester{

	public static void main(String[] args){
		Card card = new Card();
		StandardBingo bingo = new StandardBingo();
		System.out.println(card.checkBingo("Single Bingo"));
		for (int i = 0; i < 16; i++){
			card.markCall(i);
		}
		System.out.println(card.checkBingo("Single Bingo"));
		System.out.println(card.checkBingo("Double Bingo"));
		for (int i = 0; i < 32; i++){
			card.markCall(i);
		}
		System.out.println(card.checkBingo("Single Bingo"));
		System.out.println(card.checkBingo("Double Bingo"));
	}
}
import java.util.*;

//to sort players by score
public class playerSort implements Comparator<Player>{
	public int compare(Player player1, Player player2){
		return player2.getScore() - player1.getScore();
	}
}
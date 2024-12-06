/** Player class
* represents a player with a name and score
*@author Scott Weckman
*/



public class Player{

	//player name
	private String name;

	//player score
	private int score;

	//To create player with no score
	public Player(String name){
		this.name = name;
		score = 0;
	}
	//To create player with score
	public Player(String name, int score){
		this.name = name;
		this.score = score;
	}

	//return player name
	public String getName(){
		return name;
	}
	//To add 1 point to players score
	public void score(){
		score++;
	}
	//String representation of player
	public String toString(){
		String rtn = String.format("%s:\t%d", name, score);
		return rtn;
	}

	//Get score
	public int getScore(){
		return score;
	}
}
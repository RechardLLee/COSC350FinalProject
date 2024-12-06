/**
* This is the Bingo Class
* It will be the framework for each game mode/run a basic bingo game
*@author Scott Weckman
*@version 11/29/2023
*/


import java.util.*;
import java.security.SecureRandom;
import java.io.*;
public abstract class Bingo{

	//hold game mode
	private int gameType;

	//hold winners
	private Queue<String> wins;

	//hold formatted called numbers
	private boolean[] calledList;

	//hold players in game with points
	private ArrayList<Player> players;

	//Random Number Generator
	public static SecureRandom random = new SecureRandom();

	public static Scanner scanner;

	private int lastCall;

	//Blank Constructor
	public Bingo(){
		players = new ArrayList<Player>();
		wins = new Queue<String>();

	}

	//Constructor with game type
	public Bingo(int gameType){
		players = new ArrayList<Player>();
		this.gameType = gameType;
		calledList = new boolean[gameType];
		wins = new Queue<String>();
	}

	//Set game type
	public void setGameType(int gameType){
		this.gameType = gameType;
	}

	//Get calledList
	public boolean[] getCalledList(){
		return calledList;
	}
	//Get players
	public ArrayList<Player> getPlayers(){
		return players;
	}
	//Set players
	public void setPlayers(ArrayList<Player> players){
		this.players = players;
	}
	//Set call
	public void setLastCall(int call){
		lastCall = call;
	}
	//Get call
	public int getLastCall(){
		return lastCall;
	}
	//call number
	public abstract String call();

	//record number as called
	public void recordCall(int call){
		calledList[call] = true;
	}

	//Set calledList
	public void setCalledList(int size){
		calledList = new boolean[size + 1];
	}

	//format call based on game mode
	public abstract String formatCall(Integer call);


	//to score, adds a player name to the queue for each point
	public void score(String name, int points){
		for(int k = 0; k < points; k++){
			wins.add(name.toUpperCase());
		}
	}

	//adds queued scores to total
	public void refreshScores(){
		while(!(wins.isEmpty())){
			String n = wins.remove();
			Player newPlayer;
			for(Player player : players){
				if(player.getName().equals(n)){
					player.score();
					n = "";
				}
			}

			if(!(n.equals(""))){
				players.add(new Player(n, 1));
			}
		}
	}

	//to display scores
	public String printScores(){
		String rtn = "";
		Collections.sort(players, new playerSort());
		for(Player player : players){
			rtn = String.format("%s\n%s\n", rtn, player.toString());
		}
		return rtn;
	}
	//Save scores to file
	public void saveScores()throws IOException, ClassNotFoundException{
		DataOutputStream f = new DataOutputStream(new FileOutputStream("Scores.bin"));
		for(Player player : getPlayers()){
			f.writeUTF(player.getName());
			f.write(player.getScore());
		}
		f.close();
	}
	//Read scores from file
	public ArrayList<Player> readScores()throws IOException, ClassNotFoundException{
		ArrayList<Player> rtn = new ArrayList<Player>();
		DataInputStream d = new DataInputStream(new FileInputStream("Scores.bin"));
		while(d.available()>0){
			String name = d.readUTF();
			int score = d.read();
			Player player = new Player(name, score);
			rtn.add(player);

		}
		d.close();
		setPlayers(rtn);
		return rtn;
	}
	// public String readBalance(){
	// 	try (Scanner scanner = new Scanner(new File("user_data.txt"))) {
    //         while (scanner.hasNextLine()) {
    //             System.out.println(scanner.nextLine());
    //         }
    //     } catch (FileNotFoundException e) {
    //         e.printStackTrace();
    //     }

	// }
}
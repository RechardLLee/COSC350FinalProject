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



	//hold formatted called numbers
	private boolean[] calledList;


	//Random Number Generator
	public static SecureRandom random = new SecureRandom();

	public static Scanner scanner;

	private int lastCall;

	//Blank Constructor
	public Bingo(){

	}

	//Constructor with game type
	public Bingo(int gameType){
		this.gameType = gameType;
		calledList = new boolean[gameType];
	}

	//Set game type
	public void setGameType(int gameType){
		this.gameType = gameType;
	}

	//Get calledList
	public boolean[] getCalledList(){
		return calledList;
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
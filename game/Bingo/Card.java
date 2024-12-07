import java.util.*;
public class Card{

	//hold nums on card
	private ArrayList<Integer> card = new ArrayList<Integer>();
	private boolean[] cardMarked = new boolean[24];
	//for checking single bingo
	private boolean[][] singleBingos = {
		//columns
		//B column
		{true, true, true, true, true,
		 false, false, false, false, false,
		  false, false, false, false,
		   false, false, false, false, false,
		    false, false, false, false, false},
		//I column
		{false, false, false, false, false,
		 true, true, true, true, true,
		  false, false, false, false,
		   false, false, false, false, false,
		    false, false, false, false, false},
		//N column
		{false, false, false, false, false,
		 false, false, false, false, false,
		  true, true, true, true,
		   false, false, false, false, false,
		    false, false, false, false, false},
		//G column
		{false, false, false, false, false,
		 false, false, false, false, false,
		  false, false, false, false,
		   true, true, true, true, true,
		    false, false, false, false, false},
		//O column
		{false, false, false, false, false,
		 false, false, false, false, false,
		  false, false, false, false,
		   false, false, false, false, false,
		    true, true, true, true, true},
		//rows
		//row 1
		{true, false, false, false, false,
		 true, false, false, false, false,
		  true, false, false, false,
		   true, false, false, false, false,
		    true, false, false, false, false},
		//row 2
		{false, true, false, false, false,
		 false, true, false, false, false,
		  false, true, false, false,
		   false, true, false, false, false,
		    false, true, false, false, false},
		//row 3
		{false, false, true, false, false,
		 false, false, true, false, false,
		  false, false, false, false,
		   false, false, true, false, false,
		    false, false, true, false, false},
		//row 4
		{false, false, false, true, false,
		 false, false, false, true, false,
		  false, false, true, false,
		   false, false, false, true, false,
		    false, false, false, true, false},
		//row 5
		{false, false, false, false, true,
		 false, false, false, false, true,
		  false, false, false, true,
		   false, false, false, false, true,
		    false, false, false, false, true},

		//diags
		{true, false, false, false, false,
		 false, true, false, false, false,
		  false, false, false, false,
		   false, false, false, true, false,
		    false, false, false, false, true},
		{false, false, false, false, true,
		 false, false, false, true, false,
		  false, false, false, false,
		   false, true, false, false, false,
		    true, false, false, false, false},
		//4 corners/postage stamp
		{true, false, false, false, true,
		 false, false, false, false, false,
		  false, false, false, false,
		   false, false, false, false, false,
		    true, false, false, false, true},
		{false, false, false, false, false,
		 false, false, false, false, false,
		  false, false, false, false,
		   true, true, false, false, false,
		    true, true, false, false, false}
		
	};

	private boolean[] diamondBingo = {
		false, false, false, false, false,
		false, false, true, false, false,
		false, true, true, false,
		false, false, true, false, false,
		false, false, false, false, false};

	private boolean[] frameBingo = {
		false, false, false, false, false,
		false, true, true, true, false,
		false, true, true, false,
		false, true, true, true, false,
		false, false, false, false, false};

	private boolean[] ladderBingo = {
		false, false, false, false, false,
		true, true, true, true, true,
		false, true, true, false,
		true, true, true, true, true,
		false, false, false, false, false};

	private boolean[] treeBingo = {
		false, false, true, false, false,
		false, true, true, false, false,
		true, true, true, true,
		false, true, true, false, false,
		false, false, true, false, false};

	private boolean[] blackoutBingo = {
		true, true, true, true, true,
		true, true, true, true, true,
		true, true, true, true,
		true, true, true, true, true,
		true, true, true, true, true};

	private boolean[] turtleBingo = {
		false, true, false, false, true,
		false, true, true, true, false,
		true, true, true, false,
		false, true, true, true, false,
		false, true, false, false, true};


	//comparing arrays to check bingos
	public boolean compare(boolean[] b1, boolean[] b2){
		boolean rtn = true;
		for (int i = 0; i < b1.length; i++){
			if (b1[i]){
				if(!b2[i]){
					rtn = false;
				}
			}
		}
		return rtn;
	}
	//constructor
	public Card(){
		int num = 0;
		for(int k = 1; k < 6; k++){
			for(int i = 1; i < 6; i++)
				if (!((k == 3) && (i == 3))){
					num = Bingo.random.nextInt(15) + ((k - 1) * 15) + 1;
					while (check(num)){
						num = Bingo.random.nextInt(15) + ((k - 1) * 15) + 1;
					}
					card.add(num);
				}
				
		}
	}

	//setters and getters/print
	public ArrayList<Integer> getCard(){
		return card;
	}
	public void printCard(){
		for(int i : card){
			System.out.println(i);
		}
	}
	public boolean[] getMarked(){
		return cardMarked;
	}
	public void printMarked(){
		for(boolean b : cardMarked){
			System.out.println(b);
		}
	}

	public void printCardMarked(){
		for(int i = 0; i < card.size(); i++){
			System.out.println(card.get(i) + ": " + cardMarked[i]);
		}
	}

	//check to see if a num is already on the card
	public boolean check(Integer i){
		for(int n : card){
			if(n == i){
				return true;
			}
		}
		return false;
	}
	//mark a called number
	public void markCall(int i){
		for (int n : card){
			if(n == i){
				cardMarked[card.indexOf(n)] = true;
			}
		}
	}
	//clear card
	public void clearCard(){
		card.clear();
	}

	//clear marked
	public void clearMarked(){
		Arrays.fill(cardMarked, false);
	}

	public boolean checkBingo(String bingo){

		if(bingo.equals("Single Bingo")){
			return checkSingle();
		}
		else if(bingo.equals("Diamond Inside")){
			return checkDiamond();
		}
		else if(bingo.equals("Double Bingo")){
			return checkDouble();
		}
		else if(bingo.equals("Frame Inside")){
			return checkFrame();
		}
		else if(bingo.equals("Ladder")){
			return checkLadder();
		}
		else if(bingo.equals("Blackout")){
			return checkBlackout();
		}
		else if(bingo.equals("Turtle")){
			return checkTurtle();
		}
		else if(bingo.equals("Tree")){
			return checkTree();
		}
		return false;

	}

	public boolean checkSingle(){
		for(boolean[] pattern : singleBingos){
			if(compare(pattern, cardMarked)){
				return true;
			}
		}
		return false;

	}
	public boolean checkDiamond(){
		return compare(diamondBingo, cardMarked);
	}
	public boolean checkDouble(){
		int bingos = 0;
		for(boolean[] pattern : singleBingos){
			if(compare(pattern, cardMarked)){
				bingos++;
				if(bingos == 2){
					return true;
				}
			}
		}
		return false;
	}
	public boolean checkFrame(){
		return compare(frameBingo, cardMarked);
	}
	public boolean checkLadder(){
		return compare(ladderBingo, cardMarked);
	}
	public boolean checkBlackout(){
		return compare(blackoutBingo, cardMarked);
	}
	public boolean checkTurtle(){
		return compare(turtleBingo, cardMarked);
	}
	public boolean checkTree(){
		return compare(treeBingo, cardMarked);
	}

	public int getIndexOfCall(int call){
		return card.indexOf(call);
	}

	public boolean[][] getPattern(String patternType){
		if(patternType.equals("Single Bingo")){
			return singleBingos;
		}
		else if(patternType.equals("Double Bingo")){
			return singleBingos;
		}
		else if(patternType.equals("Diamond Inside")){
			return new boolean[][]{diamondBingo};
		}
		else if(patternType.equals("Blackout")){
			return new boolean[][]{blackoutBingo};
		}
		else if(patternType.equals("Tree")){
			return new boolean[][]{treeBingo};
		}
		else if(patternType.equals("Turtle")){
			return new boolean[][]{turtleBingo};
		}
		else if(patternType.equals("Ladder")){
			return new boolean[][]{ladderBingo};
		}
		else if(patternType.equals("Frame Inside")){
			return new boolean[][]{frameBingo};
		}
		else if(patternType.equals("Tree")){
			return new boolean[][]{treeBingo};
		}

		return singleBingos;
	}

}
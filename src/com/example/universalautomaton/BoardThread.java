
public class BoardThread extends Thread {
	private boolean isUpdating;
	private byte chosenGame;
	private int rows;
	private int cols;
	private Board board;
	private int startingRow; //>0
	private int endingRow; //<rows
	// <startingRow, endingRow) tak bedzie wygodniej podzielic plansze
	
	public BoardThread(boolean isUpdating, byte chosenGame, int rows, int cols, Board board, int startingRow, int endingRow) {
		super();
		this.isUpdating = isUpdating;
		this.chosenGame = chosenGame;
		this.rows = rows;
		this.cols = cols;
		this.board = board;
		this.startingRow = startingRow;
		this.endingRow = endingRow;
	}

	public void run() {
		if(isUpdating)
			updateBoard();
		else {
			if(chosenGame == C.GOL)
				calculateNextStateGOL();
			else if(chosenGame == C.WW)
				calculateNextStateWW();
		}
	}
	
	public void updateBoard() {
		for(int i=startingRow; i<endingRow; i++)
			for(int j=1; j<cols-1; j++)
				board.getCell(i, j).updateState();
	}
	
	public void calculateNextStateGOL() {
		for(int i=startingRow; i<endingRow; i++)
			for(int j=1; j<cols-1; j++)
			{
				int friendsWithStateON = countFriendsWithState(i, j, C.ON);
				byte cellState = board.getCell(i, j).getState();
				
				if(cellState == C.OFF && friendsWithStateON == 3)
					board.getCell(i, j).setNextState(C.ON);
				else if(cellState == C.ON && friendsWithStateON != 2 && friendsWithStateON != 3)
					board.getCell(i, j).setNextState(C.OFF);
				else if(cellState == C.ON && (friendsWithStateON == 2 || friendsWithStateON == 3))
					board.getCell(i, j).setNextState(C.ON);
				else
					board.getCell(i, j).setNextState(C.OFF); //przydatne zabezpieczenie przeciw komorkom WW ktore sa teraz traktowane jak wylaczone
			}
	}
	
	public void calculateNextStateWW() {
		for(int i=startingRow; i<endingRow; i++)
			for(int j=1; j<cols-1; j++)
			{
				int friendsWithStateHEAD = countFriendsWithState(i, j, C.HEAD);
				byte cellState = board.getCell(i, j).getState();
				
				if(cellState == C.OFF)
					board.getCell(i, j).setNextState(C.OFF);
				else if(cellState == C.HEAD)
					board.getCell(i, j).setNextState(C.TAIL);
				else if(cellState == C.TAIL)
					board.getCell(i, j).setNextState(C.ON);
				else if(friendsWithStateHEAD == 1 || friendsWithStateHEAD == 2)
					board.getCell(i, j).setNextState(C.HEAD);
				else {
					board.getCell(i, j).setNextState(C.ON);
				}
			}
	}
	
	private int countFriendsWithState(int i, int j, byte friendState) {
		int count = 0;
		if(board.getCell(i, j-1).getState()==friendState)
			count++;
		if(board.getCell(i, j+1).getState()==friendState)
			count++;
		if(board.getCell(i-1, j).getState()==friendState)
			count++;
		if(board.getCell(i+1, j).getState()==friendState)
			count++;
		if(board.getCell(i-1, j-1).getState()==friendState)
			count++;
		if(board.getCell(i-1, j+1).getState()==friendState)
			count++;
		if(board.getCell(i+1, j-1).getState()==friendState)
			count++;
		if(board.getCell(i+1, j+1).getState()==friendState)
			count++;
		return count;
	}
}

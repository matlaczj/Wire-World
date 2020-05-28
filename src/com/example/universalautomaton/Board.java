package com.example.universalautomaton;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

public class Board {
	private int rows;
	private int cols;
	private Cell[][] board;
	private byte chosenGame;
	private LoadBoardFromFile fileLoader;
	private File file;
	
	public Board(int rows, int cols, byte chosenGame) {
		this.rows = rows;
		this.cols = cols;
		this.chosenGame = chosenGame;
		initializeBoard();
	}
	
	public int getRows() {
		return rows;
	}
	public int getCols() {
		return cols;
	}
	public Cell getCell(int r, int c) { 
		return board[r][c];
	}
	
	private void initializeBoard() {
		BoardClickListener bcl = new BoardClickListener(chosenGame); //wspolny dla wszystkich z oszczednosci pamieci
		BoardDragListener bdl = new BoardDragListener(chosenGame);
		board = new Cell [rows][cols];
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++)
			{
				if(i==0 || j==0 || i==rows-1 || j==cols-1)
				{
					board[i][j] = new Cell(C.PADD, C.PADD);
					continue;
				}
				board[i][j] = new Cell(C.OFF, C.OFF);
				board[i][j].addActionListener(bcl);		
				board[i][j].addMouseListener(bdl); //niestety ma to koszt, spowalnia program
			}
	}	
	public void changeCellsSize(Dimension d) {
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++) {
				board[i][j].setPreferredSize(d);
				board[i][j].setMinimumSize(d);
				board[i][j].setMaximumSize(d);
			}
	}
	
	public void updateBoard() {
		for(int i=1; i<rows-1; i++)
			for(int j=1; j<cols-1; j++)
				board[i][j].updateState();
	}
	
	public void calculateNextStateGOL() {
		for(int i=1; i<rows-1; i++)
			for(int j=1; j<cols-1; j++)
			{
				int friendsWithStateON = countFriendsWithState(i, j, C.ON);
				byte cellState = board[i][j].getState();
				
				if(cellState == C.OFF && friendsWithStateON == 3)
					board[i][j].setNextState(C.ON);
				else if(cellState == C.ON && friendsWithStateON != 2 && friendsWithStateON != 3)
					board[i][j].setNextState(C.OFF);
				else if(cellState == C.ON && (friendsWithStateON == 2 || friendsWithStateON == 3))
					board[i][j].setNextState(C.ON);
				else
					board[i][j].setNextState(C.OFF); //przydatne zabezpieczenie przeciw komorkom WW ktore sa teraz traktowane jak wylaczone
			}
	}
	
	public void calculateNextStateWW() {
		for(int i=1; i<rows-1; i++)
			for(int j=1; j<cols-1; j++)
			{
				int friendsWithStateHEAD = countFriendsWithState(i, j, C.HEAD);
				byte cellState = board[i][j].getState();
				
				if(cellState == C.OFF)
					board[i][j].setNextState(C.OFF);
				else if(cellState == C.HEAD)
					board[i][j].setNextState(C.TAIL);
				else if(cellState == C.TAIL)
					board[i][j].setNextState(C.ON);
				else if(friendsWithStateHEAD == 1 || friendsWithStateHEAD == 2)
					board[i][j].setNextState(C.HEAD);
				else {
					board[i][j].setNextState(C.ON);
				}
			}
	}
	
	private int countFriendsWithState(int i, int j, byte friendState) {
		int count = 0;
		if(board[i][j-1].getState()==friendState)
			count++;
		if(board[i][j+1].getState()==friendState)
			count++;
		if(board[i-1][j].getState()==friendState)
			count++;
		if(board[i+1][j].getState()==friendState)
			count++;
		if(board[i-1][j-1].getState()==friendState)
			count++;
		if(board[i-1][j+1].getState()==friendState)
			count++;
		if(board[i+1][j-1].getState()==friendState)
			count++;
		if(board[i+1][j+1].getState()==friendState)
			count++;
		return count;
	}

	public void addStruct(String name, int x, int y) throws FileNotFoundException {
		addStruct(name, x, y, "RIGHT");
	}

	public void addStruct(String name, int x, int y, String direction) throws FileNotFoundException {
		byte dir = Directions.getStructDirection(direction);
		fileLoader = new LoadBoardFromFile(chosenGame);
		if (chosenGame == C.WW)
			file = new File("src\\structures\\wireworld\\" + name + ".wire");
		else if (chosenGame == C.GOL)
			file = new File("src\\structures\\gameoflife\\" + name + ".life");
		fileLoader.loadStructFromFile(this, file, x, y, dir);
	}
	
	
}

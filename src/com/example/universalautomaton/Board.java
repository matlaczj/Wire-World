package com.example.universalautomaton;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class Board {
	private int rows;
	private int cols;
	private Cell[][] board;
	private byte chosenGame;
	private LoadBoardFromFile fileLoader;
	private File file;
	private BoardClickListener bcl;
	private BoardDragListener bdl;
	private ActionListener tmpl;
	
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
		bcl = new BoardClickListener(chosenGame); //wspolny dla wszystkich z oszczednosci pamieci
		bdl = new BoardDragListener(chosenGame);
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
	
	public void updateBoardOrCalculateNextState(boolean isUpdating) {
		int availableThreads = Runtime.getRuntime().availableProcessors();
			if(rows-2 < availableThreads)
			{
				int previousStartingRow = 1;
				for(int i=0; i<rows-2; i++)
				{
					new BoardThread(isUpdating, chosenGame, rows, cols, this, previousStartingRow, previousStartingRow+1).run();
					previousStartingRow += 1;
				}
			}
			else if(rows-2 > availableThreads) 
			{
				int previousStartingRow = 1;
				if((rows-2) % availableThreads != 0)
				{
					for(int i=0; i<availableThreads-1; i++)
					{
						new BoardThread(isUpdating, chosenGame, rows, cols, this, previousStartingRow, previousStartingRow + (rows-2)/availableThreads).run();
						previousStartingRow += (rows-2)/availableThreads;
					}
					new BoardThread(isUpdating, chosenGame, rows, cols, this, previousStartingRow, previousStartingRow + 1 + (rows-2) % availableThreads).run();
				}
				else if((rows-2) % availableThreads == 0)
				{
					for(int i=0; i<availableThreads; i++)
					{
						new BoardThread(isUpdating, chosenGame, rows, cols, this, previousStartingRow, previousStartingRow + (rows-2)/availableThreads).run();
						previousStartingRow += (rows-2)/availableThreads;
					}
				}
			}
			else if(rows-2 == availableThreads)
			{
				int previousStartingRow = 1;
				for(int i=0; i<availableThreads; i++)
				{
					new BoardThread(isUpdating, chosenGame, rows, cols, this, previousStartingRow, previousStartingRow+1).run();
					previousStartingRow += 1;
				}
			}
		}

	public void addStruct(String name, int x, int y, byte dirOrigin) throws FileNotFoundException {
		addStruct(name, x, y, "RIGHT", dirOrigin);
	}

	public void addStruct(String name, int x, int y, String direction, byte dirOrigin) throws FileNotFoundException {
		byte mir = (byte) ((Directions.getStructDirection(direction) / 4 + dirOrigin / 4) % 2);
		byte dir = (byte) (mir*4 + (Directions.getStructDirection(direction) + dirOrigin) % 4);
		fileLoader = new LoadBoardFromFile(chosenGame);
		if (chosenGame == C.WW)
			file = new File("src\\structures\\wireworld\\" + name + ".wire");
		else if (chosenGame == C.GOL)
			file = new File("src\\structures\\gameoflife\\" + name + ".life");
		fileLoader.loadStructFromFile(this, file, x, y, dir);
	}
	
	public void setChosenGame(byte chosenGame) {
		this.chosenGame = chosenGame;
	}
	
	public void setActionListener(ActionListener l) {
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++)
			{
				board[i][j].removeActionListener(bcl);
				board[i][j].removeMouseListener(bdl);
				board[i][j].removeActionListener(tmpl);
				board[i][j].addActionListener(l);
				board[i][j].setActionCommand(i + " " + j);
			}
		tmpl = l;
	}
	
	public void restoreListeners() {
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++)
			{
				board[i][j].removeActionListener(bcl);
				board[i][j].removeMouseListener(bdl);
				board[i][j].removeActionListener(tmpl);
				if(i==0 || j==0 || i==rows-1 || j==cols-1)
					continue;
				board[i][j].addActionListener(bcl);		
				board[i][j].addMouseListener(bdl);
			}
	}
}

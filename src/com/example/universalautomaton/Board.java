package com.example.universalautomaton;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
	private String pathSeparator;

	public Board(int rows, int cols, byte chosenGame) {
		pathSeparator = File.separator;
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
		bcl = new BoardClickListener(chosenGame); // wspolny dla wszystkich z oszczednosci pamieci
		bdl = new BoardDragListener(chosenGame);
		board = new Cell[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1) {
					board[i][j] = new Cell(C.PADD, C.PADD);
					board[i][j].setVisible(false);
					continue;
				}
				board[i][j] = new Cell(C.OFF, C.OFF);
				board[i][j].addActionListener(bcl);
				board[i][j].addMouseListener(bdl); // niestety ma to koszt, spowalnia program
			}
	}

	public void changeCellsSize(Dimension d) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				board[i][j].setPreferredSize(d);
				board[i][j].setMinimumSize(d);
				board[i][j].setMaximumSize(d);
			}
	}

	public void updateBoardOrCalculateNextState(boolean isUpdating) {
		int availableThreads = Runtime.getRuntime().availableProcessors();
		ArrayList<BoardThread> boardThreadsArrayList = new ArrayList<>();
		if (rows - 2 < availableThreads) {
			int previousStartingRow = 1;
			for (int i = 0; i < rows - 2; i++) {
				boardThreadsArrayList.add(new BoardThread(isUpdating, chosenGame, cols, this, previousStartingRow,
						previousStartingRow + 1));
				previousStartingRow += 1;
				boardThreadsArrayList.get(boardThreadsArrayList.size() - 1).start();
			}
		} else if (rows - 2 > availableThreads) {
			int previousStartingRow = 1;
			if ((rows - 2) % availableThreads != 0) {
				for (int i = 0; i < availableThreads - 1; i++) {
					boardThreadsArrayList.add(new BoardThread(isUpdating, chosenGame, cols, this,
							previousStartingRow, previousStartingRow + (rows - 2) / availableThreads));
					previousStartingRow += (rows - 2) / availableThreads;
					boardThreadsArrayList.get(boardThreadsArrayList.size() - 1).start();
				}
				new BoardThread(isUpdating, chosenGame, cols, this, previousStartingRow,
						previousStartingRow + (rows - 2) / availableThreads + (rows - 2) % availableThreads).run();
			} else if ((rows - 2) % availableThreads == 0) {
				for (int i = 0; i < availableThreads; i++) {
					boardThreadsArrayList.add(new BoardThread(isUpdating, chosenGame, cols, this,
							previousStartingRow, previousStartingRow + (rows - 2) / availableThreads));
					previousStartingRow += (rows - 2) / availableThreads;
					boardThreadsArrayList.get(boardThreadsArrayList.size() - 1).start();
				}
			}
		} else if (rows - 2 == availableThreads) {
			int previousStartingRow = 1;
			for (int i = 0; i < availableThreads; i++) {
				boardThreadsArrayList.add(new BoardThread(isUpdating, chosenGame, cols, this, previousStartingRow,
						previousStartingRow + 1));
				previousStartingRow += 1;
				boardThreadsArrayList.get(boardThreadsArrayList.size() - 1).start();
			}
		}
		try {
			for (BoardThread boardThread : boardThreadsArrayList)
				boardThread.join();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void addStruct(String name, int x, int y, byte dirOrigin) throws FileNotFoundException {
		addStruct(name, x, y, "RIGHT", dirOrigin);
	}

	public void addStruct(String name, int x, int y, String direction, byte dirOrigin) throws FileNotFoundException {
		byte mir = (byte) ((Directions.getStructDirection(direction) / 4 + dirOrigin / 4) % 2);
		byte dir = (byte) (mir * 4 + (Directions.getStructDirection(direction) + dirOrigin) % 4);
		fileLoader = new LoadBoardFromFile(chosenGame);
		if (chosenGame == C.WW)
			file = new File("structures" + pathSeparator + "wireworld" + pathSeparator + ""
					+ name + ".wire");
		else if (chosenGame == C.GOL)
			file = new File("structures" + pathSeparator + "gameoflife" + pathSeparator + ""
					+ name + ".life");
		fileLoader.loadStructFromFile(this, file, x, y, dir);
	}

	public void setChosenGame(byte chosenGame) {
		this.chosenGame = chosenGame;
	}

	public void setListeners(ActionListener l) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				board[i][j].removeActionListener(bcl);
				board[i][j].removeMouseListener(bdl);
				board[i][j].removeActionListener(tmpl);
				board[i][j].addActionListener(l);
				board[i][j].setActionCommand(i + " " + j);
			}
		tmpl = l;
	}

	public void restoreListeners() {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				board[i][j].removeActionListener(bcl);
				board[i][j].removeMouseListener(bdl);
				board[i][j].removeActionListener(tmpl);
				if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1)
					continue;
				board[i][j].addActionListener(bcl);
				board[i][j].addMouseListener(bdl);
			}
	}
}

package com.example.universalautomaton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveBoardToFile {
	private Board board;
	private byte chosenGame = C.GOL;
	private String legend;
	private String pathSeparator;

	private String golSavedStatesCatalogPath;
	private String wwSavedStatesCatalogPath;
	private String savedStatesCatalogPath;

	private String golSavedStatesExtension;
	private String wwSavedStatesExtension;
	private String savedStatesExtension;

	private int golStateNumber = 0;
	private int wwStateNumber = 0;
	private int stateNumber;

	public SaveBoardToFile(Board board, byte chosenGame) {
		this.board = board;
		this.chosenGame = chosenGame;
		pathSeparator = File.separator;

		legend = "legend to numbers:\r\n" + "	universal:\r\n" + "		OFF = 0\r\n" + "		ON = 1\r\n"
				+ "	for WW only:\r\n" + "		HEAD = 2\r\n" + "		TAIL = 3";
		golSavedStatesCatalogPath = "output_files" + pathSeparator + "gameoflife"
				+ pathSeparator + "output"; // kiedys bedzie mozna ja wybrac dowolna w gui
		golSavedStatesExtension = ".life";
		wwSavedStatesCatalogPath = "output_files" + pathSeparator + "wireworld" + pathSeparator
				+ "output";
		wwSavedStatesExtension = ".wire";
	}

	public void saveBoardToFile() {
		saveBoardToFile("");
	}

	public void saveBoardToFile(String filepath) {
		try {
			FileWriter fw;

			if (filepath == "") {
				if (chosenGame == C.GOL) {
					savedStatesCatalogPath = golSavedStatesCatalogPath;
					savedStatesExtension = golSavedStatesExtension;
					stateNumber = golStateNumber;
					golStateNumber++;
				}
				if (chosenGame == C.WW) {
					savedStatesCatalogPath = wwSavedStatesCatalogPath;
					savedStatesExtension = wwSavedStatesExtension;
					stateNumber = wwStateNumber;
					wwStateNumber++;
				}
				fw = new FileWriter(new File(savedStatesCatalogPath + stateNumber + savedStatesExtension));
			} else
				fw = new FileWriter(new File(filepath));

			fw.write("rows: " + (board.getRows() - 2) + "\n");
			fw.write("columns: " + (board.getCols() - 2) + "\n");
			fw.write("\n");
			for (int i = 1; i < board.getRows() - 1; i++) // nie zapisujemy ramki, bo funkcja wczytujaca sama ja dodaje
			{
				for (int j = 1; j < board.getCols() - 1; j++) {
					if (board.getCell(i, j).getState() == C.OFF)
						fw.write(String.valueOf(C.OFF));
					else if (board.getCell(i, j).getState() == C.ON)
						fw.write(String.valueOf(C.ON));
					else if (board.getCell(i, j).getState() == C.HEAD)
						fw.write(String.valueOf(C.HEAD));
					else if (board.getCell(i, j).getState() == C.TAIL)
						fw.write(String.valueOf(C.TAIL));
				}
				fw.write("\n");
			}
			fw.write("\n");
			fw.write(legend);
			fw.close();
		} catch (IOException e) {
			;
		}
	}

	public void setChosenGame(byte chosenGame) {
		this.chosenGame = chosenGame;
	}

}

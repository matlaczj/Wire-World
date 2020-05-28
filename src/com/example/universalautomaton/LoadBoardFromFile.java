package com.example.universalautomaton;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//uniwersalna klasa do wczytywania z pliku .life dla GOL oraz WW, potencjalnie rozszerzalna dla innej gry
public class LoadBoardFromFile {
	private String usersCatalogPath = "";
	private byte chosenGame = C.GOL;
	private String buffer;
	int fileRows, fileCols;
	
	public LoadBoardFromFile(byte chosenGame){
		this.chosenGame = chosenGame;
	}
	
	public Board loadBoardFromFile(String fileName) {
		try {
			File file;
			if(!usersCatalogPath.equals("")) {
				file = new File(usersCatalogPath);
			}
			else if(chosenGame == C.GOL)
				file = new File("src\\output_files\\gameoflife\\" + fileName);
			else if(chosenGame == C.WW)
				file = new File("src\\output_files\\wireworld\\" + fileName);
			else 
				return null; //jesli wybierzemy nieprawidlowy plik dla wybranego typu gry
			
			Scanner s = new Scanner(file); //uniwersalna sciezka, dzialajaca
			s.next();
			int rows = s.nextInt()+2; //+2 dla paddingu
			s.next();
			int cols = s.nextInt()+2;
			Board board = new Board(rows, cols, chosenGame);
			s.close();
			loadFileIntoBoard(board, file);
			return board;
		}
		catch(FileNotFoundException ex) {
			return null;
		}
		
	}
	
	public void loadStructFromFile(Board board, File file, int x, int y, byte dir) throws FileNotFoundException {
		loadFileIntoBoard(board, file, x, y, dir);
	}
	
	private void loadFileIntoBoard (Board board, File file) throws FileNotFoundException {
		loadFileIntoBoard(board, file, 0, 0, Directions.R);
	}
	
	private void loadFileIntoBoard (Board board, File file, int x, int y, byte dir) throws FileNotFoundException {	
		//ta metoda laduje plik do planszy zaczynajac od pozycji (x,y)
		Scanner s = new Scanner(file);
		s.next();
		fileRows = s.nextInt();
		s.next();
		fileCols = s.nextInt();
		s.nextLine();
		s.nextLine();
		loadBlockIntoBoard(board, s, x, y, dir);
		String[] structCommand;
		for(buffer = s.nextLine().trim(); buffer.length()!=0 && buffer.charAt(0) == '+'; buffer = s.nextLine().trim()) {
			structCommand = buffer.substring(1).split(",",4);
			if (structCommand.length < 3)
				continue;
			else if (structCommand.length == 3)
				board.addStruct(structCommand[0], Integer.parseInt(structCommand[1]) + x, Integer.parseInt(structCommand[2]) + y);
			else
				board.addStruct(structCommand[0], Integer.parseInt(structCommand[1]) + x, Integer.parseInt(structCommand[2]) + y, structCommand[3]);
		}
		
		s.close();
	}
	
	public String getUsersCatalogPath() {
		return usersCatalogPath;
	}

	public void setUsersCatalogPath(String usersCatalogPath) {
		this.usersCatalogPath = usersCatalogPath;
	}
	
	private void loadBlockIntoBoard(Board board, Scanner s, int x, int y, byte dir) {
		int rows = board.getRows();
		int cols = board.getCols();	
		int jstart = 1;
		switch (dir) {
		
		case Directions.R:
			if (x < 0)
				jstart -= x;
			for(int i=1; i<1+fileRows; i++)
			{
				buffer = s.nextLine().trim();
				if (i+y >= rows-1 || i+y < 1)
					continue;
				for(int j=jstart; j<1+fileCols && j+x<cols-1; j++)
				{
					if(buffer.charAt(j-1)-'0' == (int)C.ON)
						board.getCell(i+y, j+x).setState(C.ON);
					else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
						board.getCell(i+y, j+x).setState(C.HEAD);
					else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
						board.getCell(i+y, j+x).setState(C.TAIL);
				}
			}
			break;
			
		case Directions.RR:		//odbicie wzgl osi X
			if (x < 0)
				jstart -= x;
			for(int i = 1; i<1+fileRows; i++)
			{
				buffer = s.nextLine().trim();
				if (fileRows-i+1+y >= rows-1 || fileRows-i+1+y < 1)
					continue;
				for(int j=jstart; j<1+fileCols && j+x<cols-1; j++)
				{
					if(buffer.charAt(j-1)-'0' == (int)C.ON)
						board.getCell(fileRows-i+1+y, j+x).setState(C.ON);
					else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
						board.getCell(fileRows-i+1+y, j+x).setState(C.HEAD);
					else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
						board.getCell(fileRows-i+1+y, j+x).setState(C.TAIL);
				}
			}
			break;
			
		case Directions.L:		//obrot o 180 == 2 odbicia, wzgl X i wzgl Y
			if (fileCols + x > cols - 2)
				jstart += fileCols + x - cols + 2;
			for(int i=1; i<1+fileRows; i++)
			{
				buffer = s.nextLine().trim();
				if (fileRows-i+1+y >= rows-1 || fileRows-i+1+y < 1)
					continue;
				for(int j=jstart; j<1+fileCols; j++)
				{
					if(buffer.charAt(j-1)-'0' == (int)C.ON)
						board.getCell(fileRows-i+1+y, fileCols-j+1+x).setState(C.ON);
					else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
						board.getCell(fileRows-i+1+y, fileCols-j+1+x).setState(C.HEAD);
					else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
						board.getCell(fileRows-i+1+y, fileCols-j+1+x).setState(C.TAIL);
				}
			}
			break;
			
		case Directions.LR:		//odbicie wzgl osi Y
			if (fileCols + x > cols - 2)
				jstart += fileCols + x - cols + 2;
			for(int i=1; i<1+fileRows; i++)
			{
				buffer = s.nextLine().trim();
				if (i+y >= rows-1 || i+y < 1)
					continue;
				for(int j=jstart; j<1+fileCols; j++)
				{
					if(buffer.charAt(j-1)-'0' == (int)C.ON)
						board.getCell(i+y, fileCols-j+1+x).setState(C.ON);
					else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
						board.getCell(i+y, fileCols-j+1+x).setState(C.HEAD);
					else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
						board.getCell(i+y, fileCols-j+1+x).setState(C.TAIL);
				}
			}
			break;
			
		case Directions.D:		//obrot o 90 zgodnie ze wsk zegara
			if (y < 0)
				jstart -= y;
			for(int i=1; i<1+fileRows; i++)
			{
				buffer = s.nextLine().trim();
				if (fileCols-i+1+x >= cols-1 || fileCols-i+1+x < 1)
					continue;
				for(int j=jstart; j<1+fileCols && j+y<rows-1; j++)
				{
					if(buffer.charAt(j-1)-'0' == (int)C.ON)
						board.getCell(j+y, fileRows-i+1+x).setState(C.ON);
					else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
						board.getCell(j+y, fileRows-i+1+x).setState(C.HEAD);
					else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
						board.getCell(j+y, fileRows-i+1+x).setState(C.TAIL);
				}
			}
			break;
			
		case Directions.DR:		//odbicie wzgl osi y = -x
			if (y < 0)
				jstart -= y;
			for(int i=1; i<1+fileRows; i++)
			{
				buffer = s.nextLine().trim();
				if (i+x >= cols-1 || i+x < 1)
					continue;
				for(int j=jstart; j<1+fileCols && j+y<rows-1; j++)
				{
					if(buffer.charAt(j-1)-'0' == (int)C.ON)
						board.getCell(j+y, i+x).setState(C.ON);
					else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
						board.getCell(j+y, i+x).setState(C.HEAD);
					else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
						board.getCell(j+y, i+x).setState(C.TAIL);
				}
			}
			break;
			
		case Directions.U:		//obrot o 90 przeciwnie do wsk zegara
			if (fileCols + y > rows - 2)
				jstart += fileCols + y - rows + 2;
			for(int i=1; i<1+fileRows; i++)
			{
				buffer = s.nextLine().trim();
				if (i+x >= cols-1 || i+x < 1)
					continue;
				for(int j=jstart; j<1+fileCols; j++)
				{
					if(buffer.charAt(j-1)-'0' == (int)C.ON)
						board.getCell(fileCols-j+1+y, i+x).setState(C.ON);
					else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
						board.getCell(fileCols-j+1+y, i+x).setState(C.HEAD);
					else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
						board.getCell(fileCols-j+1+y, i+x).setState(C.TAIL);
				}
			}
			break;
			
		case Directions.UR:		//odbicie wzgl osi y=x
			if (fileCols + y > rows - 2)
				jstart += fileCols + y - rows + 2;
			for(int i=1; i<1+fileRows; i++)
			{
				buffer = s.nextLine().trim();
				if (fileRows-i+1+x >= cols-1 || fileRows-i+1+x < 1 )
					continue;
				for(int j=jstart; j<1+fileCols; j++)
				{
					if(buffer.charAt(j-1)-'0' == (int)C.ON)
						board.getCell(fileCols-j+1+y, fileRows-i+1+x).setState(C.ON);
					else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
						board.getCell(fileCols-j+1+y, fileRows-i+1+x).setState(C.HEAD);
					else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
						board.getCell(fileCols-j+1+y, fileRows-i+1+x).setState(C.TAIL);
				}
			}
			break;
			
		default:
				throw new IllegalArgumentException("Bad structure direction");
				
		}
	}
}

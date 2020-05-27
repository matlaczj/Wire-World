import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//uniwersalna klasa do wczytywania z pliku .life dla GOL oraz WW, potencjalnie rozszerzalna dla innej gry
public class LoadBoardFromFile {
	private String usersCatalogPath = "";
	private byte chosenGame = C.GOL;
	
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
	
	private void loadFileIntoBoard (Board board, File file) throws FileNotFoundException {
		loadFileIntoBoard(board, file, 0, 0);
	}
	
	private void loadFileIntoBoard (Board board, File file, int x, int y) throws FileNotFoundException {	
		//this method loads the file into the board at position (x,y)
		Scanner s = new Scanner(file);
		s.next();
		int fileRows = s.nextInt();
		s.next();
		int fileCols = s.nextInt();
		s.nextLine();
		s.nextLine();
		String buffer;
		int rows = board.getRows();
		int cols = board.getCols();	
		for(int i=1; i<1+fileRows && i<rows-1; i++)
		{
			buffer = s.nextLine().trim();
			for(int j=1; j<1+fileCols && j<cols-1; j++)
			{
				if(buffer.charAt(j-1)-'0' == (int)C.ON)
					board.getCell(i+x, j+y).setState(C.ON);
				else if(buffer.charAt(j-1)-'0' == (int)C.OFF)
					board.getCell(i+x, j+y).setState(C.OFF);
				else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
					board.getCell(i+x, j+y).setState(C.HEAD);
				else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
					board.getCell(i+x, j+y).setState(C.TAIL);
			}
		}
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
}

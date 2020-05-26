import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//uniwersalna klasa do wczytywania z pliku .life dla GOL oraz WW, potencjalnie rozszerzalna dla innej gry
public class LoadBoardFromFile {
	
	public static Board loadBoardFromFile(String fileName) {
		try {
			String buffer;
			Scanner s = new Scanner(new File( "src\\saved_states\\gol_saved_states\\" + fileName)); //uniwersalna sciezka, dzialajaca
			s.next();
			int rows = s.nextInt()+2; //+2 dla paddingu
			s.next();
			int cols = s.nextInt()+2;
			Board board = new Board(rows, cols);
			s.nextLine();
			s.nextLine();
			
			int i = 0;
			for(int j=0; j<cols; j++)
				board.getCell(i, j).setState(C.PADD);
			i = rows-1;
			for(int j=0; j<cols; j++)
				board.getCell(i, j).setState(C.PADD);
			
			for(i=1; i<rows-1; i++)
			{
				buffer = s.nextLine().trim();
				for(int j=0; j<cols; j++)
				{
					if(j==0 || j==cols-1)
					{
						board.getCell(i, j).setState(C.PADD);
						continue;
					}
					if(buffer.charAt(j-1)-'0' == (int)C.ON)
						board.getCell(i, j).setState(C.ON);
					else if(buffer.charAt(j-1)-'0' == (int)C.OFF)
						board.getCell(i, j).setState(C.OFF);
					else if(buffer.charAt(j-1)-'0' == (int)C.HEAD)
						board.getCell(i, j).setState(C.HEAD);
					else if(buffer.charAt(j-1)-'0' == (int)C.TAIL)
						board.getCell(i, j).setState(C.TAIL);
				}
			}
			s.close();
			return board;
		}
		catch(FileNotFoundException ex) {
			return null;
		}
		
	}
}

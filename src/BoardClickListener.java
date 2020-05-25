import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardClickListener implements ActionListener {
	private int rows;
	private int cols;
	private Board board;
	private byte stateChangeClock = 0;
//	private static String lastCommand;
	
	public BoardClickListener(int rows, int cols, Board board) {
		super();
		this.rows = rows;
		this.cols = cols;
		this.board = board;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		for(int i=1; i<rows-1; i++)
			for(int j=1; j<cols-1; j++)
				if(command.equals(i+" "+j))
				{
					//if(lastCommand != command)
						stateChangeClock = board.getCell(i, j).getState();
					stateChangeClock++;
					board.getCell(i,j).setState((byte)(stateChangeClock%4));
				}
		//lastCommand = command;
	}

}

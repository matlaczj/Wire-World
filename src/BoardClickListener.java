import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardClickListener implements ActionListener {
	private byte stateChangeClock = 0;

	@Override
	public void actionPerformed(ActionEvent e) {
		Cell cell = (Cell)e.getSource();
		stateChangeClock = cell.getState();
		stateChangeClock++;
		cell.setState((byte)(stateChangeClock%4));
	}

}


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardDragListener implements MouseListener{
	private byte stateChangeClock = 0;
	private boolean isMouseCurrentlyPressed = false;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(isMouseCurrentlyPressed) {
			Cell cell = (Cell)e.getSource();
			stateChangeClock = cell.getState();
			stateChangeClock++;
			cell.setState((byte)(stateChangeClock%4));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		isMouseCurrentlyPressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isMouseCurrentlyPressed = false;
	}

}

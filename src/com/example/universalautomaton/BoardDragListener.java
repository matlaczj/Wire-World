package com.example.universalautomaton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardDragListener implements MouseListener{
	private byte stateChangeClock = 0;
	private boolean isMouseCurrentlyPressed = false;
	private byte chosenGame;
	
	public BoardDragListener(byte chosenGame) {
		super();
		this.chosenGame = chosenGame;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(isMouseCurrentlyPressed) {
			Cell cell = (Cell)e.getSource();
			stateChangeClock = cell.getState();
			stateChangeClock++;
			if (chosenGame == C.WW)
				cell.setState((byte)(stateChangeClock%4));
			else if (chosenGame == C.GOL)
				cell.setState((byte)(stateChangeClock%2));
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


package com.example.universalautomaton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardClickListener implements ActionListener { 
	private byte stateChangeClock = 0;
	private byte chosenGame;
	
	public BoardClickListener(byte chosenGame) {
		super();
		this.chosenGame = chosenGame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Cell cell = (Cell)e.getSource();
		stateChangeClock = cell.getState();
		stateChangeClock++;
		if (chosenGame == C.WW)
			cell.setState((byte)(stateChangeClock%4));
		else if (chosenGame == C.GOL)
			cell.setState((byte)(stateChangeClock%2));
	}
}

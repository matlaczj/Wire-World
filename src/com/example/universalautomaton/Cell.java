package com.example.universalautomaton;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComponent;

public class Cell extends JButton { 
	//private static final long serialVersionUID = -7309940893358748305L;
	private byte state;
	private byte nextState;
	
	public Cell(byte state, byte nextState) {
		super();
		this.state = state;
		this.nextState = nextState;
		updateColorToMatchState();
	}
	public byte getState() {
		return state;
	}
	public void setState(byte state) { 
		if(this.state != state) {
			this.state = state;
			this.updateColorToMatchState(); //kolor sam sie uaktualnia
		}
	}
	public void setNextState(byte nextState) {
		this.nextState = nextState;
	}
	public void updateState() {
		if(state != nextState) {
			this.state = this.nextState;
			this.updateColorToMatchState();
		}
	}
	public void updateColorToMatchState() {
		if(state == C.OFF)
			this.setBackground(Color.BLACK);
		else if(state == C.ON)
			this.setBackground(Color.ORANGE);
		else if(state == C.HEAD)
			this.setBackground(Color.BLUE);
		else if(state == C.TAIL)
			this.setBackground(Color.RED);
		else if(state == C.PADD) 
			this.setVisible(false); //nie widac ramki
		
	}
	
}

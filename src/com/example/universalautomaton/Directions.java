package com.example.universalautomaton;

public class Directions {
	public static final byte R = 0;	//struktura skierowana w prawo, domyslnie
	public static final byte D = 1;	//dol
	public static final byte L = 2;	//lewo
	public static final byte U = 3;	//gora
	public static final byte RR = 4;	//te z R to to samo co wyzej tylko ze odbite lustrzanie
	public static final byte DR = 5;
	public static final byte LR = 6;
	public static final byte UR = 7;
	public static byte getStructDirection (String name) {
		switch(name) {
		case "RIGHT":
			return 0;
		case "DOWN":
			return 1;
		case "LEFT":
			return 2;
		case "UP":
			return 3;
		case "RIGHTR":
			return 4;
		case "DOWNR":
			return 5;
		case "LEFTR":
			return 6;
		case "UPR":
			return 7;
		default:
				throw new IllegalArgumentException("Bad direction name");
		}
	}
}

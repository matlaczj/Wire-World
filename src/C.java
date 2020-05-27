
public class C { //constants
	public static final byte OFF = 0; 
	public static final byte ON = 1; 
	public static final byte HEAD = 2; 
	public static final byte TAIL = 3;
	public static final byte PADD = 4; 
	public static final byte GOL = 5;
	public static final byte WW = 6;
	
	public static final byte R = 0;	//structure faces right, default
	public static final byte D = 1;	//down
	public static final byte L = 2;	//left
	public static final byte U = 3;	//up
	public static final byte RR = 4;	//those with R are the same as above but also mirrored
	public static final byte DR = 5;
	public static final byte LR = 6;
	public static final byte UR = 7;
	public static final byte getStructDirection (String name) {
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

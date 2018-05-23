package mas.lucas.Tools;

import java.util.*;

public final class Constants {

	private Constants() {
		// restrict instantiation
	}

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public static final HashMap<String, String> ANSI_COLOR = new HashMap<String, String>();
	static {
		ANSI_COLOR.put("r", ANSI_RED_BACKGROUND);
		ANSI_COLOR.put("w", ANSI_WHITE_BACKGROUND);
		ANSI_COLOR.put("g", ANSI_GREEN_BACKGROUND);
		ANSI_COLOR.put("b", ANSI_BLUE_BACKGROUND);
		ANSI_COLOR.put("rst", ANSI_RESET);
	}	
	
	public static final HashMap<String, Integer> letterToNumber = new HashMap<String, Integer>();
	static {
		letterToNumber.put("A", 0);
		letterToNumber.put("B", 1);
		letterToNumber.put("C", 2);
		letterToNumber.put("D", 3);
		letterToNumber.put("E", 4);
		letterToNumber.put("F", 5);
		letterToNumber.put("G", 6);
		letterToNumber.put("H", 7);
		letterToNumber.put("I", 8);
		letterToNumber.put("J", 9);
	}
	
	public static final HashMap<Integer, String > numberToLetter = new HashMap<Integer, String>();
	static {
		numberToLetter.put(0,"A");
		numberToLetter.put(1,"B");
		numberToLetter.put(2,"C");
		numberToLetter.put(3,"D");
		numberToLetter.put(4,"E");
		numberToLetter.put(5,"F");
		numberToLetter.put(6,"G");
		numberToLetter.put(7,"H");
		numberToLetter.put(8,"I");
		numberToLetter.put(9,"J");
	}
	
	public static final HashMap<String, Integer> shipClassSize = new HashMap<String, Integer>();
	static {
		//shipClassSize.put("DoneTest", 1);
		shipClassSize.put("Carrier", 5);
		shipClassSize.put("Battleship", 4);
		shipClassSize.put("Cruiser", 3);
		shipClassSize.put("Submarine", 3);
		shipClassSize.put("Destroyer", 2);
	}
	


}

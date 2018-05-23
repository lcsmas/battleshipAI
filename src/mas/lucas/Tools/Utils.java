package mas.lucas.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mas.lucas.AI.AI;
import mas.lucas.AI.AILvl0;
import mas.lucas.AI.AILvl1;
import mas.lucas.AI.AILvl2;
import mas.lucas.Ship;
import mas.lucas.Exception.BadFormatException;
import mas.lucas.Exception.OutOfGridException;
import mas.lucas.Exception.ShipCreationException;

public class Utils {
	/**
	 * Say something in the console
	 * @param arg
	 */
	public static void say(String arg) {
		System.out.println(arg);
	}
	
	/**
	 * Ask something in the console
	 * @param arg : The question
	 * @return A string which is the answer of the question
	 */
	public static String ask(String arg) {
		String foo = null;
		System.out.println(arg);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			foo = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			say(e.getMessage());
		}
		return foo.toUpperCase();
	}
	
	/**
	 * Ask the coordinate on which the player want to add the ship
	 * @param size : The size of the ship to add
	 * @return The ship corresponding to the coordinate
	 * @throws ShipCreationException
	 * @throws OutOfGridException
	 * @throws BadFormatException
	 */
	public static Ship askCoordinatesForShipCreation(int size) throws ShipCreationException, OutOfGridException, BadFormatException{
		
		String startCoord = "";
		String endCoord = "";
		startCoord = Utils.ask("Starting coordinate ? (size is " + size + ") (e.g \"AO\", \"D5\")");		
		endCoord = Utils.ask("Ending coordinate ? (e.g \"AO\", \"D5\")");
	
		Checkers.checkShipCreation(startCoord, endCoord);
		
		Ship s = new Ship(startCoord, endCoord);
		int sLength = s.length();
		if ( sLength != size) {
			throw new ShipCreationException("You have to choose starting and ending coordinates corresponding to a ship measuring " + size + " squares not " + sLength);
		}
		
		
		return s;
	}

	public static AI askAILvl(int numAI) {
		while(true) {
			String answer = Utils.ask("Level of the AI number " +  numAI +" ? 0, 1 or 2 (the greater the better)");
			
			if(answer.equals("0")) {
				return new AILvl0(numAI);

			} else if(answer.equals("1")) {
				return new AILvl1(numAI);
			
			} else if(answer.equals("2")) {
				return new AILvl2(numAI);
				
			}	
		}
	}
	
	public static String askMode() {
		while(true) {
			String mode = Utils.ask("Please choose the mode : HVH (human vs human), HVAI (human vs AI) or AIVAI (AI vs AI) :");
			if(mode.equals("HVH") || mode.equals("HVAI") || mode.equals("AIVAI")) {
				return mode;
			}
		}
	}
}

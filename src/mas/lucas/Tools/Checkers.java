package mas.lucas.Tools;

import java.util.List;

import mas.lucas.Case;
import mas.lucas.Grid;
import mas.lucas.Ship;
import mas.lucas.Exception.BadFormatException;
import mas.lucas.Exception.OutOfGridException;
import mas.lucas.Exception.ShipCreationException;

public class Checkers {
	/**
	 * Check the creation of a ship
	 * @param startCoord
	 * @param endCoord
	 * @throws ShipCreationException
	 * @throws BadFormatException
	 * @throws OutOfGridException
	 */
	public static void checkShipCreation(String startCoord, String endCoord) throws ShipCreationException, BadFormatException, OutOfGridException {
		
		String[] coords = {startCoord, endCoord};
		for(String coord : coords) {
			checkCoordValidity(coord);
		}
		
		if (startCoord.charAt(0) != endCoord.charAt(0) && startCoord.charAt(1) != endCoord.charAt(1)) {
			throw new ShipCreationException("The ship has to be placed horizontally or vertically");
		}
	}
	
	/**
	 * Check if the coord are valid
	 * @param coord
	 * @throws BadFormatException
	 * @throws OutOfGridException
	 */
	public static void checkCoordValidity(String coord) throws BadFormatException, OutOfGridException {
		if (coord.length() != 2 ) {
			throw new BadFormatException("The coordinate is not formatted well,"
					+ " it have to be 2 character : a letter in capital and a number (example : \"B7\")");
		}
		
		if(!Character.isLetter(coord.charAt(0))) {
			throw new BadFormatException("The first character of the coordinate has to be a letter");
		}
		
		if (!String.valueOf(Grid.getColumns()).contains(String.valueOf(coord.charAt(0)))) {
			throw new OutOfGridException("You are trying to put something outside of the grid (bad column)");
		} 
		
		if (!String.valueOf(Grid.getLines()).contains(String.valueOf(coord.charAt(1)))) {
			throw new OutOfGridException("You are trying to put something outside of the grid (bad line)");
		} 
	}
	
	/**
	 * Check if there is a ship on the cases where we want to place our ship
	 * @param g The grid we check in
	 * @param s The ship we want to place
	 * @throws Exception
	 */
	public static void checkIfShipOnTheWay(Grid g, Ship s) throws Exception {
		for(Case c : s.getOccupiedCase()) {
			if(g.getOccupiedByShipCase().contains(c)) {
				throw new Exception("You can't place this ship here because there is an other ship on the way (" + c.toString() + ")");
			}
		}
	}
	
	public static boolean existInTheGrid(Case c, Grid g) {
		List<Case> allPossibleCases = g.getAllPossibleCase();
		return allPossibleCases.contains(c);
	}
}

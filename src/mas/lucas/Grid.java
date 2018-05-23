package mas.lucas;
import java.util.ArrayList;
import java.util.List;

import mas.lucas.Tools.Checkers;
import mas.lucas.Tools.Constants;

public class Grid {

	private List<Case> occupiedCase = new ArrayList<Case>(); //Case occupied by a color
	private static char[] columns = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
	private static char[] lines = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private List<Ship> listShip = new ArrayList<Ship>(); //All the ship in the grid
	private int length; //Length of the grid
	private int height; //Height of the grid
	
	/**
	 * In the structure we have occupiedCase to know the case occupied by a color, we are doing this 
	 * even if we have the listShip because we have to record in memory the missed hit that
	 * are case without ship on it. Moreover occupiedCase make some function more readable by using 
	 * the .contains() function of the Collection interface.
	 * That's why, even if it implies to store the ship 2 times, we are doing it this way. 
	 * In our case we don't have a lot of ship to store, but in the case we have much more ship to store
	 * (for example for an MMORPG) it will be a bad thing to store it 2 times. In this case we could have
	 * a list containing miss case (white) and a list containing  ship case (red and green)
	 */

	
	/**
	 * Create a Grid.
	 */
	public Grid(Playable p) {
		this.length = 10;
		this.height = 10;
	}
	
	/**
	 * Add a ship to the grid
	 * @param s : the ship to add in the grid
	 */
	public void addShip(Ship s) {
		this.listShip.add(s);
		this.occupiedCase.addAll(s.getOccupiedCase());
	}
	
	/**
	 * Get the list of all the ship in the grid
	 * @return
	 */
	public List<Ship> getShips() {
		return this.listShip;
	}
	
	/**
	 * Indicate if a case of the grid is occupied by a ship
	 *
	 * @param coord
	 *            a <code>String</code> value that represent the coord of a square
	 *            in the grid (e.g "A0", "B1")
	 */
	public boolean isOccupiedByShip(Case c) {
		List<Ship> listShip = getShips();
		for(Ship s : listShip) {
			if(s.getOccupiedCase().contains(c)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get all case possible of a grid
	 * @return
	 */
	public List<Case> getAllPossibleCase(){
		List<Case> allPossibleCase = new ArrayList<Case>();
		
		for(int i = 0; i < length; i++) {
			for (int j = 0; j < height; j++) {
				String col = Constants.numberToLetter.get(i);
				String line = String.valueOf(j);
				Case c = new Case(col,line);
				if(!isOccupiedByShip(c)) {
					allPossibleCase.add(c);
				}
			}
		}
		allPossibleCase.addAll(getOccupiedByShipCase());
		
		return allPossibleCase;
	}
	
	/**
	 * Return a list of adjacent cases of a case
	 * @param c
	 * @return
	 */
	public List<Case> getAdjacentCases(Case c){
		List<Case> adjacentCases = new ArrayList<>();
		if(Checkers.existInTheGrid(c.getDown(), this)) {
			adjacentCases.add(c.getDown());
		}
		if(Checkers.existInTheGrid(c.getLeft(), this)) {
			adjacentCases.add(c.getLeft());
		}
		if(Checkers.existInTheGrid(c.getRight(), this)) {
			adjacentCases.add(c.getRight());
		}
		if(Checkers.existInTheGrid(c.getUp(), this)) {
			adjacentCases.add(c.getUp());
		}
		return adjacentCases;
	}
	
	/**
	 * Get the unoccupied cases
	 * @return
	 */
	public List<Case> getUnOccupiedCase(){
		List<Case> unOccupiedCase = getAllPossibleCase();
		List<Case> occupiedCase = getOccupiedCase();
		unOccupiedCase.removeAll(occupiedCase);
		
		return unOccupiedCase;
	}
	
	/**
	 * Get the occupied cases
	 * @return
	 */
	public List<Case> getOccupiedCase(){
		return this.occupiedCase;
	}
	
	/**
	 * Get the cases occupied by a ship
	 * @return
	 */
	public List<Case> getOccupiedByShipCase(){
		List<Case> occupiedByShipCase = new ArrayList<Case>();
		for(Ship s : getShips()) {
			occupiedByShipCase.addAll(s.getOccupiedCase());
		}
		return occupiedByShipCase;
	}
	
	/**
	 * Return the identifiers of the columns of the grid
	 * @return
	 */
	public static char[] getColumns() {
		return columns;
	}
	
	/**
	 * Return the identifiers of the lines of the grid
	 * @return
	 */
	public static char[] getLines() {
		return lines;
	}
	
	/**
	 * Get the color of a case corresponding to a coord, if the case is empty its color is blue ("b")
	 * @param coord
	 * @return
	 */
	public String getCaseColor(String coord) {
		String col = String.valueOf(coord.charAt(0));
		String line = String.valueOf(coord.charAt(1));
		
		Case testedCase = new Case(col, line);		
		List<Case> occupiedCase = getOccupiedCase();
		
		int i = occupiedCase.indexOf(testedCase);
		if(i != -1) {
			return occupiedCase.get(i).getColor();
		}
		return "b";
	}
	/**
	 * Put the red color (hit) to the square associated with the coord
	 * @param coord
	 */
	public void addHit(String coord) {
		String col = String.valueOf(coord.charAt(0));
		String line = String.valueOf(coord.charAt(1));
		Case c = new Case(col, line, "r");
		int i = getOccupiedCase().indexOf(c);	
		getOccupiedCase().remove(i);	
		getOccupiedCase().add(c);
		
		//Change the color of the cases in the ship
		for(Ship s : getShips()) {
			if(s.getOccupiedCase().contains(c)) {
				i = s.getOccupiedCase().indexOf(c);
				s.getOccupiedCase().remove(i);	
				s.getOccupiedCase().add(c);		
			}
			
		}
	}
	
	/**
	 * Put the white color (miss) to the square associated with the coord
	 * @param coord
	 */
	public void addMiss(String coord) {
		String col = String.valueOf(coord.charAt(0));
		String line = String.valueOf(coord.charAt(1));
		Case c = new Case(col, line, "w");
		getOccupiedCase().add(c);
	}

	public boolean shipIsHit(String coord) {
		String col = String.valueOf(coord.charAt(0));
		String line = String.valueOf(coord.charAt(1));
		Case c = new Case(col, line);
		if(isOccupiedByShip(c)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Display the grid in the console with the ship
	 */
	public String toString() {
		String color;
		String line = "-------------\n";
		line += "| Ship Grid |\n";
		line += "-------------\n";
		line += "    ";

		for (char col : columns) {
			line += "[" + col + "] ";
		}
		line += "\n";
		
		for (int i = Character.getNumericValue(lines[0]) ; i < lines.length ; i++) {
			line += "[" + i + "] ";
			for (char col : columns) {
				color = getCaseColor(col + String.valueOf(i));
				if(color == null ) {
					line += Constants.ANSI_COLOR.get("b") + "[ ]" + Constants.ANSI_COLOR.get("rst") + " ";
				} else {
					line += Constants.ANSI_COLOR.get(color) + "[ ]" + Constants.ANSI_COLOR.get("rst") + " ";
				}
				
			}
			line += "\n";
		}
		return line;
	}
	
	/**
	 * Display the grid in the console without the ship
	 */
	public String toStringWithoutShip() {
		String color;	
		String line = "-------------\n";
		line += "| Record Grid |\n";
		line += "-------------\n";
		line += "    ";

		for (char col : columns) {
			line += "[" + col + "] ";
		}
		line += "\n";
		for (int i = Character.getNumericValue(lines[0]); i < lines.length; i++) {
			line += "[" + i + "] ";
			for (char col : columns) {
				color = getCaseColor(col + String.valueOf(i));
				if(color == null) {
					line += Constants.ANSI_COLOR.get("b") + "[ ]" + Constants.ANSI_COLOR.get("rst") + " ";
				} else if(color.equals("g")) {
					line += Constants.ANSI_COLOR.get("b") + "[ ]" + Constants.ANSI_COLOR.get("rst") + " ";
				} else {
					line += Constants.ANSI_COLOR.get(color) + "[ ]" + Constants.ANSI_COLOR.get("rst") + " ";
				}
			}
			line += "\n";
		}	
		return line;
	}	
}

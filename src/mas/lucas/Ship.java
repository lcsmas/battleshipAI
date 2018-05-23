package mas.lucas;

import java.util.ArrayList;
import java.util.List;

import mas.lucas.Exception.BadFormatException;
import mas.lucas.Exception.OutOfGridException;
import mas.lucas.Exception.ShipCreationException;
import mas.lucas.Tools.Constants;

public class Ship {


	private Case startCase;
	private Case endCase;
	private List<Case> occupiedCase = new ArrayList<Case>();
	

	/**
	 * Create a ship
	 * @param startCase
	 * @param endCase
	 * @throws ShipCreationException
	 * @throws BadFormatException
	 * @throws OutOfGridException
	 */
	public Ship(Case startCase, Case endCase) {
			this.startCase = startCase;
			this.endCase = endCase;
			this.occupiedCase = calculateOccupiedCase();
			checkIfInvert(startCase, endCase);
			
	}
	
	public Ship(String startCoord, String endCoord) {
		this.startCase = new Case(String.valueOf(startCoord.charAt(0)), String.valueOf(startCoord.charAt(1)));
		this.endCase = new Case(String.valueOf(endCoord.charAt(0)), String.valueOf(endCoord.charAt(1)));
		this.occupiedCase = calculateOccupiedCase();
		checkIfInvert(startCase, endCase);
	}
	
	public void checkIfInvert(Case startCase, Case endCase) {
		//Invert coordinates in case startCase > endCase
		if(getWay().equals("h")) {
			if(Constants.letterToNumber.get(getCol(getStartCase())) > Constants.letterToNumber.get(getCol(getEndCase()))) {
				invertCoord();			
			}
		} else {
			if( Integer.parseInt(getLine(getStartCase())) > Integer.parseInt(getLine(getEndCase())) ) {
				invertCoord();
			}
		} //
	}
	/**
	 * Invert startCase and endCase
	 */
	private void invertCoord() {
		Case foo = getStartCase();
		setStartCase(getEndCase());
		setEndCase(foo);
	}
	
	private void setStartCase(Case coord) {
		this.startCase = coord;
	}
	
	private void setEndCase(Case coord) {
		this.endCase = coord;
	}
	
	/**
	 * Get the start coordinate of the ship
	 * @return the start coordinate of the ship
	 */
	public Case getStartCase() {
		return this.startCase;
	}
	
	public String getCol(Case coord) {
		return coord.getCol();
	}
	
	public String getLine(Case coord) {
		return coord.getLine();
	}
	
	/**
	 * Get the end coordinate of the ship
	 * @return the end coordinate of the ship
	 */
	public Case getEndCase() {
		return this.endCase;
	}
	
	/**
	 * Indicate if the ship has been hit
	 * @param missileCoord : Coordinate of the missile
	 * @return a boolean indicating if the ship has been hit by the missile
	 */
	public boolean isHit(String missileCoord) {
		String col = String.valueOf(missileCoord.charAt(0));
		String line = String.valueOf(missileCoord.charAt(1));
		for(Case c : getOccupiedCase()) {
			if(c.getCol().equals(col) && c.getLine().equals(line)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the length of the ship
	 * @return the length of the ship
	 */
	public int length() {
		return getOccupiedCase().size();
	}
	
	/**
	 * Indicate if the ship is sunk
	 * @return True if it is sunk
	 */
	public boolean isSunk() {
		boolean isSunk = true;
		for(Case c : getOccupiedCase()) {
			if(c.getColor() != "r") {
				isSunk = false;
				break;
			}
		}
		return isSunk;
	}
	
	/**
	 * Get the way the ship is oriented ( "v" = vertical, "h" = horizontal)
	 * @return 
	 */
	public String getWay() {
		if( getCol(getEndCase()).equals(getCol(getStartCase())) ) {
			return "v";
		} else {
			return "h";
		}
	}
	
	/*
	 * Calculate the occupied cases for the initialization of the List occupiedCase
	 */
	private List<Case> calculateOccupiedCase(){
		List<Case> occupiedCase = new ArrayList<Case>();
		
		if( getWay().equals("h") ) {
			
			int startCol = Constants.letterToNumber.get(getCol(getStartCase()));
			int endCol = Constants.letterToNumber.get(getCol(getEndCase()));
			for (int i = startCol; i <= endCol ; i++) {
				occupiedCase.add(new Case(Constants.numberToLetter.get(i),getLine(getStartCase()),"g"));
			}
		} else {
			int startLine = Integer.parseInt(getLine(getStartCase()));
			int endLine = Integer.parseInt(getLine(getEndCase()));
			for (int i = startLine; i <= endLine; i++) {
				occupiedCase.add(new Case(getCol(getStartCase()),String.valueOf(i),"g"));
			}
		}
		
		return occupiedCase;
	}
	
	/*
	 * Get the occupied cases
	 */
	public List<Case> getOccupiedCase() {
		return this.occupiedCase;
	}

	@Override
	public String toString() {
		String res = "";
		for (Case c : getOccupiedCase()) {
			res+=c.toString();
		}
		return res;
	}

}

package mas.lucas;

import mas.lucas.AI.AI;
import mas.lucas.Exception.BadFormatException;
import mas.lucas.Exception.OutOfGridException;
import mas.lucas.Tools.Checkers;
import mas.lucas.Tools.Utils;

public class Human implements Playable {
	private Grid grid = null; 

	private int numPlayer;
	
	/**
	 * Create a player
	 * @param numPlayer
	 */
	public Human(int numPlayer) {
		this.numPlayer = numPlayer;
		this.grid = new Grid(this);
	}

	@Override
	/**
	 * Get the number of the player
	 * @return
	 */
	public int getNum() {
		return this.numPlayer;
	}
	
	@Override
	/**
	 * Place a ship in the grid
	 * @param s
	 */
	public void placeShip(Ship s) {
		getGrid().addShip(s);
	}
	
	@Override
	/**
	 * Get the record grid of the player
	 * @return
	 */
	public Grid getGrid() {
		return this.grid;
	}
	
	@Override
	/**
	 * Give a case to hit
	 * @return The case the player want to hit
	 * @throws OutOfGridException 
	 * @throws BadFormatException 
	 * @throws Exception
	 */
	public String giveHitCase() throws BadFormatException, OutOfGridException {
		String coord = Utils.ask("Player "+ getNum() + ", where do you want to hit ? (e.g \"AO\", \"D5\")");	
		Checkers.checkCoordValidity(coord);
		return coord;
	}
	
	@Override
	/**
	 * Give a ship to place
	 * @param size
	 * @return The ship the player want to place
	 */
	public Ship giveShipToPlace(int size) throws Exception {
		Ship s = Utils.askCoordinatesForShipCreation(size); //Throw an error if the ship can't be created	
		return s;
	}

	@Override
	public void hitSuccessful() {
		Utils.say("Touched !");
		
	}

	@Override
	public void hitMissed() {
		Utils.say("Missed !");		
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj == null) return false;
	    if (obj == this) return true;
	    if (!(obj instanceof Human))return false;
	    AI objCase = (AI)obj;
	    if(objCase.getNum() == this.getNum()) return true;	
		return false;
	}

}

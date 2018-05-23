package mas.lucas.AI;
import java.util.Random;

import mas.lucas.Case;
import mas.lucas.Game;
import mas.lucas.Grid;
import mas.lucas.Playable;
import mas.lucas.Ship;
import mas.lucas.Exception.BadFormatException;
import mas.lucas.Exception.OutOfGridException;
import mas.lucas.Exception.ShipCreationException;
import mas.lucas.Tools.Checkers;
import mas.lucas.Tools.Constants;
import mas.lucas.Tools.Utils;
public abstract class AI implements Playable {
	
	private Grid grid = null; 
	private int numPlayer;
	
	protected abstract String getName();
	protected abstract Ship generateShip(int size);
	protected abstract Case generateHitCase();
	public abstract AI getCopy();
	
	public AI(int numPlayer) {
		this.numPlayer = numPlayer;
		this.grid = new Grid(this);
	}

	/**
	 * Generate a random columns
	 * @return
	 */
	protected String randomCol() {
		Random rand =  new Random();
		int int0_9 = rand.nextInt(10);
		return Constants.numberToLetter.get(int0_9);
	}
	
	/**
	 * Generate a random line
	 * @return
	 */
	protected String randomLine() {
		Random rand =  new Random();
		return String.valueOf(rand.nextInt(10));

	}

	/**
	 * Generate a random endCase for a ship considering start case, way and size of the wanted ship
	 * @param startCase
	 * @param way
	 * @param size
	 * @return
	 */
	protected Case generateEndCaseFromStartCase(Case startCase, String way, int size) {
		Case endCase = null;
		String startCol = startCase.getCol();
		String startLine = startCase.getLine();
		if(way.equals("h")) {			
			int start = Constants.letterToNumber.get(startCol);
			endCase = new Case(Constants.numberToLetter.get(start + size -1),startLine);
		} else {
			int start = Integer.parseInt(startLine);
			endCase = new Case(startCol,String.valueOf(start + size -1));
		}
		return endCase;
	}
	
	/**
	 * Generate a random way to generate a ship
	 * @return
	 */
	protected String randomWay() {
		Random rand =  new Random();
		int foo = rand.nextInt(2);
		if(foo == 1) {
			return "v";
		} else {
			return "h";
		}
	}

	@Override
	public String giveHitCase() throws BadFormatException, OutOfGridException{
		Case c = generateHitCase();
		return c.getCol() + c.getLine();
	}
	
	@Override
	public Ship giveShipToPlace(int size) throws ShipCreationException, BadFormatException, OutOfGridException {
		Ship s = generateShip(size);
		Checkers.checkShipCreation(s.getStartCase().toString(), s.getEndCase().toString());
		return s;
	}

	@Override
	public Grid getGrid() {
		return this.grid;
	}
	
	@Override
	public int getNum() {
		return this.numPlayer;
	}
	
	@Override
	public void placeShip(Ship s) {
		getGrid().addShip(s);
	}
	
	@Override
	public void hitSuccessful() {
		if(Game.isDisplayON()) Utils.say("Touched !");
		
	}

	@Override
	public void hitMissed() {
		if(Game.isDisplayON()) Utils.say("Missed !");		
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) return false;
	    if (obj == this) return true;
	    if (!(obj instanceof AI))return false;
	    AI objCase = (AI)obj;
	    if(objCase.getNum() == this.getNum()) return true;	
		return false;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.getName());
	}
}

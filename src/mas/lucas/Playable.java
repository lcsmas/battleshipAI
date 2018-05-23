package mas.lucas;

import mas.lucas.Exception.BadFormatException;
import mas.lucas.Exception.OutOfGridException;

public interface Playable {
	
	/**
	 * Give a ship that will be placed
	 * @return
	 */
	public Ship giveShipToPlace(int size) throws Exception;
	
	/**
	 * Give a case to hit
	 * @return
	 */
	public String giveHitCase() throws BadFormatException, OutOfGridException;
	
	/**
	 * Get the grid of the playable
	 * @return
	 */
	public Grid getGrid();
	
	/**
	 * Give a ship to a playable
	 */
	public void placeShip(Ship s);
	
	/**
	 * Return the number of the playable
	 * @return
	 */
	public int getNum();
	
	/**
	 * Behavior of the playable if his hit tentative is successful
	 */
	public void hitSuccessful();
	
	/**
	 * Behavior of the playable if his hit tentative is missed
	 */
	public void hitMissed();
	
	/**
	 * Indicate if two player are the same
	 */
	@Override
	public boolean equals(Object obj);
}

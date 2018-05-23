package mas.lucas.Tools;
import mas.lucas.Playable;

public class Display {
	/**
	 * Display both grids of the player in the console
	 * @param p Current player
	 * @param o Opponent of current player
	 */
	public static void displayGrids(Playable p, Playable o) {
		Utils.say("This is the grids of the player " + String.valueOf(p.getNum()));
		displayGridWithoutShip(o);
		displayGridWithShip(p);
		
	}
	
	/**
	 * Display the ship grid of a player
	 * @param p
	 */
	public static void displayGridWithShip(Playable p) {
		Utils.say(p.getGrid().toString());
	}
	
	/**
	 * Display the record grid of a player
	 * @param p
	 */
	public static void displayGridWithoutShip(Playable p) {
		Utils.say(p.getGrid().toStringWithoutShip());
	}
	
}

package mas.lucas;
import java.util.List;

import mas.lucas.AI.AILvl0;
import mas.lucas.AI.AILvl1;
import mas.lucas.AI.AILvl2;
import mas.lucas.Exception.BadFormatException;
import mas.lucas.Exception.OutOfGridException;
import mas.lucas.Tools.Checkers;
import mas.lucas.Tools.Constants;
import mas.lucas.Tools.Display;
import mas.lucas.Tools.Utils;

public class Game {
	private Playable[] players;
	private Playable playingPlayer = null;
	private Playable winner = null;
	
	/**
	 * Indicate to the game if it has to display the grid or not.
	 * It is useful particularly when we want to benchmark AI performance, since two AI are
	 * playing against each other we don't want to display the grids.
	 * The data is private because we use a public static method to set it off
	 */
	private static boolean displayON = true;
	
	/**
	 * We choose a static value for the starting player because it don't depend on the game object 
	 * himself but it still being related to the game logic
	 */
	private static Playable startingPlayer = null; 

	
	public Game(Playable p1, Playable p2) {
		Playable[] p = {null, null};
		if(startingPlayer.equals(p1)) {
			p[0] = p1;
			p[1] = p2;
			
		} else {
			p[1] = p1;
			p[0] = p2;
		}
		
		setPlayers(p);
	}
	
	/**
	 * Initialize the game with runtime mode (known at the execution), wait for user input 
	 * to know the type of game and the levels of AI if an AI is playing
	 */
	public static Playable[] initGame() {
		Playable p1 = null;
		Playable p2 = null;
		String mode = Utils.askMode();
		if(mode.equals("HVH")) {
			p1 = new Human(1);
			p2 = new Human(2);
		} else if(mode.equals("HVAI")) {
			p1 = new Human(1);
			p2 = Utils.askAILvl(2);	
		} else if(mode.equals("AIVAI")) {
			p1 = Utils.askAILvl(1);
			p2 = Utils.askAILvl(2);
		}
		
		switchStartingPlayer(p1,p2);
		
		Playable ps[] = {p1, p2};
		return ps;
	}
	
	/**
	 * Initialize the game with a static mode (known before the execution)
	 * @param m
	 * @return
	 */
	public static Playable[] initGame(String m) {
		Playable p1 = null;
		Playable p2 = null;
		String mode = m;
		if(mode.equals("HVH")) {
			p1 = new Human(1);
			p2 = new Human(2);
		} else if(mode.equals("HVAI")) {
			p1 = new Human(1);
			p2 = Utils.askAILvl(2);	
		} else if(mode.equals("AIVAI")) {
			p1 = Utils.askAILvl(1);
			p2 = Utils.askAILvl(2);
		}
		
		switchStartingPlayer(p1,p2);
		
		Playable ps[] = {p1, p2};
		return ps;
	}
	
	/**
	 * Initialize the game with a static mode and static AI level (known before the execution)
	 * @param m
	 * @return
	 */
	public static Playable[] initGame(String m, String lvl1, String lvl2) {
		Playable p1 = null;
		Playable p2 = null;

		if(lvl1.equals("0")) {
			p1 = new AILvl0(1);

		} else if(lvl1.equals("1")) {
			p1 = new AILvl1(1);
		
		} else if(lvl1.equals("2")) {
			p1 = new AILvl2(1);
			
		}	
		
		if(lvl2.equals("0")) {
			p2 = new AILvl0(2);

		} else if(lvl2.equals("1")) {
			p2 = new AILvl1(2);
		
		} else if(lvl2.equals("2")) {
			p2 = new AILvl2(2);
			
		}	
		
		switchStartingPlayer(p1,p2);
		
		Playable ps[] = {p1, p2};
		return ps;
	}
	
	/**
	 * Check who was the last starting player between the two given in parameter then
	 * switch the starting player
	 * @param p1
	 * @param p2
	 */
	public static void switchStartingPlayer(Playable p1, Playable p2) {
		if(Game.startingPlayer == null) Game.startingPlayer = p1;	
		else if(Game.startingPlayer.equals(p1)) Game.startingPlayer = p2;			
		else if(Game.startingPlayer.equals(p2)) Game.startingPlayer = p1;
	}
	
	/**
	 * Start the game
	 */
	public String start() {
		setUp();
		loop();
		Utils.say("The winner is player " + getWinner().getNum() + " !!" );
		return stop();
	}
	
	public String stop(){
		return getWinner().toString();
	}
	/**
	 * Set up the game (placing ship for both player)
	 */
	public void setUp() {	
		for(Playable p : getPlayers() ) {
			setPlayingPlayer(p);
			Playable playingP = getPlayingPlayer();
			for (String shipClass : Constants.shipClassSize.keySet()) {
				Integer shipSize = Constants.shipClassSize.get(shipClass);
				if(displayON) {
					Display.displayGrids(playingP,getOpponentOfPlayer(playingP));
					Utils.say("Player " + playingP.getNum() + " start to place his ships");
					Utils.say("Time to place the " + shipClass +", his size is " + shipSize);	
				}
				
				while(true) {
					try {
						Ship shipToPlace = askShipToPlace(playingP,shipSize);
						placeShip(playingP, shipToPlace);
						break;
					} catch (Exception e) {
						if(displayON) {
							Display.displayGrids(playingP,getOpponentOfPlayer(p));
							Utils.say(Constants.ANSI_RED + e.getMessage() + Constants.ANSI_RESET);
						}
						
					}
				}			
			}
		}
	}
	
	/**
	 * Ask a ship to place to a player object
	 * @param p
	 * @param size
	 * @return
	 * @throws Exception
	 */
	private Ship askShipToPlace(Playable p, int size) throws Exception {
		return p.giveShipToPlace(size);
	}
	
	/**
	 * This is the game loop (player fight each other)
	 */
	public void loop() {
		while(!isDone()) {
			for(Playable p : getPlayers()) {
				setPlayingPlayer(p);
				if(displayON) {
					Display.displayGrids(p, getOpponentOfPlayer(p));
				}
				
				
				Grid ennemyGrid = getOpponentOfPlayer(getPlayingPlayer()).getGrid();
				
				String coord ="";

				boolean validCoord = false;
				while(!validCoord) {
					try {
						coord = p.giveHitCase();
						validCoord = true;
					} catch (BadFormatException | OutOfGridException e) {
						if(displayON) {
							Display.displayGrids(getPlayingPlayer(), getOpponentOfPlayer(p));
							Utils.say(Constants.ANSI_RED + e.getMessage() + Constants.ANSI_RESET);	
						}
							
					}
				}	
				
				if(ennemyGrid.shipIsHit(coord)) {
					triggerAction("hit");
					ennemyGrid.addHit(coord);	
					
				} else {
					triggerAction("miss");
					ennemyGrid.addMiss(coord);
				}
				
				if(isDone()) {
					setWinner(getPlayingPlayer());
					break;
				}
			}
		}
	}
	
	/**
	 * Indicate if the game is done (all ship of the ennemy player are sunk)
	 * @return True if the game is done
	 */
	private boolean isDone() {
		List<Ship> playerShips = getOpponentOfPlayer(getPlayingPlayer()).getGrid().getShips();
		boolean areAllShipSunk = true;
		for(Ship s : playerShips) {
			if(!s.isSunk()) {
				areAllShipSunk = false;
				break;
			}
		}
		return areAllShipSunk;
		
	}
	
	/**
	 * Place a ship in the player grid
	 * @param p : The player for whom we will add the ship
	 * @param s : The ship to add in the player grid
	 */
	private void placeShip(Playable p, Ship s) throws Exception {
		Grid pGrid = p.getGrid();
		Checkers.checkIfShipOnTheWay(pGrid, s);
		p.placeShip(s);
	}

	/**
	 * Get the opponent of a player
	 * @param p
	 * @return
	 */
	private Playable getOpponentOfPlayer(Playable p) {
		Playable opponent = null;
		for (Playable aPlayer : getPlayers()) {
			if(p != aPlayer) {
				opponent = aPlayer;
			}
		}
		return opponent;
	}	
	
	private void triggerAction(String action) {
		switch (action) {
		case "hit":
			getPlayingPlayer().hitSuccessful();
			break;
		case "miss":
			getPlayingPlayer().hitMissed();
		default:
			break;
		}
	}
	
	/**
	 * Set the players of the game
	 * @param players
	 */
	private void setPlayers(Playable[] players) {
		this.players = players;
	}
	
	/**
	 * Set the player who is currently playing
	 * @param p : The player who play
	 */
	private void setPlayingPlayer(Playable p) {
		this.playingPlayer = p;
	}
	
	/**
	 * Set the winner of the game
	 * @param p
	 */
	private void setWinner(Playable p) {
		this.winner = p;
	}
	
	public static void setDisplayOff() {
		displayON = false;
	}
	
	public static boolean isDisplayON() {
		return displayON;
	}
	
	public static void setStartingPlayer(Playable p) {
		startingPlayer = p;
	}
	
	/**
	 * Get the players of the game
	 * @return
	 */
	private Playable[] getPlayers() {
		return this.players;
	}
	

	/**
	 * Get the winner of the game
	 * @return
	 */
	private Playable getWinner() {
		return this.winner;
	}
	
	/**
	 * Get the player who is currently playing
	 * @return
	 */
	private Playable getPlayingPlayer() {
		return this.playingPlayer;
	}
	
	

	
}

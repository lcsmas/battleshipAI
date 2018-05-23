package mas.lucas.AI;

import java.util.ArrayList;
import java.util.List;

import mas.lucas.Case;
import mas.lucas.Ship;
import mas.lucas.Tools.Checkers;
import mas.lucas.Tools.Constants;
import mas.lucas.Tools.Utils;

public class AILvl2 extends AI {

	private List<Case> usableCaseForShipPlacement = new ArrayList<>();
	private List<Case> shootableCases = new ArrayList<>();
	private List<Case> potentialShootingCases = new ArrayList<>();
	private Case firstHitCase;
	private Case lastShotCase;
	private Case secondLastShotCase;
	private Case nextCaseToShot = null;
	private String lastAction;
	private String nextAction;
	private boolean debugMode = false; //Slow down the execution and display debug message in the console
	
	public AILvl2(int numPlayer) {
		super(numPlayer);
		usableCaseForShipPlacement = getGrid().getUnOccupiedCase();
		shootableCases = getGrid().getAllPossibleCase();
		nextAction = "shootingRandomly";
	}
	
	@Override
	protected Ship generateShip(int size) {
		List<Case> usableCase = getUsableCaseForShipPlacement();
		Case startCase = null;
		Case endCase = null;
		String way = null;
		int nbTentative;
		boolean found = false;
		while(!found) {
			nbTentative = 0;
			startCase = usableCase.get((int)(Math.random()*usableCase.size()));
			usableCase.remove(startCase);
			while(nbTentative<2) {
				way = randomWay();
				endCase = generateEndCaseFromStartCase(startCase, way, size);
				Ship s = new Ship(startCase, endCase);
				if(!this.getGrid().getOccupiedByShipCase().contains(s.getOccupiedCase())) {
					found = true;
					usableCase.removeAll(s.getOccupiedCase());
					break;
				}
				nbTentative++;
			}
		}
		
		return new Ship(startCase, endCase);
	}

	@Override
	protected Case generateHitCase() {
		if(debugMode) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		
		Case caseToShot = null;
		
		switch (getNextAction()) {
		case "shootingDefinedCase":
			caseToShot = getNextCaseToShoot();
			setLastAction("shootingDefinedCase");
			break;
		case "shootingPotentialCase":
			List<Case> potentialShootingCases = getPotentialShootingCases();
			//If there is 0 potentialShootingCase we don't break the switch and go directly to shootingRandomly case
			if(potentialShootingCases.size() != 0) {
				caseToShot = potentialShootingCases.get((int)(Math.random()*potentialShootingCases.size()));
				potentialShootingCases.remove(caseToShot);
				setLastAction("shootingPotentialCase");
				break;
			}
			
		case "shootingRandomly":
			List<Case> shootableCases = getShootableCases(); 
			caseToShot = shootableCases.get((int)(Math.random()*shootableCases.size()));
			setLastAction("shootingRandomly");
			break;
		case "shootingBackward":
			caseToShot = getNextCaseToShoot();
			setLastAction("shootingBackward");
			break;
		default:
			break;
		}
		
		if(getLastShotCase() != null) {
			setSecondLastShotCase(getLastShotCase());
		}
		setLastShotCase(caseToShot);
		updateShootableCases();
		
		if(debugMode) {
			Utils.say(getLastAction());
			Utils.say(caseToShot.toString());
		}
		
		return caseToShot;
		
	}
	
	@Override
	public void hitSuccessful() {
		super.hitSuccessful();
		
		Case c;
		
		switch (getLastAction()) {
		case "shootingRandomly":
			setNextAction("shootingPotentialCase");
			c = getLastShotCase();
			setFirstHitCase(c);
			List<Case> adjacentCases = getGrid().getAdjacentCases(c);
			List<Case> uselessAdjacentCase = new ArrayList<>();
			for(Case foo : adjacentCases) { //Remove adjacent cases that have already been shot
				if(!getShootableCases().contains(foo)) {
					uselessAdjacentCase.add(foo);
				}
			}
			adjacentCases.removeAll(uselessAdjacentCase);
			//Then update the potentials cases
			getPotentialShootingCases().addAll(adjacentCases); 
			break;
			
		case "shootingPotentialCase":
			setNextAction("shootingDefinedCase");
			getPotentialShootingCases().clear();
			setSecondLastShotCase(getFirstHitCase());
			if(findNextCaseToShoot() == null) {
				setSecondLastShotCase(getLastShotCase());
				setLastShotCase(getFirstHitCase());
				setNextCaseToShoot(findNextCaseToShoot());
				setNextAction("shootingBackward");
				break;
			}
			
			setNextCaseToShoot(findNextCaseToShoot());
			break;
			
		case "shootingBackward":
			setNextAction("shootingBackward");
			if(findNextCaseToShoot() == null) {
				setNextAction("shootingRandomly");
				break;
			}
			setNextCaseToShoot(findNextCaseToShoot());
			break;
			
		case "shootingDefinedCase": 
			setNextAction("shootingDefinedCase");
			if(findNextCaseToShoot() == null) {
				setNextAction("shootingBackward");
				setLastShotCase(getFirstHitCase());
				setNextCaseToShoot(findNextCaseToShoot());
				break;
			}
			setNextCaseToShoot(findNextCaseToShoot());
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void hitMissed() {
		super.hitMissed();
		switch (getLastAction()) {
		case "shootingBackward":
			setNextAction("shootingRandomly");
			break;
		case "shootingDefinedCase":
			setLastShotCase(getFirstHitCase());
			if(findNextCaseToShoot() != null) {
				setNextAction("shootingBackward");
				setNextCaseToShoot(findNextCaseToShoot());
				break;
			}
			setNextAction("shootingRandomly");
			break;
		case "shootingPotentialCase":
			setNextAction("shootingPotentialCase");
			break;
		case "shootingRandomly":
			setNextAction("shootingRandomly");
		default:
			break;
		}
	}
	
	/**
	 * Find the next case to shot depending on the lastShot and secondLastShot
	 * @return
	 */
	private Case findNextCaseToShoot() {
		Case c = getLastShotCase();

		switch (getDirectionOfShooting()) {
		case "r":
			if(Checkers.existInTheGrid(c.getRight(), getGrid())) {
				return c.getRight();
			}
			break;		
		case "l":
			if(Checkers.existInTheGrid(c.getLeft(), getGrid())) {
				return c.getLeft();
			}
			break;		
		case "u":
			if(Checkers.existInTheGrid(c.getUp(), getGrid())) {
				return c.getUp(); 
			}
			break;	
		case "d":
			if(Checkers.existInTheGrid(c.getDown(), getGrid())) {
				return c.getDown();
			}
			break;	
		default:
			break;
		}
		return null;
	}
	
	/**
	 * Update the shootable cases memory (remove the useless cases and cases that have been shot)
	 */
	private void updateShootableCases() {
		getShootableCases().remove(getLastShotCase());
		List<Case> uselessCases = new ArrayList<>();
		for(Case c : getShootableCases()) {
			boolean shootable = false;
			for(Case c_adj : getGrid().getAdjacentCases(c)) {
				if(c_adj.getColor() == null) {
					shootable = true;
					break;
				}
			}
			if(!shootable) {
				uselessCases.add(c);
			}
		}
		getShootableCases().removeAll(uselessCases);
	}
	
	/**
	 * Get the way of the shot (usefull for getDirectionOfShooting)
	 * @return
	 */
	private String getWayOfShooting() {
		Case lastShotCase = getLastShotCase();
		Case secondLastShotCase = getSecondLastShotCase();
		if(lastShotCase.getCol().equals(secondLastShotCase.getCol())) {
			return "v";
		} else if(lastShotCase.getLine().equals(secondLastShotCase.getLine())) {
			return "h";
		}
		return null;
	}
	
	/**
	 * Get the direction of the shot so the AI can determine the next cases
	 * @return
	 */
	private String getDirectionOfShooting() {
		Case lastShotCase = getLastShotCase();
		Case secondLastShotCase = getSecondLastShotCase();
		if(getWayOfShooting().equals("v")) {
			int lastShotLine = Integer.parseInt(lastShotCase.getLine());
			int secondLastShotLine = Integer.parseInt(secondLastShotCase.getLine());
			if( lastShotLine > secondLastShotLine ) {
				return "d"; //down
			} else {
				return "u"; //up
			}
		} else if (getWayOfShooting().equals("h")) {
			int lastShotCol = Constants.letterToNumber.get(lastShotCase.getCol());
			int secondLastShotCol = Constants.letterToNumber.get(secondLastShotCase.getCol());
			if( lastShotCol > secondLastShotCol ) {
				return "r"; //right
			} else {
				return "l"; //left
			}
		}
		return null;
	}
	
	private List<Case> getUsableCaseForShipPlacement(){
		return this.usableCaseForShipPlacement;
	}
	
	private List<Case> getShootableCases(){
		return this.shootableCases;
	}
	
	private List<Case> getPotentialShootingCases(){
		return this.potentialShootingCases;
	}
	
	private void setLastShotCase(Case c) {
		this.lastShotCase = c;
	}
	
	private Case getLastShotCase() {
		return this.lastShotCase;
	}
	
	private void setLastAction(String action) {
		this.lastAction = action;
	}
	
	private String getLastAction() {
		return this.lastAction;
	}
	
	private Case getNextCaseToShoot() {
		return this.nextCaseToShot;
	}
	
	private void setNextCaseToShoot(Case c) {
		this.nextCaseToShot = c;
	}
	
	private void setSecondLastShotCase(Case c) {
		this.secondLastShotCase = c;
	}
	
	private Case getSecondLastShotCase() {
		return this.secondLastShotCase;
	}
	
	private void setFirstHitCase(Case c) {
		this.firstHitCase = c;
	}
	
	private Case getFirstHitCase() {
		return this.firstHitCase;
	}
	
	private String getNextAction() {
		return this.nextAction;
	}
	
	private void setNextAction(String a) {
		this.nextAction = a;
	}
	
	protected String getName() {
		return "AILvL2";
	}
	
	public AILvl2 getCopy(){
		return new AILvl2(getNum());
	}
}


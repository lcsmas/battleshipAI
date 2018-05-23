package mas.lucas.AI;

import java.util.ArrayList;
import java.util.List;

import mas.lucas.Case;
import mas.lucas.Ship;

public class AILvl0 extends AI {
	
	private List<Case> usableCaseForShipPlacement = new ArrayList<>();
	
	public AILvl0(int numPlayer) {
		super(numPlayer);
		usableCaseForShipPlacement = getGrid().getUnOccupiedCase();
	}

	@Override
	protected Ship generateShip(int size) {
		List<Case> usableCase = getUsableCaseForShipPlacement();
		Ship s = null;
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
				s = new Ship(startCase, endCase);
				if(!this.getGrid().getOccupiedByShipCase().contains(s.getOccupiedCase())) {
					found = true;
					break;
				}
				nbTentative++;
			}
		}
		return s;
	}

	@Override
	protected Case generateHitCase() {
		List<Case> allPossibleCases = getGrid().getAllPossibleCase();
		return allPossibleCases.get((int)(Math.random()*allPossibleCases.size()));
	}
	
	private List<Case> getUsableCaseForShipPlacement(){
		return this.usableCaseForShipPlacement;
	}
	
	protected String getName() {
		return "AILvL0";
	}
	
	public AILvl0 getCopy(){
		return new AILvl0(getNum());
	}

	
}

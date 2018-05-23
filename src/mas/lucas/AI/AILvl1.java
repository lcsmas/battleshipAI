package mas.lucas.AI;

import java.util.ArrayList;
import java.util.List;

import mas.lucas.Case;
import mas.lucas.Ship;

public class AILvl1 extends AI {
	
	private List<Case> usableCaseForShipPlacement = new ArrayList<>();
	private List<Case> shootableCases = new ArrayList<>();
	
	public AILvl1(int numPlayer) {
		super(numPlayer);
		usableCaseForShipPlacement = getGrid().getUnOccupiedCase();
		shootableCases = getGrid().getAllPossibleCase();
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
		List<Case> shootableCases = getShootableCases();
		Case c = shootableCases.get((int)(Math.random()*shootableCases.size()));
		shootableCases.remove(c);
		return c;
	}

	private List<Case> getUsableCaseForShipPlacement(){
		return this.usableCaseForShipPlacement;
	}
	
	private List<Case> getShootableCases(){
		return this.shootableCases;
	}
	
	protected String getName() {
		return "AILvL1";
	}
	
	public AILvl1 getCopy(){
		return new AILvl1(getNum());
	}
	
}

package mas.lucas;

import java.util.ArrayList;
import java.util.List;

import mas.lucas.Tools.Constants;

public class Case {
	String col;
	String line;
	String color = null; // "g" for unhit ship, "r" for hit ship, "w" for miss
	
	public Case(String col, String line) {
		this.col = col;
		this.line = line;
	}
	
	public Case(String col, String line, String color) {
		this.col = col;
		this.line = line;
		this.color = color;
	}
	
	public void setCol(String col) {
		this.col = col;
	}
	
	public void setLine(String line) {
		this.line = line;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getCol() {
		return this.col;
	}
	
	public String getLine() {
		return this.line;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public boolean isAMiss() {
		return getColor().equals("w");
	}
	
	/**
	 * Get the case at the right of the current case in the grid
	 * @return
	 * @throws Exception
	 */
	public Case getRight() {
		int numCol = Constants.letterToNumber.get(getCol());
		numCol++;
		if(numCol<Grid.getColumns().length) {
			String col = Constants.numberToLetter.get(numCol);
			return new Case(col, getLine());
		}
		return new Case("0","0");
		
	}
	
	/**
	 * Get the case at the left of the current case in the grid
	 * @return
	 * @throws Exception
	 */
	public Case getLeft() {
		int numCol = Constants.letterToNumber.get(getCol());
		numCol--;
		if(numCol>=0) {
			String col = Constants.numberToLetter.get(numCol);
			return new Case(col, getLine());
		}
		return new Case("0","0");
	}
	
	/**
	 * Get the case under the current case in the grid
	 * @return
	 * @throws Exception
	 */
	public Case getDown() {
		int numLine = Integer.parseInt(getLine());
		numLine++;
		if(numLine < Grid.getLines().length) {
			String line = String.valueOf(numLine);
			return new Case(getCol(), line);
		}
		return new Case("0","0");
	}
	
	/**
	 * Get the case over the current case in the grid
	 * @return
	 * @throws Exception
	 */
	public Case getUp() {
		int numLine = Integer.parseInt(getLine());
		numLine--;
		if(numLine >= 0) {
			String line = String.valueOf(numLine);
			return new Case(getCol(), line);
		}
		return new Case("0","0");
	}
	

	
	@Override
	//Two case are the same if their coordinates are the same
	public boolean equals(Object obj){
	    if (obj == null) return false;
	    if (obj == this) return true;
	    if (!(obj instanceof Case))return false;
	    Case objCase = (Case)obj;
	    if(objCase.col.equals(col) && objCase.line.equals(line)) return true;	
		return false;
	}
	
	@Override
	public String toString() {
		return getCol() + getLine();
	}

	
}

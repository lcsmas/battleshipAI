package fr.battleship;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import mas.lucas.AI.AI;
import mas.lucas.*;
import mas.lucas.Tools.Utils;
public class TestIA {
	public static void main(String[] args) {
		String resultat[];
		
		writeCsv("AI Name; score; AI Name2; score2");
		goToLineCsv();
		
		resultat = testAI("0","1");
		writeCsv("AI Level Beginner;" + resultat[0] +"; Level Medium;" + resultat[1] );
		goToLineCsv();
		
		resultat = testAI("0","2");
		writeCsv("AI Level Beginner;" + resultat[0] +"; Level Hard;" + resultat[1] );
		goToLineCsv();
		
		resultat = testAI("1","2");
		writeCsv("AI Medium;" + resultat[0] +"; Level Hard;" + resultat[1] );
		goToLineCsv();
		goToLineCsv();
		
	}
	
	
	public static String[] testAI(String lvl1, String lvl2) {
		Playable ps[] = Game.initGame("IAVIA",lvl1,lvl2);
		Game.setDisplayOff();
		
		HashMap<String, Integer> victoryByPlayer = new HashMap<>();		
		victoryByPlayer.put(ps[0].toString(), 0);
		victoryByPlayer.put(ps[1].toString(), 0);
		
		AI p1 = null;
		AI p2 = null;
		for(int i=0; i<100; i++) {
			p1 = ((AI)ps[0]).getCopy();
			p2 = ((AI)ps[1]).getCopy();
			Game game = new Game(p1,p2);
			String winner = game.start();
			victoryByPlayer.put(winner, victoryByPlayer.get(winner)+1 );
			Game.switchStartingPlayer(p1, p2);
		}
		
		Utils.say(victoryByPlayer.toString());
		
		String res[] = { String.valueOf(victoryByPlayer.get( p1.toString() )),
				String.valueOf(victoryByPlayer.get(p2.toString()))
		};
		
		return res;
	}
	
	public static void writeCsv(String data) {
		try {
			FileWriter fileWriter = new FileWriter(new File("ai_proof.csv"),true);
			fileWriter.write(data);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void goToLineCsv() {
		writeCsv(System.lineSeparator());
	}
}

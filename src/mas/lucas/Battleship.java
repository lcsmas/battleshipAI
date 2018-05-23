package mas.lucas;

import mas.lucas.Tools.Utils;

public class Battleship {

	public static void main(String args[]) {
		String replay = "R";
		Game game = null;
		
		while(replay.equals("R")) {
			Playable ps[] = Game.initGame();
			
			game = new Game(ps[0], ps[1]);
			game.start();
			
			replay = Utils.ask("Press \"r\" to replay");
			
		}
	}
}

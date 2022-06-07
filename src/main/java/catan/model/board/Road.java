package catan.model.board;

import catan.model.Game;
import catan.model.player.Player;

public class Road implements Buildable {
	public Player player;  // le joueur qui a construit la route (null si aucune route n'est construite)
	
	public Road() {
		player = null;
	}
	
	public void printH() {
		if (player == null) System.out.print("       ");
		else System.out.print("-------");
	}
	
	public void printV() {
		if (player == null) System.out.print(" ");
		else System.out.print("|");
	}

	@Override
	public void build(Game game, Player p, int type) {
		// on construit la route
		player = p;
		
		p.hasBuiltARoad();

		// on calcule quelle est la longueur de cette route
		int n = game.getBoard().calculateRoadLength(this);
		
		// on stocke sa nouvelle longueur
		p.longestRoad = n;

		// si elle est plus longue que la route la plus longue pr�c�dente
		if (n > 5 && n > p.longestRoad) {

			// on r�attribue la route la plus longue au cas-o� le joueur qui la poss�de
			// change
			game.assignLongestRoad();
		}
	}
}

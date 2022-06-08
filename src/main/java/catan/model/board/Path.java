package catan.model.board;

import catan.model.Game;
import catan.model.player.Human;

public class Path implements Buildable {
	public Construction construction;
	public Human player;
	
	public Path() {
		construction = Construction.NOTHING;
		player = null;
	}
	
	public void printH() {
		if (construction == Construction.NOTHING) System.out.print("       ");
		else System.out.print("-------");
	}
	
	public void printV() {
		if (construction == Construction.NOTHING) System.out.print(" ");
		else System.out.print("|");
	}

	@Override
	public void build(Game game, Human p, Construction c) {
		if (c != Construction.ROAD) return;
		
		construction = c;
		player = p;
		p.hasBuiltARoad();

		// on calcule quelle est la longueur de cette route
		int n = game.getBoard().calculateRoadLength(this);
		
		// on stocke sa nouvelle longueur
		p.longestRoad = n;

		// si elle est plus longue que la route la plus longue precedente
		if (n > 5 && n > p.longestRoad) {

			// on reattribue la route la plus longue au cas-ou le joueur qui la possede change
			game.assignLongestRoad();
		}
	}
}

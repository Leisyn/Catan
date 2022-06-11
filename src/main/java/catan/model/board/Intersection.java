package catan.model.board;

import java.util.LinkedList;

import catan.model.Game;
import catan.model.board.Harbor.HarborType;
import catan.model.player.Player;

public class Intersection implements Buildable {
	public Construction construction;
	public Player player;

	public Intersection() {
		construction = Construction.NOTHING;
		player = null;
	}

	@Override
	public String toString() {
		if (construction == Construction.NOTHING) return " ";
		if (construction == Construction.SETTLEMENT) return "*";
		return "#";
	}

	public boolean isBuilt() {
		return construction != Construction.NOTHING;
	}

	@Override
	public void build(Game game, Player p, Construction c) {
		if (c != Construction.SETTLEMENT && c != Construction.CITY) return;
		
		// on regarde si le joueur construit une ville
		if (c == Construction.CITY) {
			// on construit l'intersection
			player = p;
			construction = c;
			
			p.builtACity();
			return;
		}

		// on regarde si le joueur construit une colonie

		// on r�cup�re les 4 routes environnantes
		Path[] r = game.getBoard().getAllPathsInContactWith(this);

		LinkedList<Player> ontLeurRouteLaPlusLongueBrise = new LinkedList<>();

		// on regarde si une de ces routes appartient � un adversaire
		for (int i = 0; i < r.length; i++) {
			if (r[i] != null && r[i].player != null && r[i].player != p) {

				// on regarde si une autre de ces routes appartient � cette adversaire
				for (int k = i; k < r.length; k++) {
					if (r[k].player == r[i].player) {

						// si ces 2 routes forment sa route la plus longue, on note que sa route la plus
						// longue sera brisee
						if (game.getBoard().calculateRoadLength(r[i]) == r[i].player.getPersonalLongestRoad())
							ontLeurRouteLaPlusLongueBrise.add(r[i].player);
					}
				}
			}
		}

		// on construit l'intersection
		player = p;
		construction = c;

		p.builtASettlement();

		// on regarde si un port est en contact
		if (game.getBoard().isInContactWithAHarbor(this)) {
			HarborType typePort = game.getBoard().getHarborInContactWith(this).harborType;
			p.changeTradeRate(typePort);
		}

		// pour chaque joueur qui ont eu leur route la plus longue bris�e, on calcule
		// leur nouvelle route la plus longue
		for (Player jo : ontLeurRouteLaPlusLongueBrise)
			jo.setPersonalLongestRoad(Math.max(jo.getPersonalLongestRoad(), game.getBoard().calculateLongestRoad(jo)));

		// on r�attribue la route la plus longue au cas ou le joueur qui la possede a
		// change
		game.assignLongestRoad();

	}
}

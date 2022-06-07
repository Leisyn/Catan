package catan.model.board;

import java.util.LinkedList;

import catan.model.Game;
import catan.model.player.Player;

public class Intersection implements Buildable {
	public Player player; // le joueur qui a construit un batiment sur l'intersection (null si rien n'est
							// construit)
	public int building; // 0 : rien / 1 : colonie / 2 : ville

	public Intersection() {
		building = 0;
		player = null;
	}

	@Override
	public String toString() {
		if (building == 0) return " ";
		if (building == 1) return "*";
		return "#";
	}

	public boolean isBuilt() {
		return building != 0;
	}

	@Override
	public void build(Game game, Player p, int type) {
		if (type == 0)
			return;
		
		// on regarde si le joueur construit une ville
		if (type == 2) {
			// on construit l'intersection
			player = p;
			building = type;
			
			p.hasBuiltACity();

			// on ajoute les points au joueur
			p.points += type;

			return;
		}

		// on regarde si le joueur construit une colonie

		// on r�cup�re les 4 routes environnantes
		Road[] r = game.getBoard().getAllRoadsInContactWith(this);

		LinkedList<Player> ontLeurRouteLaPlusLongueBrise = new LinkedList<>();

		// on regarde si une de ces routes appartient � un adversaire
		for (int i = 0; i < r.length; i++) {
			if (r[i] != null && r[i].player != null && r[i].player != p) {

				// on regarde si une autre de ces routes appartient � cette adversaire
				for (int k = i; k < r.length; k++) {
					if (r[k].player == r[i].player) {

						// si ces 2 routes forment sa route la plus longue, on note que sa route la plus
						// longue sera brisee
						if (game.getBoard().calculateRoadLength(r[i]) == r[i].player.longestRoad)
							ontLeurRouteLaPlusLongueBrise.add(r[i].player);
					}
				}
			}
		}

		// on construit l'intersection
		player = p;
		building = type;

		p.hasBuiltASettlement();
		
		// on ajoute les points au joueur
		p.points += type;

		// on regarde si un port est en contact
		if (game.getBoard().isInContactWithAHarbor(this)) {
			int typePort = game.getBoard().getHarborInContactWith(this).typeHarbor;
			p.changeTradeRate(typePort);
		}

		// pour chaque joueur qui ont eu leur route la plus longue bris�e, on calcule
		// leur nouvelle route la plus longue
		for (Player jo : ontLeurRouteLaPlusLongueBrise)
			jo.longestRoad = Math.max(jo.longestRoad, game.getBoard().calculateLongestRoad(jo));

		// on r�attribue la route la plus longue au cas ou le joueur qui la possede a
		// change
		game.assignLongestRoad();

	}
}

package catan.model.card;

import catan.model.Game;
import catan.model.player.Human;

public class KnightCard extends Card {

	public KnightCard() {
		super(0, "chevalier");
	}

	public void jouer(Game game, Human p) {
		// on augmente l'arm�e la plus puissante du joueur
		p.largestArmy++;
		
		// on r�attribue l'arm�e la plus puissante si besoin
		game.assignLargestArmy();
		
		// on d�place le voleur
		game.onTheRobber(p);
	}

	@Override
	public String toString() {
		return "Carte Chevalier - Chevalier";
	}
}


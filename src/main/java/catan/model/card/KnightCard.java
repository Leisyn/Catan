package catan.model.card;

import catan.model.Game;
import catan.model.player.Player;

public class KnightCard extends Card {

	public KnightCard() {
		super(0, "chevalier");
	}

	public void jouer(Game game, Player p) {
		// on augmente l'arm�e la plus puissante du joueur
		p.usedAKnightCard();
		
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


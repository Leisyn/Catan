package catan.model.card;

import catan.model.Game;
import catan.model.other.CardName;
import catan.model.player.Player;

public class KnightCard extends Card {

	public KnightCard() {
		super(CardName.KNIGHT);
	}

	public void play(Game game, Player p) {
		// on augmente l'armee la plus puissante du joueur
		p.usedAKnightCard();
		
		// on r�attribue l'armee la plus puissante si besoin
		game.assignLargestArmy();
		
		// on deplace le voleur
		game.onTheRobber(p);
	}

	@Override
	public String toString() {
		return "Knight Card";
	}
}


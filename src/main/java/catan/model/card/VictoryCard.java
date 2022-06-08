package catan.model.card;

import catan.model.Game;
import catan.model.player.Player;

public class VictoryCard extends Card {

	public VictoryCard(CardName name) {
		super(name);
	}

	@Override
	public void jouer(Game game, Player p) {}

	@Override
	public String toString() {
		if (name == CardName.MARKET) return "Victory Card - Market";
		if (name == CardName.UNIVERSITY) return "Victory Card - University";
		if (name == CardName.GREATHALL) return "Victory Card - Great Hall";
		if (name == CardName.CHAPEL) return "Victory Card - Chapel";
		return "Victory Card - Library";
	}
}


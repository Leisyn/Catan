package catan.model.card;

import catan.model.Game;
import catan.model.other.CardName;
import catan.model.player.Player;

public abstract class Card {
	public CardName name;
	
	public Card(CardName name) {
		this.name = name;
	}
	
	public abstract void play(Game game, Player p);
	public abstract String toString();
}


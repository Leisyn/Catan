package catan.model.card;

import catan.model.Game;
import catan.model.player.Player;

public abstract class Card {
	public enum CardName{KNIGHT, ROADBUILDING, YEAROFPLENTY, MONOPOLY, MARKET, UNIVERSITY, GREATHALL, CHAPEL, LIBRARY};
	
	public CardName name;
	
	public Card(CardName name) {
		this.name = name;
	}
	
	public abstract void play(Game game, Player p);
	public abstract String toString();
}


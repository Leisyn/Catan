package catan.model.card;

import catan.model.Game;
import catan.model.player.Player;

public abstract class Card {
	protected int type;  // 0 : chevalier / 1 : progres / 2 : victoire
	public String name;
	
	static String[] acceptedName = {"chevalier", "construction", "invention",
			"monopole", "bibliotheque", "marche", "parlement", "eglise", "universite"};
	
	
	public Card(int type, String name) {
		if (type < 0 || type > 2)
			throw new IllegalArgumentException("Can't create the card: unknown type");
		
		boolean nomCorrect = false;
		for (String s : acceptedName) {
			if (s.equals(name))
				nomCorrect = true;
		}
		if (nomCorrect == false)
			throw new IllegalArgumentException("Can't create the card: unknown name");
		
		
		this.type = type;
		this.name = name;
	}
	
	public abstract void jouer(Game game, Player p);
	public abstract String toString();
}


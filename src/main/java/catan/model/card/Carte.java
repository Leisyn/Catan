package catan.model.card;

import catan.model.Jeu;
import catan.model.player.Joueur;

public abstract class Carte {
	protected int type;  // 0 : chevalier / 1 : progres / 2 : victoire
	public String nom;
	
	static String[] nomAccepte = {"chevalier", "construction", "invention",
			"monopole", "bibliotheque", "marche", "parlement", "eglise", "universite"};
	
	
	public Carte(int type, String nom) {
		if (type < 0 || type > 2)
			throw new IllegalArgumentException("Impossible de construire la carte : type non reconnu.");
		
		boolean nomCorrect = false;
		for (String s : nomAccepte) {
			if (s.equals(nom))
				nomCorrect = true;
		}
		if (nomCorrect == false)
			throw new IllegalArgumentException("Impossible de construire la carte : nom incorrect.");
		
		
		this.type = type;
		this.nom = nom;
	}
	
	public abstract void jouer(Jeu jeu, Joueur j);
	public abstract String toString();
}


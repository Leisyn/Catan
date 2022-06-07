package catan.model.card;

import catan.model.Jeu;
import catan.model.player.Joueur;

public class CarteChevalier extends Carte {

	public CarteChevalier() {
		super(0, "chevalier");
	}

	public void jouer(Jeu jeu, Joueur j) {
		// on augmente l'arm�e la plus puissante du joueur
		j.armeeLaPlusPuissante++;
		
		// on r�attribue l'arm�e la plus puissante si besoin
		jeu.giveArmeeLaPlusPuissante();
		
		// on d�place le voleur
		jeu.auVoleur(j);
	}

	@Override
	public String toString() {
		return "Carte Chevalier - Chevalier";
	}
}


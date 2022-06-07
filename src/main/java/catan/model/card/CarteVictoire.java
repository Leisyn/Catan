package catan.model.card;

import catan.model.Jeu;
import catan.model.player.Joueur;

public class CarteVictoire extends Carte {

	public CarteVictoire(String nom) {
		super(2, nom);
	}

	@Override
	public void jouer(Jeu jeu, Joueur j) {
		return;
	}

	@Override
	public String toString() {
		if (this.nom.equals("bibliotheque")) {
			return "CarteVictoire - Biblioth�que";
		}
		
		if (this.nom.equals("marche")) {
			return "CarteVictoire - Place du march�";
		}
		
		if (this.nom.equals("parlement")){
			return "CarteVictoire - Parlement";
		}
		
		if (this.nom.equals("eglise")) {
			return "CarteVictoire - �glise";
		}
		
		return "CarteVictoire - Universit�";
	}
}



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
			return "CarteVictoire - Bibliothèque";
		}
		
		if (this.nom.equals("marche")) {
			return "CarteVictoire - Place du marché";
		}
		
		if (this.nom.equals("parlement")){
			return "CarteVictoire - Parlement";
		}
		
		if (this.nom.equals("eglise")) {
			return "CarteVictoire - Église";
		}
		
		return "CarteVictoire - Université";
	}
}


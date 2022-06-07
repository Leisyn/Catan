
public class CarteChevalier extends Carte {

	public CarteChevalier() {
		super(0, "chevalier");
	}

	public void jouer(Jeu jeu, Joueur j) {
		// on augmente l'armée la plus puissante du joueur
		j.armeeLaPlusPuissante++;
		
		// on réattribue l'armée la plus puissante si besoin
		jeu.giveArmeeLaPlusPuissante();
		
		// on déplace le voleur
		jeu.auVoleur(j);
	}

	@Override
	public String toString() {
		return "Carte Chevalier - Chevalier";
	}
}



public class CarteProgres extends Carte {

	public CarteProgres(String nom) {
		super(1, nom);
	}

	public void jouer(Jeu jeu, Joueur j) {
		// s'il s'agit d'une carte construction
		if (this.nom.equals("construction")) {
			
			// on construit 2 routes
			j.construire(0, false);
			j.construire(0, false);
		}
		
		// s'il s'agit d'une carte invention
		if (this.nom.equals("invention")) {
			
			// on demande la ressource que le joueur veut recevoir et on lui donne
			String ressourceDemandee = null;
			while (ressourceDemandee == null) {
				ressourceDemandee = j.demandeRessourceARecevoir(1);
			}
			
			// on donne au joueur la ressource qu'il a souhaité
			j.obtient(ressourceDemandee, 1);

			
			// on demande la 2e ressource que le joueur veut recevoir et on lui donne
			String ressourceDemandee2 = null;
			while (ressourceDemandee2 == null) {
				ressourceDemandee2 = j.demandeRessourceARecevoir(1);
			}
			
			// on donne au joueur la ressource qu'il a souhaité
			j.obtient(ressourceDemandee2, 1);
		}
	
		// s'il s'agit d'une carte monopole
		if (this.nom.equals("monopole")) {

			// on demande la ressource que le joueur veut recevoir
			String ressourceDemandee = null;
			while (ressourceDemandee == null) {
				ressourceDemandee = j.demandeRessourceARecevoir(1);
			}
			
			// on donne au joueur la ressource qu'il a souhaité
			j.obtient(ressourceDemandee, 1);		

			// on compte et on retire toutes les ressources de ce type que les autres joueurs ont
			int cmpt = 0;
			int cmpt2 = 0;
			for (int i = 0; i < jeu.getJoueurs().length; i++) {
				cmpt2 = cmpt2 + jeu.getJoueurs()[i].getRessources().get(ressourceDemandee);
				cmpt = jeu.getJoueurs()[i].getRessources().get(ressourceDemandee);
	        	jeu.getJoueurs()[i].perd(ressourceDemandee, cmpt);
			}
			
			// on donne le nombre compté au joueur
			j.obtient(ressourceDemandee, cmpt2);
		}

	}

	@Override
	public String toString() {
		if (this.nom.equals("construction")) {
			return "Carte Progrès - Construction de Routes";
		}
		
		if (this.nom.equals("invention")) {
			return "Carte Progrès - Invention";
		}
		
		return "Carte Progrès - Monopole";
	}
}


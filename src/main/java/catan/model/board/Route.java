package catan.model.board;

import catan.model.Jeu;
import catan.model.player.Joueur;

public class Route implements IConstructible {
	public Joueur joueur;  // le joueur qui a construit la route (null si aucune route n'est construite)
	
	public Route() {
		joueur = null;
	}
	
	public void afficheH() {
		if (joueur == null)
			System.out.print("       ");
		
		else {
			
			System.out.print("-------");
		}
	}
	
	public void afficheV() {
		if (joueur == null)
			System.out.print(" ");
		
		else
			System.out.print("|");
	}

	@Override
	public void construire(Jeu jeu, Joueur j, int type) {
		// on construit la route
		joueur = j;
		
		j.aConstruitRoute();

		// on calcule quelle est la longueur de cette route
		int n = jeu.getPlateau().calculeLongueurRoute(this);
		
		// on stocke sa nouvelle longueur
		j.routeLaPlusLongue = n;

		// si elle est plus longue que la route la plus longue pr�c�dente
		if (n > 5 && n > j.routeLaPlusLongue) {

			// on r�attribue la route la plus longue au cas-o� le joueur qui la poss�de
			// change
			jeu.giveRouteLaPlusLongue();
		}
	}
}

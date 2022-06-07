import java.util.LinkedList;

public class Intersection implements IConstructible {
	public Joueur joueur; // le joueur qui a construit un batiment sur l'intersection (null si rien n'est
							// construit)
	public int batiment; // 0 : rien / 1 : colonie / 2 : ville

	public Intersection() {
		batiment = 0;
		joueur = null;
	}

	@Override
	public String toString() {
		if (batiment == 0)
			return " ";

		if (batiment == 1)
			return "*";

		return "#";
	}

	public boolean estConstruit() {
		return batiment != 0;
	}

	@Override
	public void construire(Jeu jeu, Joueur j, int type) {
		if (type == 0)
			return;
		
		// on regarde si le joueur construit une ville
		if (type == 2) {
			// on construit l'intersection
			joueur = j;
			batiment = type;
			
			j.aConstruitVille();

			// on ajoute les points au joueur
			j.points += type;

			return;
		}

		// on regarde si le joueur construit une colonie

		// on récupère les 4 routes environnantes
		Route[] r = jeu.getPlateau().getAllRoutes(this);

		LinkedList<Joueur> ontLeurRouteLaPlusLongueBrise = new LinkedList<>();

		// on regarde si une de ces routes appartient à un adversaire
		for (int i = 0; i < r.length; i++) {
			if (r[i] != null && r[i].joueur != null && r[i].joueur != j) {

				// on regarde si une autre de ces routes appartient à cette adversaire
				for (int k = i; k < r.length; k++) {
					if (r[k].joueur == r[i].joueur) {

						// si ces 2 routes forment sa route la plus longue, on note que sa route la plus
						// longue sera brisee
						if (jeu.getPlateau().calculeLongueurRoute(r[i]) == r[i].joueur.routeLaPlusLongue)
							ontLeurRouteLaPlusLongueBrise.add(r[i].joueur);
					}
				}
			}
		}

		// on construit l'intersection
		joueur = j;
		batiment = type;

		j.aConstruitColonie();
		
		// on ajoute les points au joueur
		j.points += type;

		// on regarde si un port est en contact
		if (jeu.getPlateau().enContactAvecPort(this)) {
			int typePort = jeu.getPlateau().getPort(this).typePort;
			j.changeTauxEchange(typePort);
		}

		// pour chaque joueur qui ont eu leur route la plus longue brisée, on calcule
		// leur nouvelle route la plus longue
		for (Joueur jo : ontLeurRouteLaPlusLongueBrise)
			jo.routeLaPlusLongue = Math.max(jo.routeLaPlusLongue, jeu.getPlateau().calculeRouteLaPlusLongue(jo));

		// on réattribue la route la plus longue au cas ou le joueur qui la possede a
		// change
		jeu.giveRouteLaPlusLongue();

	}
}

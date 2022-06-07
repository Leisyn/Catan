package catan.model.board;

import java.util.LinkedList;
import java.util.Scanner;

import catan.model.Jeu;
import catan.model.other.Paire;
import catan.model.player.Joueur;

public class Plateau {
	private Tuile[][] tuiles;
	private Route[][] routes;
	private Intersection[][] intersections;

	// Cree le plateau de jeu, de taille 7x7
	public Plateau() {
		tuiles = new Tuile[7][7];
		routes = new Route[13][7];
		intersections = new Intersection[6][6];
	}

	public Tuile[][] getTuiles() {
		return tuiles;
	}
	
	public Route[][] getRoutes() {
		return routes;
	}
	
	public Intersection[][] getIntersections() {
		return intersections;
	}
	
	public Paire getXY(Route r) {
		for (int i = 0; i < routes.length; i++) {
			for (int j = 0; j < routes[i].length; j++) {
				if (routes[i][j] == r)
					return new Paire(i, j);
			}
		}
		
		return null;
	}
	
	// Renvoie les 4 routes en contact avec l'intersection donn�e dans l'ordre : NO, NE, SO, SE
	public Tuile[] getAllTuiles(Intersection in) {
		if (in == null)
			throw new IllegalArgumentException("Intersection �gale � null");

		// on cherche l'intersection donnee
		for (int i = 0; i < intersections.length; i++) {
			for (int j = 0; j < intersections[i].length; j++) {
				if (intersections[i][j] == in) {
					Tuile[] res = new Tuile[4];
					
					// on ajoute les 4 tuiles en contact avec l'intersection
					res[0] = tuiles[i][j];
					res[1] = tuiles[i][j + 1];
					res[2] = tuiles[i + 1][j];
					res[3] = tuiles[i + 1][j + 1];
					
					return res;
				}
			}
		}
		
		// l'intersection n'a pas �t� trouv�e
		return null;
	}
	
	public Port getPort(Intersection in) {
		Tuile[] t = getAllTuiles(in);
		for (int i = 0; i < t.length; i++) {
			if (t[i] instanceof Port)
				return (Port)t[i];
		}
		
		return null;
	}

	// Renvoie les 4 routes menant � l'intersection donn�e dans l'ordre : N, S, O, E
	public Route[] getAllRoutes(Intersection in) {
		if (in == null)
			throw new IllegalArgumentException("Intersection �gale � null");

		// on cherche l'intersection donnee
		for (int i = 0; i < intersections.length; i++) {
			for (int j = 0; j < intersections[i].length; j++) {
				if (intersections[i][j] == in) {
					Route[] res = new Route[4];
					int m = i * 2 + 1;
					int n = j;

					// on ajoute les 4 routes menant a cette intersections
					res[0] = routes[m][n];
					res[1] = routes[m - 1][n];
					res[2] = routes[m][n + 1];
					res[3] = routes[m + 1][n];

					return res;
				}
			}
		}

		return null;
	}

	// Renvoie les 6 routes en contact � la route donn�e dans l'ordre : O, NO, SO,
	// E, NE, SE ou N, NO, NE, S, SO, SE
	public Route[] getAllRoutes(Route r) {
		if (r == null)
			throw new IllegalArgumentException("Route �gale � null");

		// on cherche la route donn�e
		for (int i = 1; i < routes.length - 1; i++) {
			for (int j = 0; j < routes[i].length - 1; j++) {
				if (routes[i][j] == r) {
					Route[] res = new Route[6];

					// s'il s'agit d'une route horizontale
					if (i % 2 == 1) {

						// on cherche la route � l'ouest
						res[0] = routes[i][j - 1];

						// on cherche la route au nord-ouest
						res[1] = routes[i - 1][j - 1];

						// on cherche la route au sud-ouest
						res[2] = routes[i + 1][j - 1];

						// on cherche la route � l'est
						res[3] = routes[i][j + 1];

						// on cherche la route au nord-est
						res[4] = routes[i - 1][j];

						// on cherche la route au sud-est
						res[5] = routes[i + 1][j];

						// on renvoie les 6 routes trouv�es
						return res;
					}

					// s'il s'agit d'une route verticale
					else {
						// on cherche la route au nord
						res[0] = routes[i - 2][j];

						// on cherche la route au nord-ouest
						res[1] = routes[i - 1][j];

						// on cherche la route au nord-est
						res[2] = routes[i - 1][j + 1];

						// on cherche la route au sud
						res[3] = routes[i + 2][j];

						// on cherche la route au sud-ouest
						res[4] = routes[i + 1][j];

						// on cherche la route au sud-est
						res[5] = routes[i + 1][j + 1];

						// on renvoie les 6 routes trouv�es
						return res;
					}
				}
			}
		}

		// on n'a pas trouv� la route donn�e
		return null;
	}

	// Renvoie les deux intersections se trouvant aux bouts de la route
	public Intersection[] getAllIntersections(Route r) {
		if (r == null)
			throw new IllegalArgumentException("Route �gale � null");

		// on cherche la route donn�e
		for (int i = 1; i < routes.length - 1; i++) {
			for (int j = 0; j < routes[i].length - 1; j++) {
				if (routes[i][j] == r) {

					// on regarde s'il s'agit d'une route hozizontale
					if (i % 2 == 1) {
						// on renvoie les intersections nord et sud
						Intersection inO = intersections[i / 2][j - 1];
						Intersection inE = intersections[i / 2][j];

						Intersection[] res = {inO, inE};
						return res;
					}

					// sinon, il s'agit d'une route verticale

					// on renvoie les intersections ouest et est
					Intersection inN = intersections[i / 2 - 1][j];
					Intersection inS = intersections[i / 2][j];

					Intersection[] res = {inN, inS};
					return res;
				}
			}
		}

		// on n'a pas trouv� la route
		return null;
	}

	// Renvoie la position ou se trouve la tuile demandee
	public Paire position(String typeTuile, int jeton) {
		if (jeton < 2 || jeton > 12)
			return null;

		int type;
		switch (typeTuile.toUpperCase()) {
			case "PR": type = 2; break;
			case "FO": type = 3; break;
			case "CO": type = 4; break;
			case "CH": type = 5; break;
			case "MO": type = 6; break;
			case "DES": type = 7;
			default: type = -1;
		}

		if (type == -1)
			return null;

		for (int i = 0; i < tuiles.length; i++) {
			for (int j = 0; j < tuiles[i].length; j++) {
				if (tuiles[i][j].type == type && tuiles[i][j].jeton == jeton)
					return new Paire(i, j);
			}
		}
		return null;
	}

	// Retire le voleur du plateau
	public void retireVoleur() {
		for (int i = 1; i < tuiles.length - 1; i++) {
			for (int j = 1; j < tuiles[i].length - 1; j++) {
				if (tuiles[i][j].voleurEstIci) {
					tuiles[i][j].voleurEstIci = false;
					return;
				}
			}
		}
	}

	// Place le voleur a la position donnee
	public void placeVoleur(Paire p) {
		tuiles[p.x][p.x].voleurEstIci = true;
	}
	
	public void placeVoleur(Tuile t) {
		for (int i = 1; i < tuiles.length - 1; i++) {
			for (int j = 1; j < tuiles[i].length - 1; j++) {
				if (tuiles[i][j] == t) {
					tuiles[i][j].voleurEstIci = true;
					return;
				}
			}
		}
	}

	public boolean respecteRegleDistance(Intersection in) {
		if (in == null)
			throw new IllegalArgumentException("Intersection null.");
		
		// on cherche l'intersection en question
		for (int i = 0; i < intersections.length; i++) {
			for (int j = 0; j < intersections[i].length; j++) {

				// lorsqu'on l'a trouve
				if (intersections[i][j] == in) {

					// on regarde si l'intersection nord est construite
					if (i > 0 && intersections[i - 1][j] != null && intersections[i - 1][j].joueur != null)
						return false;

					// on regarde si l'intersection sud est construite
					if (i < intersections.length - 1 && intersections[i + 1][j] != null && intersections[i + 1][j].joueur != null)
						return false;

					// on regarde si l'intersection ouest est construite
					if (j > 0 && intersections[i][j - 1] != null && intersections[i][j - 1].joueur != null)
						return false;

					// on regarde si l'intersection est est construite
					if (j < intersections[i].length - 1 && intersections[i][j + 1] != null && intersections[i][j + 1].joueur != null)
						return false;

					// si aucune des intersections autour n'est construite, alors la regle de
					// distance est respectee
					return true;
				}
			}
		}

		// l'intersection en question n'a pas ete trouvee
		return false;
	}
	
	public boolean enContactAvecPort(Intersection in) {
		Tuile[] tuilesContact = getAllTuiles(in);
		
		for (int i = 0; i < tuilesContact.length; i++) {
			if (tuilesContact[i] instanceof Port)
				return true;
		}
		
		return false;
	}
	
	public IConstructible getConstruction(Paire positionTuile, String direction) {
		if (direction.equals("N") || direction.equals("S") || direction.equals("O") || direction.equals("E"))
			return tuiles[positionTuile.x][positionTuile.y].getRoute(direction);
		
		return tuiles[positionTuile.x][positionTuile.y].getIntersection(direction);
	}

	public void construire(Jeu jeu, IConstructible construction, Joueur j, int type) {
		construction.construire(jeu, j, type);
	}

	// Calcule la route la plus longue du joueur donn�
	public int calculeRouteLaPlusLongue(Joueur joueur) {
		int max = 0;
		for (int i = 1; i < routes.length - 1; i++) {
			for (int j = 0; j < routes[i].length - 1; j++) {
				if (routes[i][j] != null && routes[i][j].joueur == joueur)
					max = Math.max(max, calculeLongueurRoute(routes[i][j]));
			}
		}
		return max;
	}

	// Calcule la longueur de la route de la route donn�e
	public int calculeLongueurRoute(Route r) {
		boolean[][] routesVu = new boolean[routes.length][routes[0].length];
		return calculeLongueurRouteAux(routesVu, r.joueur, r, 0);
	}

	private int calculeLongueurRouteAux(boolean[][] routesVu, Joueur joueur, Route r, int n) {
		// on regarde s'il s'agit d'une route existante
		if (r == null)
			return n;

		Paire p = getXY(r);
		if (p == null || routesVu[p.x][p.y])
			return n;
		
		routesVu[p.x][p.y] = true;
		
		// on regarde si la route appartient au joueur
		if (r.joueur != joueur)
			return n;

		// on r�cup�re les intersections au bout de la route
		Intersection[] in = getAllIntersections(r);

		// on regarde si des batiments adverses bloquent les deux intersections
		if ((in[0].batiment != 0 && in[0].joueur != joueur) || (in[1].batiment != 0 && in[1].joueur != joueur))
			return n;

		// on r�cup�re les 6 routes li�es � la route actuelle
		Route[] routes = getAllRoutes(r);
		int[] max = new int[2];
		int m;

		// si aucun batiment adverse ne bloque la 1er intersection
		if (in[0].batiment == 0 || in[0].joueur == joueur) {

			// on r�cup�re la route la plus longue a partir de la route � l'ouest / au nord
			// et on la compare avec les deux routes les plus longues pr�c�dentes
			m = calculeLongueurRouteAux(routesVu, joueur, routes[0], n);
			if (m > max[1]) {
				if (m < max[0])
					max[1] = m;
				
				else {
					max[1] = max[0];
					max[0] = m;
				}
			}

			// on r�cup�re la route la plus longue a partir de la route au nord-ouest et on
			// la compare avec les deux routes les plus longues pr�c�dentes
			m = calculeLongueurRouteAux(routesVu, joueur, routes[1], n);
			if (m > max[1]) {
				if (m < max[0])
					max[1] = m;
				
				else {
					max[1] = max[0];
					max[0] = m;
				}
			}

			// on r�cup�re la route la plus longue a partir de la route au sud-ouest / au
			// nord-est et on la compare avec les deux routes les plus longues pr�c�dentes
			m = calculeLongueurRouteAux(routesVu, joueur, routes[2], n);
			if (m > max[1]) {
				if (m < max[0])
					max[1] = m;
				
				else {
					max[1] = max[0];
					max[0] = m;
				}
			}
		}

		// si aucun batiment adverse ne bloque la 2e intersection
		if (in[1].batiment == 0 || in[1].joueur == joueur) {

			// on r�cup�re la route la plus longue a partir de la route � l'est / au sud et
			// on la compare avec les deux routes les plus longues pr�c�dentes
			m = calculeLongueurRouteAux(routesVu, joueur, routes[3], n);
			if (m > max[1]) {
				if (m < max[0])
					max[1] = m;
				
				else {
					max[1] = max[0];
					max[0] = m;
				}
			}

			// on r�cup�re la route la plus longue a partir de la route au nord-est / au
			// sud-ouest et on la compare avec les deux routes les plus longues pr�c�dentes
			m = calculeLongueurRouteAux(routesVu, joueur, routes[4], n);
			if (m > max[1]) {
				if (m < max[0])
					max[1] = m;
				
				else {
					max[1] = max[0];
					max[0] = m;
				}
			}

			// on r�cup�re la route la plus longue a partir de la route au sud-est, on la
			// compare avec les deux routes les plus longues pr�c�dentes
			m = calculeLongueurRouteAux(routesVu, joueur, routes[5], n);
			if (m > max[1]) {
				if (m < max[0])
					max[1] = m;
				
				else {
					max[1] = max[0];
					max[0] = m;
				}
			}
		}

		// on renvoie la somme entre les deux routes les plus longues calcul�es
		return max[0] + max[1] + 1;
	}

	public void affichePlateau() {
		System.out.println("---------------------------------------------------------");
		for (int i = 0; i < tuiles.length; i++) {
			afficheNomTuile(i);
			afficheDetailTuile(i);
			if (i != tuiles.length - 1) // il n'y a pas de routes et intersections sous la derniere ligne de tuile
				afficheRouteEtIntersection(i);
		}
		System.out.println("---------------------------------------------------------");
	}

	private void afficheNomTuile(int i) {
		System.out.print("|");
		for (int j = 0; j < tuiles[i].length; j++) {
			tuiles[i][j].afficheNom();
			;

			if (j != tuiles[i].length - 1) {
				if (tuiles[i][j].rouE == null)
					System.out.print(" ");
				else
					tuiles[i][j].rouE.afficheV();
			}
		}
		System.out.println("|");
	}

	private void afficheDetailTuile(int i) {
		System.out.print("|");
		for (int j = 0; j < tuiles[i].length; j++) {
			tuiles[i][j].afficheDetail();

			if (j != tuiles[i].length - 1) {
				if (tuiles[i][j].rouE == null)
					System.out.print(" ");
				else
					tuiles[i][j].rouE.afficheV();
			}
		}
		System.out.println("|");
	}

	private void afficheRouteEtIntersection(int i) {
		System.out.print("|");
		for (int j = 0; j < tuiles[i].length; j++) {
			if (tuiles[i][j].rouS == null)
				System.out.print("       ");
			else
				tuiles[i][j].rouS.afficheH();

			if (j != tuiles[i].length - 1) {
				if (tuiles[i][j].interSE == null)
					System.out.print(" ");
				else
					System.out.print(tuiles[i][j].interSE);
			}
		}
		System.out.println("|");
	}

	// Initialise un plateau
	public static Plateau iniPlateau() {
		Plateau res = new Plateau();
		iniRoutes(res.routes);
		iniIntersections(res.intersections);
		iniTuiles(res.tuiles, res.routes, res.intersections);
		return res;
	}

	// Initialise les tuiles
		private static void iniTuiles(Tuile[][] t, Route[][] r, Intersection[][] in) {
			
			// on initialises les tuiles elles-memes
			for (int i = 0; i < t.length; i++) {
				for (int j = 0; j < t[i].length; j++) {
					if (i == 0) {
						switch(j) {
							case 2: t[i][j] = new Port(0); break;
							case 4: t[i][j] = new Port(1); break;
							default: t[i][j] = new Tuile(0, false); break;
						}
					} else if (i == 1) {
						switch(j) {
							case 2: t[i][j] = new Tuile(3, 6, false); break;
							case 3: t[i][j] = new Tuile(2, 3, false); break;
							case 4: t[i][j] = new Tuile(5, 8, false); break;
							default : t[i][j] = new Tuile(0, false); break;
						}
					} else if (i == 2) {
						switch(j) {
							case 0: t[i][j] = new Port(5); break;
							case 1: t[i][j] = new Tuile(2, 2, false); break;
							case 2: t[i][j] = new Tuile(4, 4, false); break;
							case 3: t[i][j] = new Tuile(6, 5, false); break;
							case 4: t[i][j] = new Tuile(3, 9, false); break;
							case 5: t[i][j] = new Tuile(6, 11, false); break;
							case 6: t[i][j] = new Port(0); break;
						}
					} else if (i == 3) {
						switch(j) {
							case 1: t[i][j] = new Tuile(3, 5, false); break;
							case 2: t[i][j] = new Tuile(6, 3, false); break;
							case 3: t[i][j] = new Tuile(7, true); break;
							case 4: t[i][j] = new Tuile(2, 12, false); break;
							case 5: t[i][j] = new Tuile(4, 6, false); break;
							default : t[i][j] = new Tuile(0, false); break;
						}
					} else if (i == 4) {
						switch(j) {
							case 0: t[i][j] = new Port(4); break;
							case 1: t[i][j] = new Tuile(5, 10, false); break;
							case 2: t[i][j] = new Tuile(2, 4, false); break;
							case 3: t[i][j] = new Tuile(4, 8, false); break;
							case 4: t[i][j] = new Tuile(6, 10, false); break;
							case 5: t[i][j] = new Tuile(5, 9, false); break;
							case 6: t[i][j] = new Port(2); break;
						}
					} else if (i == 5) {
						switch(j) {
							case 2: t[i][j] = new Tuile(4, 2, false); break;
							case 3: t[i][j] = new Tuile(5, 11, false); break;
							case 4: t[i][j] = new Tuile(3, 12, false); break;
							default : t[i][j] = new Tuile(0, false); break;
						}
					} else {
						switch(j) {
							case 2: t[i][j] = new Port(3); break;
							case 4: t[i][j] = new Port(0); break;
							default: t[i][j] = new Tuile(0, false); break;
						}
					}
				}
			}
			
			// on pointe les routes et intersections des tuiles au bon endroit
			for (int i = 0; i < t.length; i++) {
				for (int j = 0; j < t[i].length; j++) {
					// on commence par les routes
					Route nord, sud, est, ouest;
					
					// on note les routes nord (la 1er ligne n'en a pas)
					if (i != 0) nord = r[(i - 1) * 2 + 1][j];
					else nord = null;

					// on note les routes sud (la derniere ligne n'en a pas)
					if (i != t.length - 1) sud = r[i * 2 + 1][j];
					else sud = null;
					
					// on note les routes ouest (la 1er colonne n'en a pas)
					if (j != 0) ouest = r[i * 2][j - 1];
					else ouest = null;
					
					// on note les routes est (la derniere colonne n'en a pas)
					if (j != t[i].length - 1) est = r[i * 2][j];
					else est = null;
					
					// on initialise les routes avec ce qu'on a note
					t[i][j].iniRoute(nord, sud, est, ouest);
					
					
					// on fini par les intersections
					Intersection no, ne, so, se;
					
					// on note les intersections nord
					if (i != 0) {
						
						// on note l'intersection nord-ouest (la 1er colonne n'en a pas)
						if (j != 0) no = in[i - 1][j - 1];
						else no = null;
						
						// on note l'intersection nord-est (la derniere colonne n'en a pas)
						if (j != t[i].length - 1) ne = in[i - 1][j];
						else ne = null;
					}
					
					// la 1er ligne n'a pas d'intersections nord
					else {
						no = null;
						ne = null;
					}
					
					// on note les intersections sud
					if (i != t.length - 1) {
						
						// on note l'intersection sud-ouest (la 1er colonne n'en a pas)
						if (j != 0) so = in[i][j - 1];
						else so = null;
						
						// on note l'intersection sud-est (la derniere colonne n'en a pas)
						if (j != t[i].length - 1) se = in[i][j];
						else se = null;
					}
					
					// la derniere ligne n'a pas d'intersections sud
					else {
						so = null;
						se = null;
					}
					
					// on initialise les intersections avec ce qu'on a note
					t[i][j].iniIntersection(no, ne, so, se);
				}
			}
		}
		
	// Initialise les routes construisables (les routes entre les tuiles marines
	// sont laissees a null)
	private static void iniRoutes(Route[][] r) {
		for (int i = 1; i < r.length - 1; i++) {

			// la 2e et avant-avant-derniere ligne n'ont que 3 routes construisibles
			if (i == 1 || i == 11) {
				for (int j = 2; j < r[i].length - 2; j++)
					r[i][j] = new Route();
			}

			// la 3e et avant-avant-avant derniere ligne n'ont que 4 routes construisibles
			else if (i == 2 || i == 10) {
				for (int j = 1; j < r[i].length - 2; j++)
					r[i][j] = new Route();
			}

			// les lignes impaires ont 5 routes construisibles
			else if (i % 2 != 0) {
				for (int j = 1; j < r[i].length - 1; j++)
					r[i][j] = new Route();
			}

			// les lignes paires ont 6 routes construisibles
			else {
				for (int j = 0; j < r[i].length - 1; j++)
					r[i][j] = new Route();
			}
		}
	}

	// Initialise les intersections construisables (les intersections entre
	// tuiles marines sont laissees a null)
	private static void iniIntersections(Intersection[][] t) {
		for (int i = 0; i < t.length; i++) {

			// les 1er et derniere lignes n'ont que 4 intersections construisibles
			if (i == 0 || i == 5) {
				for (int j = 1; j < t[i].length - 1; j++)
					t[i][j] = new Intersection();
			}

			// les autres lignes ont toutes leurs intersections construisibles
			else {
				for (int j = 0; j < t[i].length; j++)
					t[i][j] = new Intersection();
			}
		}
	}
}

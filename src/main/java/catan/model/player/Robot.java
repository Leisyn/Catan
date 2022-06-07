package catan.model.player;

import java.util.ArrayList;
import java.util.Random;

import catan.model.Jeu;
import catan.model.board.Intersection;
import catan.model.board.Route;
import catan.model.board.Tuile;
import catan.model.card.Carte;
import catan.model.card.CarteChevalier;
import catan.model.card.CarteProgres;
import catan.model.other.Paire;



public class Robot extends Joueur {
	public Robot(String n, Jeu j) {
		super(n, j);
	}

	public boolean phaseDeConstruction() {
		// TODO: implemente action pour les IA
		// meme fonctionnement que dans Joueur, mais on tire des choix
		//   aleatoires

		System.out.println("=========================");
		System.out.println("| PHASE DE CONSTRUCTION |");
		System.out.println("=========================\n");

		// on affiche ce que le joueur peut faire
		System.out.println("Vous pouvez actuellement : ");
		affichePeutFaire();

		// on demande ce que le joueur veut faire et on effectue l'action demandee

		Random rd = new Random();
		int n = rd.nextInt(5);

		if(n==0) {
			if(getRessources().get("argile") >= 1 && getRessources().get("bois") >= 1) {
				construire(0, false);
			}
		}


		if(n==1) {
			if(getRessources().get("argile") >= 1 && getRessources().get("bois") >= 1
			&& getRessources().get("laine") >= 1 && getRessources().get("blé") >= 1) {

				construire(1, false);
			}
		}

		if(n==3) {
			if(getRessources().get("minerai") >= 1 && getRessources().get("laine") >= 1
				&& getRessources().get("blé") >= 1 ) {

				acheteCarte();

			}
		}

		if(n==2) {
			if(getRessources().get("minerai") >= 3 && getRessources().get("blé") >= 2) {
				construire(2, false);
			}
		}

		if(n==4) {
			jouerCarte();
		}
		return true; //le default du switch

	}




	public void marchander(Joueur j) {
		// TODO: implemente marchander pour les IA
		// meme fonctionnement que dans Joueur, mais on tire des choix
		//   aleatoires

		int ressourceADonner = 0;
		int ressourceARecevoir = 0;

		// on affiche les taux d'echanges du joueur
		afficheTauxEchange();
		ArrayList <String> m = new ArrayList<String>();

		if(j.getRessources().get("laine") >= j.getTauxEchange().get("laine")) {
			m.add("laine");
		}
		if(j.getRessources().get("bois") >= j.getTauxEchange().get("bois")) {
			m.add("bois");
		}
		if(j.getRessources().get("blé") >= j.getTauxEchange().get("blé")) {
			m.add("blé");
		}
		if(j.getRessources().get("argile") >= j.getTauxEchange().get("argile")) {
			m.add("argile");
		}
		if(j.getRessources().get("minerai") >= j.getTauxEchange().get("minerai") ) {
			m.add("minerai");
		}


		// on demande ce que le joueur veut echanger
		Random rd = new Random();
		int n = rd.nextInt(m.size());


		ressourceADonner = n;

		System.out.println();


		// on demande contre quoi le joueur veut echanger


		Random rd1 = new Random();
		int r = rd1.nextInt(4);
		ressourceARecevoir  = r;

		System.out.println();

		// sinon, on procede a l'echange
		perd(ressourceADonner, j.getTauxEchange().get(m.get(n)));
		obtient(ressourceARecevoir, 1);
	}





	public boolean jouerCarte() {

		afficheCartesJouables();

		// on demande la carte que le joueur veut jouer

		String nomCarte="";
		Random rd = new Random();
		int n = rd.nextInt(5);


		if(n==0) {
			nomCarte="retour";
		}

		if(n==1) {
			nomCarte="chevalier";
		}

		if(n==2) {
			nomCarte="construction";
		}

		if(n==3) {
			nomCarte="monopole";
		}

		if(n==4) {
			nomCarte="invention";
		}

		if(n==5) {
			nomCarte="retour";
		}


		// on regarde si le joueur a demande de revenir
		if (nomCarte.equals("retour"))
			return aGagne();

		// sinon, on la retire des cartes actuelles du joueur et on la joue
		for (Carte c : getCartes()) {
			if (c.nom.equals(nomCarte)) {
				getCartes().remove(c);
				c.jouer(jeuActuel, this);
				return aGagne();
			}
		}

		return aGagne();
	}





	public boolean phaseDeProduction() {
		System.out.println("=======================");
		System.out.println("| PHASE DE PRODUCTION |");
		System.out.println("=======================\n");

		// on affiche ce que le joueur peut faire
		System.out.println("Vous pouvez :");
		affichePeutJouerCarte();
		System.out.println("  * (L) Lancer un dé\n");

		// on demande ce que le joueur veut faire

		//0 pour jouer une carte et 1 pour lancer le de
		Random rd = new Random();
		int n = rd.nextInt(1);


		// on regarde si le joueur a demande de jouer une carte
		if (n==0) {

			// on regarde s'il a une carte jouable
			for (Carte c : getCartes()) {
				if (c instanceof CarteChevalier || c instanceof CarteProgres)
					return true;
				}

				// sinon, il ne possede aucune cartes jouables
				System.out.println("Vous ne possédez aucune cartes jouables.\n");
				return false;
			}

		// on regarde si le joueur a demande de lancer le de
		if (n==1)
			return true;

		// on regarde si le joueur a demandé de jouer une carte et on renvoie s'il a gagné ou non
		return jouerCarte();
	}



	public boolean phaseDeCommerce() {
		System.out.println("=====================");
		System.out.println("| PHASE DE COMMERCE |");
		System.out.println("=====================\n");

		// on affiche ce que le joueur peut faire
		System.out.println("Vous pouvez :");
		affichePeutJouerCarte();
		System.out.println("  * (M) Marchander");
		System.out.println("  * (P) Passer à la phase de construction\n");

		// on demande ce que le joueur veut faire

		//0 pour passer à la phase de construction donc changer de phase ;
		//1 pour jouer une carte ;
		//2 pour marchander
		Random rd = new Random();
		int n = rd.nextInt(2);

		// on regarde si le joueur a demande de passer a la phase de construction donc de changer de phase
		if (n==0)
			return true;

		// on regarde si le joueur a demande de jouer une carte, et on renvoie s'il a gagné ou non
		if (n==1) {
				// on regarde s'il a une carte jouable
				for (Carte c : getCartes()) {
					if (c instanceof CarteChevalier || c instanceof CarteProgres) {
						return jouerCarte();
					}else {
						// sinon, il ne possede aucune cartes jouables
						System.out.println("Vous ne possedez aucune cartes jouables.\n");
						return false;
					}
				}

		}

		// le joueur a demandé de marchander
		if(n==2) {
			marchander();
		}

		// on renvoie si le joueur a gagné ou non
		return aGagne();

	}







	// Demande au joueur la position où il souhaite construire (0 : route / 1 : colonie / 2 : ville) et renvoie s'il a gagné ou non
		public boolean construire(int typeAConstruire, boolean phaseInitiale) {
		//	Random rd = new Random();
		//	typeAConstruire = rd.nextInt(2);

			if (typeAConstruire == 0 && nbRoute >= Jeu.maxRoute) {
				System.out.println("Vous avez atteint le nombre maximum de routes.");
				return aGagne();
			}

			if (typeAConstruire == 1 && nbColonie >= Jeu.maxColonie) {
				System.out.println("Vous avez atteint le nombre maximum de colonies.");
				return aGagne();
			}

			if (typeAConstruire == 2 && nbVille >= Jeu.maxVille) {
				System.out.println("Vous avez atteint le nombre maximum de villes.");
				return aGagne();
			}
/////////////////////////////////////////////////////////////////


			Paire position = null;
			String direction = null;

////////////////////////////////////////////////////////////////////
//METHODE demandePosition

			Random rd1 = new Random();
			int type = rd1.nextInt(2);//0 pour demander la tuile pour construire
									 //1 pour la tuile pour placer le voleur
									//2 pour retourner au menu d'action

	/*
			// on regarde s'il faut demander la tuile pour construire, ou la tuile pour placer le voleur
			if (type == 1)
				System.out.println("Sur quelle tuile voulez-vous placer le voleur ? (Veuillez entrer le nom de la tuile sous le format \"FO 6\", ou \"DES\" pour le désert)");
			else {
				if (phaseInitiale) {
					if (typeAConstruire == 0)
						System.out.println("Autour de quelle tuile voulez-vous construire votre route ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le désert)");
					else if (typeAConstruire == 1)
						System.out.println("Autour de quelle tuile voulez-vous construire votre colonie ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le désert)");
					else if (typeAConstruire == 2)
						System.out.println("Autour de quelle tuile se trouve la colonie que vous souhaitez transformer en ville ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le désert)");
				}

				else {
					if (typeAConstruire == 0)
						System.out.println("Autour de quelle tuile voulez-vous construire votre route ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le désert, ou \"retour\" pour revenir)");
					else if (typeAConstruire == 1)
						System.out.println("Autour de quelle tuile voulez-vous construire votre colonie ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le désert, ou \"retour\" pour revenir)");
					else if (typeAConstruire == 2)
						System.out.println("Autour de quelle tuile se trouve la colonie que vous souhaitez transformer en ville ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le désert, ou \"retour\" pour revenir)");
				}
			}

			*/

			// on recupere ce que l'utilisateur a rentre
			String position2 = "";
			Random rd2 = new Random();
			int n = rd2.nextInt(6);

			if(n==0) {
				Random rd3 = new Random();
				int n1 = rd3.nextInt(12);
				if(n==3 || n== 12 || n==2 || n==4)
					position2="PR "+n;
				System.out.println("n'existe pas");
			}
			if(n==1) {
				Random rd3 = new Random();
				int n1 = rd3.nextInt(12);
				if(n==6 || n== 9 || n==5 || n==12)
					position2="FO "+n;
				System.out.println("n'existe pas");
			}
			if(n==2) {
				Random rd3 = new Random();
				int n1 = rd3.nextInt(12);
				if(n==4 || n==6 || n==8 || n==2)
					position2="CO "+n;
				System.out.println("n'existe pas");
			}
			if(n==3) {
				Random rd3 = new Random();
				int n1 = rd3.nextInt(12);
				if(n==8 || n==10 || n==9 || n==11)
					position2="CH "+n;
				System.out.println("n'existe pas");
			}
			if(n==4) {
				Random rd3 = new Random();
				int n1 = rd3.nextInt(12);
				if(n==5 || n==11 || n==3 || n==10)
					position2="MO "+n;
				System.out.println("n'existe pas");
			}
			if(n==5) {
				position2="DES";
			}
			if(n==6) {
				position2="retour";
			}


			// si l'utilisateur ne doit pas placer le voleur, on regarde s'il a demande de retourner au menu d'action

			/*ggg*/
			if ((type == 2 || !phaseInitiale) && n==6)
				//return new Paire(-1, -1);
				return true;
			/*hh*/
	        // on regarde si le joueur a rentré uniquement 3 lettres
			if (position2.length() != 3 && position2.length() != 4) {
				System.out.println("Format incorrect.\n");
				return false;
			}

			/*h*/
			// on regarde si l'utilisateur a demande le desert
			if (n==5) {
			    System.out.println();
				position=new Paire(3, 3);
				return true;
			}
			/*h*/

			// on parse ce que l'utilisateur a rentre pour obtenir le type de la tuile et le numero du jeton
			String nom = position2.substring(0, 2);
			String nb = position2.substring(2);
			int jeton = 0;

			// on regarde si l'utilisateur a bien rentre un nombre pour le jeton
			if (!nb.matches("\\d*")) {
				System.out.println("Format incorrect.\n");
				return false;
			}

			// on recupere la position de la tuile dans le plateau
			jeton = Integer.parseInt(nb);
			Paire pa = jeuActuel.getPlateau().position(nom, jeton);

			// on renvoie la position de la tuile si elle existe
			if (pa != null) {
				System.out.println();
				return true;
			}
			// sinon, le joueur a rentre une tuile inconnue
			System.out.println("Tuile inexistante.\n");
			//return false;


/////////////////////////////////////////////////////////////////////////////
			// on demande la direction dans laquelle il veut construire

			Random rd4 = new Random();
			int n4 = rd4.nextInt(4);

			if(n4==0) {
				direction="N";
			}
			if(n4==1) {
				direction="S";
			}
			if(n4==2) {
				direction="O";
			}
			if(n4==3) {
				direction="E";
			}
			if(n4==4) {
				direction="RETOUR";
			}


//////////////////////////////////////////////////////////////////////
//methode demandeDirection

			// on regarde si l'action donnee est correcte
			if (typeAConstruire != 0 && typeAConstruire != 1 && typeAConstruire != 2)
				throw new IllegalArgumentException("Identifiant d'action inconnu.");

			System.out.println("Dans quelle direction voulez-vous construire ? (Veuillez entrez l'initial de la direction en format cardinal (N pour nord), ou \"retour\" pour revenir à la phase de construction)");
			//String d = jeuActuel.sc.next().toUpperCase();

			// on regarde si l'utilisateur a demande de retourner au menu d'action
			if (n4==4) {
				System.out.println();
				return aGagne();
			}

			// on regarde si l'utilisateur veut construire une route
			if ( typeAConstruire== 0) {

				// on regarde s'il lui reste des routes disponibles
				if (nbRoute >= 15) {
					System.out.println("Vous avez atteint le nombre maximum de routes.\n");
					return false;
				}

				// on regarde si les directions correspondent
				if (n4!=0 && n4!=1 && n4!=2 && n4!=3) {
					System.out.println("Direction inconnue\n");
					return false;
				}

				Tuile t = jeuActuel.getPlateau().getTuiles()[position.x][position.y];

				// on regarde si la route que le joueur veut construire est deja construite
				if (t.getRoute(direction).joueur != null) {
					System.out.println("Cette route est deja occupée.\n");
					return false;
				}

				// on regarde s'il s'agit de la phase initiale
				if (phaseInitiale) {
					// on récupère les deux intersections en contact avec la route que le joueur veut construire
					Intersection[] intersections = jeuActuel.getPlateau().getAllIntersections(t.getRoute(direction));

					// on regarde si une de ces deux intersections appartient au joueur
					if (intersections[0].joueur != this && intersections[1].joueur != this) {
						System.out.println("La route n'est en contact avec aucune de vos intersections.\n");
						return false;
					}

					// on récupère l'intersection construite par le joueur
					Intersection in;
					if (intersections[0].joueur == this)
						in = intersections[0];
					else
						in = intersections[1];

					// on regarde si cette intersection n'a pas déjà une route construite par le joueur (pendant la phase initiale, il faut construire la route près de la colonie qu'on vient de construire)
					Route[] r = jeuActuel.getPlateau().getAllRoutes(in);
					for (int i = 0; i < r.length; i++) {
						if (r[i] != null && r[i].joueur == this) {
							System.out.println("Pendant la phase initiale, veuillez placer votre route près de la colonie que vous venez juste de construire.\n");
							return false;
						}
					}
				}

				else {
					// on recupere les 6 routes en contact avec la route a construire
					Route[] r = jeuActuel.getPlateau().getAllRoutes(t.getRoute(direction));
					boolean enContact = false;

					// on regarde si une de ces routes appartient au joueur
					for (int i = 0; i < r.length; i++) {
						if (r[i].joueur == this)
							enContact = true;
					}

					// si aucune de ces routes n'appartient au joueur, la construction n'est pas possible
					if (!enContact) {
						System.out.println("La construction n'est en contact avec aucune autre de vos constructions.\n");
						return false;
					}
				}
			}

			// on regarde si le joueur veut construire une intersection
			else {
				// on regarde s'il lui reste des colonies disponibles s'il veut construire une colonie
				if (typeAConstruire == 1 && nbColonie >= Jeu.maxColonie) {
					System.out.println("Vous avez atteint le nombre maximum de colonie.\n");
					return false;
				}

				// on regarde s'il lui reste des villes disponibles s'il veut construire une ville
				else if (typeAConstruire == 2 && nbVille >= Jeu.maxVille) {
					System.out.println("Vous avez atteint le nombre maximum de ville.\n");
					return false;
				}
///////////
				String direction2="";
				Random rd5 = new Random();
				int n5 = rd5.nextInt(3);

				if(n5==0) {
					direction2="N0";
				}
				if(n5==1) {
					direction2="NE";
				}
				if(n5==2) {
					direction2="SO";
				}
				if(n5==3) {
					direction2="SE";
				}

//////////
				// on regarde si les directions correspondent
				if (n5!=0 && n5!=1 && n5!=2 && n5!=3) {
					System.out.println("Direction inconnue.\n");
					return false;
				}

				// on recupere la tuile autour de laquelle on veut construire l'intersection
				Tuile t = jeuActuel.getPlateau().getTuiles()[position.x][position.y];

				// si le joueur veut construire une ville
				if (typeAConstruire == 2) {

					// on regarde si l'intersection qu'il a donne contient une de ses colonies
					if (t.getIntersection(direction2) == null || t.getIntersection(direction2).joueur != this || t.getIntersection(direction2).batiment != 1) {
						System.out.println("Impossible de construire une ville dans cette position.\n");
						return false;
					}

					// si oui, on renvoie ce que le joueur a entre
					System.out.println();
					return true; //return direction;
				}

				// s'il veut construire une colonie, on regarde si l'intersection est deja construite
				if (t.getIntersection(direction2).joueur != null) {
					System.out.println("Cette intersection est déjà occupée.\n");
					return false;
				}

				// si ce n'est pas le debut de la partie
				if (!phaseInitiale) {

					// on recupere les 4 routes qui menent a l'intersection que le joueur veut construire
					Route[] routes = jeuActuel.getPlateau().getAllRoutes(t.getIntersection(direction2));
					boolean enContact = false;

					// on regarde si une de ces routes appartient au joueur
					for (int i = 0; i < routes.length; i++) {
						if (routes[i].joueur == this)
							enContact = true;
					}

					// si aucune de ces routes n'appartient au joueur, il ne peut pas construire
					if (!enContact) {
						System.out.println("La construction n'est en contact avec aucune autre de vos construction.\n");
						return false;
					}
				}

				// on regarde si la regle de distance est respecte
				if (!jeuActuel.getPlateau().respecteRegleDistance(t.getIntersection(direction2))) {
					System.out.println("La règle de distance n'est pas respectée.\n");
					return false;
				}
			}

			// toutes les conditions de constructions sont correctes, on renvoie ce que l'utilisateur a entre
			System.out.println();
			//return true; //a mettre return direction;




//////////////////////////////////////////////////////////////////

//METHODE CONSTRUIRE

			// on regarde s'il a demande de revenir
			if (n4==4)
				return aGagne();

			// sinon, on construit
			//jeuActuel.getPlateau().construire(jeuActuel, typeAConstruire, this, position, direction);

	        // on incrémente le compteur
			if (typeAConstruire == 0)
				nbRoute++;
			else if (typeAConstruire == 1)
				nbColonie++;
			else
				nbVille++;

			// on renvoie si le joueur a gagne ou non
			return aGagne();
		}


////////////////////////////////////////////////////////////////////
}

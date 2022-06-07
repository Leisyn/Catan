package catan.model;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import catan.model.board.Intersection;
import catan.model.board.Plateau;
import catan.model.board.Tuile;
import catan.model.card.Carte;
import catan.model.card.CarteChevalier;
import catan.model.card.CarteProgres;
import catan.model.card.CarteVictoire;
import catan.model.other.Paire;
import catan.model.player.Joueur;

public class Jeu {
	private Plateau plateau;
	private Joueur[] joueurs;
	
	public Scanner sc;
	public LinkedList<Carte> carteDispo = new LinkedList<>();
	
	public static int maxRoute = 15;
	public static int maxColonie = 5;
	public static int maxVille = 4;
	
	public Joueur aLaRouteLaPlusLongue;
	public Joueur aLArmeeLaPlusPuissante;
	
	public static int minRoutePlusLongue = 5;
	public static int minArmeeLaPlusPuissante = 3;
	
	public Jeu(int n, Scanner s) {
		plateau = Plateau.iniPlateau();
		joueurs = new Joueur[n];
		
		sc = s;
		iniCarteDispo();
		
		aLaRouteLaPlusLongue = null;
		aLArmeeLaPlusPuissante = null;
	}
	
	public void iniJoueurs(LinkedList<Joueur> j) {
		// on v�rifie que la liste donn�e contient le nombre de joueurs correspondant au jeu
		if (joueurs.length != j.size())
			throw new IllegalArgumentException("Le nombre de joueurs donn�s est diff�rent du nombre de joueur attendu.");
		
		// on tire les joueurs al�atoirement, afin que le joueur qui commence soit al�atoire
		Random rd = new Random();

		for (int i = 0; i < joueurs.length; i++) {
			int n = rd.nextInt(j.size());
			joueurs[i] = j.get(n);
			j.remove(n);
		}
	}
	
	// Initialise les cartes disponibles a l'achat
	private void iniCarteDispo() {
		// 14 cartes chevalier
		for (int i = 0; i < 14; i++)
			carteDispo.add(new CarteChevalier());
		
		// 6 cartes progres, 2 de chaque
		for (int i = 0; i < 2; i++)
			carteDispo.add(new CarteProgres("construction"));
		for (int i = 0; i < 2; i++)
			carteDispo.add(new CarteProgres("invention"));
		for (int i = 0; i < 2; i++)
			carteDispo.add(new CarteProgres("monopole"));
		
		// 5 cartes victoires, 5 de chaque
		carteDispo.add(new CarteVictoire("bibliotheque"));
		carteDispo.add(new CarteVictoire("marche"));
		carteDispo.add(new CarteVictoire("parlement"));
		carteDispo.add(new CarteVictoire("eglise"));
		carteDispo.add(new CarteVictoire("universite"));
	}
	
	public Plateau getPlateau() {
		return plateau;
	}

	public Joueur[] getJoueurs() {
		return joueurs;
	}

	
	
	
	
	public void giveRouteLaPlusLongue() {
		// on r�cup�re le nombre minimum de routes requis pour �tre attribu� la route la plus longue
		int max = minRoutePlusLongue;
		
		// on r�cup�re le joueur qui a la route la plus longue
		Joueur avant = aLaRouteLaPlusLongue;
				
		// on attribue la route la plus longue au joueur ayant la plus longue route, atteignant au moins le nombre requis
		for (int i = 0; i < joueurs.length; i++) {
			if (joueurs[i].routeLaPlusLongue > max) {
				max = joueurs[i].routeLaPlusLongue;
				aLaRouteLaPlusLongue = joueurs[i];
			}
		}
		
		// si le joueur qui a la route la plus longue n'est plus le m�me
		if (aLaRouteLaPlusLongue != avant) {
			
			// on enl�ve 2 points au joueur qui avait la route la plus longue
			if (avant != null)
				avant.points -= 2;
			
			// on ajoute 2 points au joueur qui a la route la plus longue
			if (aLaRouteLaPlusLongue != null)
				aLaRouteLaPlusLongue.points += 2;
		}
	}
	
	public void giveArmeeLaPlusPuissante() {
		// on r�cup�re le nombre minimum de routes requis pour �tre attribu� la route la plus longue
		int max = minArmeeLaPlusPuissante;
		
		// on r�cup�re le joueur qui a l'arm�e la plus puissante
		Joueur avant = aLArmeeLaPlusPuissante;
				
		// on attribue l'arm�e la plus puissante au joueur ayant jou� le plus de cartes chevalier, atteignant au moins le nombre requis
		for (int i = 0; i < joueurs.length; i++) {
			if (joueurs[i].armeeLaPlusPuissante > max) {
				max = joueurs[i].armeeLaPlusPuissante;
				aLArmeeLaPlusPuissante = joueurs[i];
			}
		}
		
		// si le joueur qui a l'arm�e la plus puissante n'est plus le m�me
		if (aLArmeeLaPlusPuissante != avant) {
			
			// on enl�ve 2 points au joueur qui avait l'arm�e la plus puissante
			if (avant != null)
				avant.points -= 2;
			
			// on ajoute 2 points au joueur qui a l'arm�e la plus puissante
			if (aLArmeeLaPlusPuissante != null)
				aLArmeeLaPlusPuissante.points += 2;
		}
	}
	
	public void partie() {
		// on lance la phase initiale du jeu
		phaseInitiale();
		
		// on lance le tour de chacun des joueurs, jusqu'� ce qu'un des joueurs gagne
		int i = 0;
		while (!unTour(joueurs[i])) {
			if (i == joueurs.length - 1) i = 0;
			else i++;
		}
		
		// on affiche le gagnant
		System.out.println(joueurs[i].nom + " a gagn� !");
	}
	
	// Lance la phase initiale du jeu
	private void phaseInitiale() {
		// 1er tour
		for (int i = 0; i < joueurs.length; i++) {
			afficheNomTour(joueurs[i]);
			plateau.affichePlateau();
			while (joueurs[i].getNbColonie() != 1)
				joueurs[i].construire(1, true);  // on demande au joueur de placer une colonie
			plateau.affichePlateau();
			while (joueurs[i].getNbRoute() != 1)
				joueurs[i].construire(0, true);  // on demande au joueur de placer une route pr�s de la colonie qu'il vient de placer
		}
		
		// 2e tour
		for (int i = joueurs.length - 1; i >= 0; i--) {
			afficheNomTour(joueurs[i]);
			plateau.affichePlateau();
			while (joueurs[i].getNbColonie() != 2)
				joueurs[i].construire(1, true);  // on demande au joueur de placer une colonie
			plateau.affichePlateau();
			while (joueurs[i].getNbRoute() != 2)
				joueurs[i].construire(0, true);  // on demande au joueur de placer une route pr�s de la colonie qu'il vient de placer
		}
	}
	
	private void afficheNomTour(Joueur j) {
		String s = "";
		for (int i = 0; i < j.nom.length(); i++)
			s += "=";
		
		System.out.println("==========" + s + "==");
		System.out.println("| TOUR DE " + j.nom.toUpperCase() + " |");
		System.out.println("==========" + s + "==\n");
	}
	
	// Effectue un tour de jeu du joueur et renvoie si le joueur a gagne
	public boolean unTour(Joueur j) {
		// on affiche le nom du joueur actuel
		afficheNomTour(j);
		
		// on affiche l'�tat du plateau
		plateau.affichePlateau();
		
		// on affiche ses points et ses cartes actuelles
		j.afficheCartes();
		j.affichePoints();
		
		// on lance sa phase de production
		boolean next = false;
		while (next == false)
			next = j.phaseDeProduction();
		
		// on regarde si le joueur a gagne
		if (j.aGagne())
			return true;
		
		// on lance le de et on effectue l'action correspondante
		int n = lanceDe();
		System.out.println("Vous avez lanc� un " + n + ".");
		
		if (n == 7)
			septAuDe(j);
		else
			donneRessource(n, j);
		
		// on affiche ses ressources et ses points
		j.afficheRessources();
		j.affichePoints();
		
		// on lance sa phase de commerce
		next = false;
		while (next == false)
			next = j.phaseDeCommerce();
		
		// on regarde si le joueur a gagne
		if (j.aGagne())
			return true;
		
		// on lance sa phase de construction
		next = false;
		while (next == false)
			next = j.phaseDeConstruction();
		
		// on renvoie si le joueur a gagne
		return j.aGagne();
	}
	
	public int lanceDe() {
		Random rd = new Random();
		return rd.nextInt(11) + 2;
	}
	
	private void septAuDe(Joueur j) {
		System.out.println("\n=================");
		System.out.println("| SEPT AUX D�ES |");
		System.out.println("=================\n");
		
		// on regarde si un joueur a plus de 7 ressources
		for (Joueur jo : joueurs) {
			if (jo.getNbRessource() > 7) {
				
				// si oui, il doit se defausser de la moitie de ses ressources
				int n = jo.getNbRessource() / 2;
				if (jo == j)
					System.out.println("Vous avez plus de 7 ressources, veuillez vous d�fausser de " + n + " ressources.\n");
				else
					System.out.println(jo.nom + " a plus de 7 ressources, il doit se d�fausser de " + n + " ressources.\n");
				
				// on affiche ses ressources
				jo.afficheRessources();
				
				// on lui demande de se defausser d'une ressource a la fois
				for (int i = 0; i < n; i++) {
					String ressource = null;
					
					while (ressource == null)
						ressource = jo.demandeRessourceADonner(1);
					
					jo.perd(ressource, 1);
				}
			}
		}
		
		// on deplace alors le voleur
		auVoleur(j);
	}
	
	public void auVoleur(Joueur j) {
		Paire p = null;
		
		// on demande sur quelle tuile le joueur veut deplacer le voleur
		while (p == null) {
			p = j.demandePosition(1, -1, true);
			
			// on verifie que le voleur n'est pas deja sur la tuile (d'apres les regles, il est obligatoire de deplacer le voleur)
			if (plateau.getTuiles()[p.x][p.y].voleurEstIci) {
				System.out.println("Le voleur se trouve d�j� sur la tuile.\n");
				p = null;
			}
		}
		
		// on deplace le voleur a la position
		plateau.retireVoleur();
		plateau.placeVoleur(p);
		
		// on regarde s'il y a des intersections construites autour de la nouvelle position du voleur
		LinkedList<Intersection> intersections = plateau.getTuiles()[p.x][p.y].getToutesInterConstruites();
		
		if (!intersections.isEmpty()) {
			
			// on recupere tous les joueurs adverses qui ont une intersection construite autour de la nouvelle position du voleur
			LinkedList<Joueur> joueurs = new LinkedList<>();
			
			for (Intersection in : intersections) {
				if (!joueurs.contains(in.joueur) && in.joueur != j)
					joueurs.add(in.joueur);
			}
			
			// on demande au joueur a quel joueur adverse il veut aleatoirement prendre une ressource
			Joueur adverse = null;
			while (adverse == null)
				adverse = j.demandeQuelJoueur(joueurs);
			
			// on recupere une ressource du joueur adverse de facon aleatoire
			String ressource = adverse.perdRessourceAleatoire();
			
			// s'il n'avait aucune ressource, on l'annonce
			if (ressource.equals("rien"))
				System.out.println("Ce joueur ne poss�dait aucune ressource.\n");
			
			// sinon, on donne la ressource recupere au joueur actuel
			else
				j.obtient(ressource, 1);
		}
	}
	
	public void donneRessource(int n, Joueur joueur) {
		Tuile[][] t = plateau.getTuiles();
		
		// on parcourt le plateau
		for (int i = 0; i < t.length; i++) {
			for (int j = 0; j < t[i].length; j++) {
				
				// si le jeton de la tuile correspond et qu'il n'y a pas de voleur
				if (t[i][j].jeton == n && !t[i][j].voleurEstIci) {
					
					// on recupere toutes les intersections construites autour et on donne les ressources selon le type de batiment construit
					for (Intersection in : t[i][j].getToutesInterConstruites()) {
						String ressource = null;
						
						switch (t[i][j].type) {
							case 2: ressource = "laine"; break; 
							case 3: ressource = "bois"; break;
							case 4: ressource = "argile"; break;
							case 5: ressource = "bl�"; break;
							case 6: ressource = "minerai"; break;
							default: ressource = "rien";
						}
						
						if (!ressource.equals("rien")) {
							if (in.joueur == joueur)
								System.out.println("Vous obtenez " + in.batiment + " " + ressource + ".");
							else
								System.out.println(in.joueur.nom + " obtient " + in.batiment + " " + ressource + ".");
							in.joueur.obtient(ressource, in.batiment);
						}
					}
				}
			}
		}
		System.out.println();
	}
	
	// Implementer une methode qui renvoie le status actuel du jeu
	//   (les routes les plus longues de chaque joueur, le nombre de
	//   carte chevalier utilise pour chaque joueur, le nombre de
	//   points de chacun, le nombre total de ressource de chacun,
	//   le nombre de routes et colonies de chacun...)
}

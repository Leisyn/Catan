package catan.model.player;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import catan.model.Jeu;
import catan.model.board.IConstructible;
import catan.model.board.Intersection;
import catan.model.board.Route;
import catan.model.board.Tuile;
import catan.model.card.Carte;
import catan.model.card.CarteChevalier;
import catan.model.card.CarteProgres;
import catan.model.card.CarteVictoire;
import catan.model.other.Paire;

public class Joueur {
	public static int nbJoueur = 1;

	public final Jeu jeuActuel;

	public final int id;
	public final String nom;

	public LinkedList<Carte> cartes = new LinkedList<>();
	public HashMap<String, Integer> tauxEchange = new HashMap<>();
	private HashMap<String, Integer> ressources = new HashMap<>();

	public int points = 0;
	public int routeLaPlusLongue = 0;  // le nombre de routes constituant la route la plus longue du joueur
	public int armeeLaPlusPuissante = 0;  // le nombre de carte chevalier joue

	protected int nbColonie = 0;  // le nombre de colonie (5 max)
	protected int nbVille = 0;  // le nombre de ville (4 max)
	protected int nbRoute = 0;  // le nombre de routes (15 max)

	public Joueur(String n, Jeu j) {
		// on verifie que le nom n'est pas null
		if (n == null || j == null)
			throw new IllegalArgumentException("Impossible d'initialiser le joueur : des arguments sont �gals � null");

		jeuActuel = j;

		// on initialise les informations du joueur (son id et son nom)
		id = nbJoueur;
		nom = n;
		nbJoueur++;

		// on initialise les taux d'echanges du joueur (initialement a 4:1)
		tauxEchange.put("laine", 4);
		tauxEchange.put("bois", 4);
		tauxEchange.put("argile", 4);
		tauxEchange.put("bl�", 4);
		tauxEchange.put("minerai", 4);

		// on initialise les ressources que le joueur possede au depart (aucune)
		ressources.put("laine", 0);
		ressources.put("bois", 0);
		ressources.put("argile", 0);
		ressources.put("bl�", 0);
		ressources.put("minerai", 0);
	}

	public HashMap<String, Integer> getRessources() {
		return ressources;
	}

	public HashMap<String, Integer> getTauxEchange() {
		return tauxEchange;
	}

	public LinkedList<Carte> getCartes() {
		return cartes;
	}

	// Renvoie le nombre total de ressource du joueur
	public int getNbRessource() {
		int n = 0;
		for (Integer i : ressources.values())
			n += i;
		return n;
	}

	public int getNbRoute() {
		return nbRoute;
	}

	public int getNbColonie() {
		return nbColonie;
	}

	public int getNbVille() {
		return nbVille;
	}

	public void aConstruitRoute() {
		nbRoute++;
	}

	public void aConstruitColonie() {
		nbColonie++;
	}

	public void aConstruitVille() {
		nbVille++;
	}





	public boolean aGagne() {
		return points >= 10;
	}

	public void obtient(int typeTuile, int montant) {
		// on recupere le nom de la ressource a donner
		String ressource;
		switch (typeTuile) {
			case 2: ressource = "laine"; break;
			case 3: ressource = "bois"; break;
			case 4: ressource = "argile"; break;
			case 5: ressource = "bl�"; break;
			case 6: ressource = "minerai"; break;
			default: ressource = "rien";
		}

		// on l'ajoute aux ressources actuelles du joueur
		if (!ressource.equals("rien"))
			ressources.replace(ressource, ressources.get(ressource) + montant);
	}

	public void obtient(String ressource, int montant) {
		// on l'ajoute aux ressources actuelles du joueur
		ressources.replace(ressource, ressources.get(ressource) + montant);
	}

	public void perd(int typeTuile, int montant) {
		// on recupere le nom de la ressource a recuperer
		String ressource;
		switch (typeTuile) {
			case 2: ressource = "laine"; break;
			case 3: ressource = "bois"; break;
			case 4: ressource = "argile"; break;
			case 5: ressource = "bl�"; break;
			case 6: ressource = "minerai"; break;
			default: ressource = "rien";
		}

		// on l'enleve des ressources actuelles du joueur
		if (!ressource.equals("rien"))
			ressources.replace(ressource, ressources.get(ressource) - montant);
	}

	public void perd(String ressource, int montant) {
		// on l'ajoute aux ressources actuelles du joueur
		ressources.replace(ressource, ressources.get(ressource) - montant);
	}

	public String perdRessourceAleatoire() {
		Random rd = new Random();
		int n = 0;

		// s'il n'a aucune ressource, on ne renvoie rien
		if (getNbRessource() == 0)
			return "rien";

		// sinon, on tire une ressource aleatoirement, juqu'a ce qu'on arrive sur une ressource que le joueur possede
		while (true) {
			n = rd.nextInt(5);
			switch (n) {
				case 0: if (ressources.get("laine") > 0) return "laine";
				case 1: if (ressources.get("bois") > 0) return "bois";
				case 2: if (ressources.get("argile") > 0) return "argile";
				case 3: if (ressources.get("bl�") > 0) return "bl�";
				default: if (ressources.get("minerai") > 0) return "minerai";
			}
		}
	}

	public void changeTauxEchange(int type) {
		if (type < 0 || type > 5)
			throw new IllegalArgumentException("Type inconnu");

		switch (type) {
			case 1:
				tauxEchange.replace("laine", 2);
				break;
			case 2:
				tauxEchange.replace("argile", 2);
				break;
			case 3:
				tauxEchange.replace("bois", 2);
				break;
			case 4:
				tauxEchange.replace("bl�", 2);
				break;
			case 5:
				tauxEchange.replace("minerai", 2);
				break;
			default:
				tauxEchange.replace("laine", 3);
				tauxEchange.replace("argile", 3);
				tauxEchange.replace("bois", 3);
				tauxEchange.replace("bl�", 3);
				tauxEchange.replace("minerai", 3);
		}
	}





	public boolean phaseDeProduction() {
		System.out.println("=======================");
		System.out.println("| PHASE DE PRODUCTION |");
		System.out.println("=======================\n");

		// on affiche ce que le joueur peut faire
		System.out.println("Vous pouvez :");
		affichePeutJouerCarte();
		System.out.println("  * (L) Lancer un d�\n");

		// on demande ce que le joueur veut faire
		String action = null;
		while (action == null)
			action = demandePhaseProduction();
		System.out.println();

		// on regarde si le joueur a demande de lancer le de
		if (action.equals("L"))
			return true;

		// on regarde si le joueur a demand� de jouer une carte et on renvoie s'il a gagn� ou non
		return jouerCarte();
	}

	public boolean aUneCarteJouable() {
		for (Carte c : cartes) {
			if (c instanceof CarteChevalier || c instanceof CarteProgres)
				return true;
		}
		return false;
	}

	// Joue une carte du joueur et renvoie si le joueur a gagn� ou non
	public boolean jouerCarte() {
		afficheCartesJouables();

		// on demande la carte que le joueur veut jouer
		String carte = null;
		while (carte == null)
			carte = demandeCarteAJouer();

		// on regarde si le joueur a demande de revenir
		if (carte.equals("retour"))
			return aGagne();

		// sinon, on la retire des cartes actuelles du joueur et on la joue
		for (Carte c : cartes) {
			if (c.nom.equals(carte)) {
				cartes.remove(c);
				c.jouer(jeuActuel, this);
				return aGagne();
			}
		}

		return aGagne();
	}

	public boolean phaseDeCommerce() {
		System.out.println("=====================");
		System.out.println("| PHASE DE COMMERCE |");
		System.out.println("=====================\n");

		// on affiche ce que le joueur peut faire
		System.out.println("Vous pouvez :");
		affichePeutJouerCarte();
		System.out.println("  * (M) Marchander");
		System.out.println("  * (P) Passer � la phase de construction\n");

		// on demande ce que le joueur veut faire
		String action = null;
		while (action == null)
			action = demandePhaseCommerce();
		System.out.println();

		// on regarde si le joueur a demande de passer a la phase de construction
		if (action.equals("P"))
			return true;

		// on regarde si le joueur a demande de jouer une carte, et on renvoie s'il a gagn� ou non
		if (action.equals("J"))
			return jouerCarte();

		// le joueur a demand� de marchander
		marchander();

		// on renvoie si le joueur a gagn� ou non
		return aGagne();
	}

	public void marchander() {
		String ressourceADonner = null;
		String ressourceARecevoir = null;

		// on affiche les taux d'echanges du joueur
		afficheTauxEchange();

		// on demande ce que le joueur veut echanger
		while (ressourceADonner == null)
			ressourceADonner = demandeRessourceADonner(0);
		System.out.println();

		// on regarde s'il a demande de retourner au menu d'action
		if (ressourceADonner.equals("retour"))
			return;

		// on demande contre quoi le joueur veut echanger
		while (ressourceARecevoir == null)
			ressourceARecevoir = demandeRessourceARecevoir(0);
		System.out.println();

		// on regarde s'il a demande de retourner au menu d'action
		if (ressourceARecevoir.equals("retour"))
			return;

		// sinon, on procede a l'echange
		perd(ressourceADonner, tauxEchange.get(ressourceADonner));
		obtient(ressourceARecevoir, 1);
	}

	public boolean phaseDeConstruction() {
		System.out.println("=========================");
		System.out.println("| PHASE DE CONSTRUCTION |");
		System.out.println("=========================\n");

		// on affiche ce que le joueur peut faire
		System.out.println("Vous pouvez actuellement : ");
		affichePeutFaire();

		// on demande ce que le joueur veut faire
		String action = null;
		while (action == null)
			action = demandePhaseConstruction();
		System.out.println();

		// on effectue l'action demandee
		switch (action) {
			case "R": return construire(0, false);
			case "C": return construire(1, false);
			case "V": return construire(2, false);
			case "A": return acheteCarte();
			case "J": return jouerCarte();
			default: return true;
		}
	}

	// Verifie si le joueur a les ressources necessaires pour faire l'action demandee
	//  (R construire une route / C construire une colonie / V construire une ville / A acheter une carte)
	public boolean aLesRessources(String action) {
		int laine = ressources.get("laine");
		int bois = ressources.get("bois");
		int argile = ressources.get("argile");
		int ble = ressources.get("bl�");
		int minerai = ressources.get("minerai");

		switch (action) {
			case "R": if (argile >= 1 && bois >= 1) return true;
					break;
			case "C": if (argile >= 1 && bois >= 1 && laine >= 1 && ble >= 1) return true;
					break;
			case "V": if (minerai >= 3 && ble >= 2) return true;
					break;
			case "A": if (minerai >= 1 && laine >= 1 && ble >= 1) return true;
					break;
			default: throw new IllegalArgumentException("Identifiant d'action inconnu.");
		}

		return false;
	}

	// Demande au joueur la position o� il souhaite construire (0 : route / 1 : colonie / 2 : ville) et renvoie s'il a gagn� ou non
	public boolean construire(int typeAConstruire, boolean phaseInitiale) {
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

		Paire position = null;
		String direction = null;

		// on demande a l'utilisateur la tuile autour duquelle il veut construire
		while (position == null)
			position = demandePosition(0, typeAConstruire, phaseInitiale);

		// on regarde s'il a demande de revenir
		if (position.x == -1 && position.y == -1)
			return aGagne();

		// on demande la direction dans laquelle il veut construire
		while (direction == null)
			direction = demandeDirection(position, typeAConstruire, phaseInitiale);

		// on regarde s'il a demande de revenir
		if (direction.equals("RETOUR"))
			return aGagne();

		// sinon, on construit
		IConstructible construction = jeuActuel.getPlateau().getConstruction(position, direction);
		jeuActuel.getPlateau().construire(jeuActuel, construction, this, typeAConstruire);

		// on renvoie si le joueur a gagne ou non
		return aGagne();
	}

	// Achete une carte de la liste de carte disponible
	protected boolean acheteCarte() {
		if (jeuActuel.carteDispo.size() == 0) {
			System.out.println("Il n'y a plus de cartes disponibles � l'achat.\n");
			return aGagne();
		}

		Random rd = new Random();
		int r = rd.nextInt(jeuActuel.carteDispo.size());

		cartes.add(jeuActuel.carteDispo.get(r));
		if (jeuActuel.carteDispo.get(r) instanceof CarteVictoire) {
			points += 1;
		}
		jeuActuel.carteDispo.remove(r);

		// on renvoie si le joueur a gagne ou non
		return aGagne();
	}





	// METHODES D'AFFICHAGE

	// Affiche si le joueur peut jouer une carte
	public void affichePeutJouerCarte() {
		for (Carte c : cartes) {
			if (c instanceof CarteChevalier || c instanceof CarteProgres) {
				System.out.println("  * (J) Jouer une carte");
				return;
			}
		}
	}

	// Affiche les cartes jouables du joueur
	public void afficheCartesJouables() {
		for (Carte c : cartes) {
			if (c instanceof CarteChevalier || c instanceof CarteProgres)
				System.out.println("  * " + c);
		}
		System.out.println();
	}

	// Affiche ce que le joueur peut faire pendant la phase de construction
	public void affichePeutFaire() {
		// on recupere son montant actuel de ressource
		int laine = ressources.get("laine");
		int bois = ressources.get("bois");
		int argile = ressources.get("argile");
		int ble = ressources.get("bl�");
		int minerai = ressources.get("minerai");

		// on regarde s'il a les ressources pour construire une route
		if (argile >= 1 && bois >= 1)
			System.out.println("  * (R) Construire une route (1 argile + 1 bois)");

		// on regarde s'il a les ressources pour construire une colonie
		if (argile >= 1 && bois >= 1 && laine >= 1 && ble >= 1)
			System.out.println("  * (C) Construire une colonie (1 argile + 1 bois + 1 laine + 1 bl�)");

		// on regarde s'il a les ressources pour construire une ville
		if (minerai >= 3 && ble >= 2)
			System.out.println("  * (V) Construire une ville (3 minerais + 2 bl�s)");

		// on regarde s'il a les ressources pour acheter une carte
		if (minerai >= 1 && laine >= 1 && ble >= 1)
			System.out.println("  * (A) Acheter une carte de d�veloppement (1 minerai + 1 laine + 1 bl�)");

		// on regarde s'il a une carte jouable
		affichePeutJouerCarte();

		System.out.println("  * (P) Passer son tour\n");
	}

	// Affiche les ressources
	public void afficheRessources() {
		System.out.println("Ressources actuelles :");
		for (String s : ressources.keySet())
			System.out.println("  * " + ressources.get(s) + " " + s);
		System.out.println();
	}

	public void affichePoints() {
		System.out.println("Vous avez " + points + " points de victoire.\n");
	}

	public void afficheCartes() {
		if (cartes.size() != 0) {
			System.out.println("\nCartes actuelles : ");
			for (Carte c : cartes)
				System.out.println("  * " + c);
		}
		System.out.println();
	}

	public void afficheTauxEchange() {
		System.out.println("Taux d'�change :");

		for (String s : tauxEchange.keySet()) {
			String avecMaj = s.toUpperCase().charAt(0) + s.substring(1);
			System.out.println("  * " + avecMaj + " : " + tauxEchange.get(s) + ":1");
		}

		System.out.println();
	}





	// METHODES DE DEMANDE AU JOUEUR

	private String demandePhaseProduction() {
		System.out.println("Que voulez-vous faire ? (Veuillez entrez la lettre capitale se trouvant avant l'action voulue)");
		String action = jeuActuel.sc.next().toUpperCase();

		// on regarde si le joueur a demande de passer a la prochaine phase
		if (action.equals("L"))
			return action;

		// on regarde si le joueur a demande de jouer une carte
		if (action.equals("J")) {

			// on regarde s'il a une carte jouable
			for (Carte c : cartes) {
				if (c instanceof CarteChevalier || c instanceof CarteProgres)
					return action;
			}

			// sinon, il ne possede aucune carte jouables
			System.out.println("Vous ne poss�dez aucune cartes jouables.\n");
			return null;
		}

		// le joueur a rentre une action inconnue
		System.out.println("Action inconnue.\n");
		return null;
	}

	private String demandeCarteAJouer() {
		System.out.println("Quelle carte voulez-vous jouer ? (Entrez le nom de la carte � jouer, ou \"retour\" pour revenir)");
		String nomCarte = jeuActuel.sc.next().toLowerCase();

		// on regarde s'il s'agit d'un nom de carte jouable correcte, ou de "retour"
		if (!nomCarte.equals("retour") && !nomCarte.equals("chevalier") && !nomCarte.equals("construction") && !nomCarte.equals("monopole") && !nomCarte.equals("invention")) {
			System.out.println("Nom inconnu.\n");
			return null;
		}

		// on regarde s'il s'agit d'une carte que le joueur possede
		for (Carte c : cartes) {
			if (c.nom.equals(nomCarte))
				return nomCarte;
		}

		// le joueur a rentre une carte qu'il ne possede pas
		System.out.println("Vous ne possedez pas cette carte.\n");
		return null;
	}

	private String demandePhaseCommerce() {
		System.out.println("Que voulez-vous faire ? (Veuillez entrez la lettre capitale se trouvant avant l'action voulue)");
		String action = jeuActuel.sc.next().toUpperCase();

		// on regarde s'il a demande de marchander, ou de changer de phase
		if (action.equals("P") || action.equals("M"))
			return action;

		// on regarde s'il a demande d'utiliser une carte
		if (action.equals("J")) {

			// on regarde s'il a une carte jouable
			for (Carte c : cartes) {
				if (c instanceof CarteChevalier || c instanceof CarteProgres)
					return action;
			}

			// sinon, il ne possede aucune cartes jouables
			System.out.println("Vous ne possedez aucune cartes jouables.\n");
			return null;
		}

		// le joueur a rentre quelque chose d'inconnu
		System.out.println("Action inconnue.\n");
		return null;
	}

	private String demandePhaseConstruction() {
		System.out.println("Que voulez-vous faire ? (Veuillez entrez la lettre capitale se trouvant avant l'action voulue)");
		String action = jeuActuel.sc.next().toUpperCase();

		// on regarde s'il a demande de passer son tour
		if (action.equals("P"))
			return action;

		// on regarde si le joueur a demande une action coutant des ressources
		if (action.equals("R") || action.equals("C") || action.equals("V") || action.equals("A")) {

			// on regarde s'il a les ressources necessaires
			if (!aLesRessources(action)) {
				System.out.println("Ressources insuffisantes.\n");
				return null;
			}

			// si oui, on renvoie ce que le joueur a rentre
			return action;
		}

		// on regarde s'il a demande d'utiliser une carte
		if (action.equals("J")) {

			// on regarde s'il a une carte jouable
			for (Carte c : cartes) {
				if (c instanceof CarteChevalier || c instanceof CarteProgres)
					return action;
			}

			// sinon, il ne possede aucune cartes jouables
			System.out.println("Vous ne poss�dez aucune cartes jouables.\n");
			return null;
		}

		// sinon, la lettre ne correspond a aucune action
		System.out.println("Action inconnue.\n");
		return null;
	}

	// Demande la position d'une tuile au joueur
	public Paire demandePosition(int type, int typeAConstruire, boolean phaseInitiale) {
		// on regarde s'il faut demander la tuile pour construire, ou la tuile pour placer le voleur
		if (type == 1)
			System.out.println("Sur quelle tuile voulez-vous placer le voleur ? (Veuillez entrer le nom de la tuile sous le format \"FO 6\", ou \"DES\" pour le d�sert)");

		else {
			if (phaseInitiale) {
				if (typeAConstruire == 0)
					System.out.println("Autour de quelle tuile voulez-vous construire votre route ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert)");
				else if (typeAConstruire == 1)
					System.out.println("Autour de quelle tuile voulez-vous construire votre colonie ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert)");
				else if (typeAConstruire == 2)
					System.out.println("Autour de quelle tuile se trouve la colonie que vous souhaitez transformer en ville ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert)");
			}

			else {
				if (typeAConstruire == 0)
					System.out.println("Autour de quelle tuile voulez-vous construire votre route ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert, ou \"retour\" pour revenir)");
				else if (typeAConstruire == 1)
					System.out.println("Autour de quelle tuile voulez-vous construire votre colonie ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert, ou \"retour\" pour revenir)");
				else if (typeAConstruire == 2)
					System.out.println("Autour de quelle tuile se trouve la colonie que vous souhaitez transformer en ville ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert, ou \"retour\" pour revenir)");
			}
		}

		// on recupere ce que l'utilisateur a rentre
		String position = jeuActuel.sc.next().toUpperCase();

		// si l'utilisateur ne doit pas placer le voleur, on regarde s'il a demande de retourner au menu d'action
		if ((type != 1 || !phaseInitiale) && position.equals("retour"))
			return new Paire(-1, -1);

		// on regarde si le joueur a rentr� uniquement 3 lettres
		if (position.length() != 3 && position.length() != 4) {
			System.out.println("Format incorrect.\n");
			return null;
		}

		// on regarde si le joueur a rentr� le d�sert
		if (position.equals("DES")) {
			System.out.println();
			return new Paire(3, 3);
		}

		// on parse ce que l'utilisateur a rentre pour obtenir le type de la tuile et le numero du jeton
		String nom = position.substring(0, 2);
		String nb = position.substring(2);
		int jeton = 0;

		// on regarde si l'utilisateur a bien rentre un nombre pour le jeton
		if (!nb.matches("\\d*")) {
			System.out.println("Format incorrect.\n");
			return null;
		}

		// on recupere la position de la tuile dans le plateau
		jeton = Integer.parseInt(nb);
		Paire pa = jeuActuel.getPlateau().position(nom, jeton);

		// on renvoie la position de la tuile si elle existe
		if (pa != null) {
			System.out.println();
			return pa;
		}

		// sinon, le joueur a rentre une tuile inconnue
		System.out.println("Tuile inexistante.\n");
		return null;
	}

	public String demandeDirection(Paire positionTuile, int action, boolean phaseInitiale) {
		// on regarde si l'action donnee est correcte
		if (action != 0 && action != 1 && action != 2)
			throw new IllegalArgumentException("Identifiant d'action inconnu.");

		System.out.println("Dans quelle direction voulez-vous construire ? (Veuillez entrez l'initial de la direction en format cardinal (N pour nord), ou \"retour\" pour revenir � la phase de construction)");
		String d = jeuActuel.sc.next().toUpperCase();

		// on regarde si l'utilisateur a demande de retourner au menu d'action
		if (d.equals("RETOUR")) {
			System.out.println();
			return d;
		}

		// on regarde si l'utilisateur veut construire une route
		if (action == 0) {

			// on regarde s'il lui reste des routes disponibles
			if (nbRoute >= 15) {
				System.out.println("Vous avez atteint le nombre maximum de routes.\n");
				return null;
			}

			// on regarde si les directions correspondent
			if (!d.equals("N") && !d.equals("S") && !d.equals("E") && !d.equals("O")) {
				System.out.println("Direction inconnue\n");
				return null;
			}

			Tuile t = jeuActuel.getPlateau().getTuiles()[positionTuile.x][positionTuile.y];

			// on regarde si la route que le joueur veut construire est deja construite
			if (t.getRoute(d).joueur != null) {
				System.out.println("Cette route est deja occup�e.\n");
				return null;
			}

			// on regarde s'il s'agit de la phase initiale
			if (phaseInitiale) {
				// on r�cup�re les deux intersections en contact avec la route que le joueur veut construire
				Intersection[] intersections = jeuActuel.getPlateau().getAllIntersections(t.getRoute(d));

				// on regarde si une de ces deux intersections appartient au joueur
				if (intersections[0].joueur != this && intersections[1].joueur != this) {
					System.out.println("La route n'est en contact avec aucune de vos intersections.\n");
					return null;
				}

				// on r�cup�re l'intersection construite par le joueur
				Intersection in;
				if (intersections[0].joueur == this)
					in = intersections[0];
				else
					in = intersections[1];

				// on regarde si cette intersection n'a pas d�j� une route construite par le joueur (pendant la phase initiale, il faut construire la route pr�s de la colonie qu'on vient de construire)
				Route[] r = jeuActuel.getPlateau().getAllRoutes(in);
				for (int i = 0; i < r.length; i++) {
					if (r[i] != null && r[i].joueur == this) {
						System.out.println("Pendant la phase initiale, veuillez placer votre route pr�s de la colonie que vous venez juste de construire.\n");
						return null;
					}
				}
			}

			else {
				// on recupere les 6 routes en contact avec la route a construire
				Route[] r = jeuActuel.getPlateau().getAllRoutes(t.getRoute(d));
				boolean enContact = false;

				// on regarde si une de ces routes appartient au joueur
				for (int i = 0; i < r.length; i++) {
					if (r[i].joueur == this)
						enContact = true;
				}

				// si aucune de ces routes n'appartient au joueur, la construction n'est pas possible
				if (!enContact) {
					System.out.println("La construction n'est en contact avec aucune autre de vos constructions.\n");
					return null;
				}
			}
		}

		// on regarde si le joueur veut construire une intersection
		else {
			// on regarde s'il lui reste des colonies disponibles s'il veut construire une colonie
			if (action == 1 && nbColonie >= Jeu.maxColonie) {
				System.out.println("Vous avez atteint le nombre maximum de colonie.\n");
				return null;
			}

			// on regarde s'il lui reste des villes disponibles s'il veut construire une ville
			else if (action == 2 && nbVille >= Jeu.maxVille) {
				System.out.println("Vous avez atteint le nombre maximum de ville.\n");
				return null;
			}

			// on regarde si les directions correspondent
			if (!d.equals("NO") && !d.equals("NE") && !d.equals("SO") && !d.equals("SE")) {
				System.out.println("Direction inconnue.\n");
				return null;
			}

			// on recupere la tuile autour de laquelle on veut construire l'intersection
			Tuile t = jeuActuel.getPlateau().getTuiles()[positionTuile.x][positionTuile.y];

			// si le joueur veut construire une ville
			if (action == 2) {

				// on regarde si l'intersection qu'il a donne contient une de ses colonies
				if (t.getIntersection(d) == null || t.getIntersection(d).joueur != this || t.getIntersection(d).batiment != 1) {
					System.out.println("Impossible de construire une ville dans cette position.\n");
					return null;
				}

				// si oui, on renvoie ce que le joueur a entre
				System.out.println();
				return d;
			}

			// s'il veut construire une colonie, on regarde si l'intersection est deja construite
			if (t.getIntersection(d).joueur != null) {
				System.out.println("Cette intersection est d�j� occup�e.\n");
				return null;
			}

			// si ce n'est pas le debut de la partie
			if (!phaseInitiale) {

				// on recupere les 4 routes qui menent a l'intersection que le joueur veut construire
				Route[] routes = jeuActuel.getPlateau().getAllRoutes(t.getIntersection(d));
				boolean enContact = false;

				// on regarde si une de ces routes appartient au joueur
				for (int i = 0; i < routes.length; i++) {
					if (routes[i].joueur == this)
						enContact = true;
				}

				// si aucune de ces routes n'appartient au joueur, il ne peut pas construire
				if (!enContact) {
					System.out.println("La construction n'est en contact avec aucune autre de vos construction.\n");
					return null;
				}
			}

			// on regarde si la regle de distance est respecte
			if (!jeuActuel.getPlateau().respecteRegleDistance(t.getIntersection(d))) {
				System.out.println("La r�gle de distance n'est pas respect�e.\n");
				return null;
			}
		}

		// toutes les conditions de constructions sont correctes, on renvoie ce que l'utilisateur a entre
		System.out.println();
		return d;
	}

	// type = 0 marchander, type = 1 7 au de (pas de retour en arriere)
	public String demandeRessourceADonner(int type) {
		// on regarde s'il s'agit d'un type accepte
		if (type != 0 && type != 1)
			throw new IllegalArgumentException("Type inconnu");

		// on regarde si le joueur peut revenir en arriere (s'il est en train de marchander ou s'il doit donner des ressources du a un 7 au de)
		if (type == 0)
			System.out.println("Quel ressource voulez-vous donner ? (Veuillez entrer le nom entier de la ressource � donner, ou \"retour\" pour revenir au menu d'action)");
		else
			System.out.println("Quel ressource voulez-vous donner ? (Veuillez entrer le nom entier de la ressource � donner)");

		String ressourceADonner = jeuActuel.sc.next().toLowerCase();

		// on regarde si le joueur est en train de marchander
		if (type == 0) {

			// on regarde s'il a demande de retourner au menu d'action
			if (ressourceADonner.equals("retour")) {
				System.out.println();
				return ressourceADonner;
			}

			// selon son taux d'echange, on regarde s'il a assez de cette ressource
			if (ressources.get(ressourceADonner) < tauxEchange.get(ressourceADonner)) {
				System.out.println("Nombre insuffisant de ressource.\n");
				return null;
			}
		}

		// on regarde si le joueur a rentre un nom de ressource correct
		if (!tauxEchange.keySet().contains(ressourceADonner)) {
			System.out.println("Ressource inconnue.\n");
			return null;
		}

		// toutes les conditions sont remplis, on renvoie ce que le joueur a rentre
		System.out.println();
		return ressourceADonner;
	}

	// type = 0 marchander, type = 1 carte invention (pas de retour en arriere)
	public String demandeRessourceARecevoir(int type) {
		// on regarde s'il s'agit d'un type accept�
		if (type != 0 && type != 1)
			throw new IllegalArgumentException("Type inconnu");

		// on affiche le message correspondant
		if (type == 0)
			System.out.println("Quel ressource souhaitez-vous recevoir ? (Veuillez entrer le nom entier de la ressource voulue, ou \"retour\" pour revenir au menu d'action)");
		else
			System.out.println("Quel ressource souhaitez-vous recevoir ? (Veuillez entrer le nom entier de la ressource voulue)");

		// on r�cup�re ce que le joueur a entre
		String ressourceARecevoir = jeuActuel.sc.next().toLowerCase();

		// s'il est en train de marchander, on regarde s'il a demande de revenir en arriere
		if (type == 0 && ressourceARecevoir.equals("retour")) {
			System.out.println();
			return ressourceARecevoir;
		}

		// on regarde sinon si le joueur a entre un nom de ressource correct
		if (!tauxEchange.keySet().contains(ressourceARecevoir)) {
			System.out.println("Ressource inconnue.\n");
			return null;
		}

		// si oui, on renvoie ce qu'il a entr�
		System.out.println();
		return ressourceARecevoir;
	}

	public Joueur demandeQuelJoueur(LinkedList<Joueur> joueurs) {
		// on affiche les joueurs possibles
		System.out.println("De quel joueur voulez-vous prendre une ressource ? (Veuillez entrer le nom entier du joueur voulu)");
		for (Joueur j : joueurs)
			System.out.println("  * " + j.nom + " (" + j.getNbRessource() + " ressources)");

		String nom = jeuActuel.sc.next().toLowerCase();

		// on regarde s'il s'agit d'un nom d'un joueur possible
		for (Joueur j : joueurs) {

			// si oui, on renvoie ce que le joueur avait entre
			if (j.nom.toLowerCase().equals(nom)) {
				System.out.println();
				return j;
			}
		}

		// sinon, il a entre quelque chose d'inconnu
		System.out.println("Nom inconnu.\n");
		return null;
	}
}

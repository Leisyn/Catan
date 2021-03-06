package catan.model.player;

import java.util.LinkedList;

import catan.model.Game;
import catan.model.board.Buildable;
import catan.model.other.Construction;
import catan.model.board.Intersection;
import catan.model.board.Path;
import catan.model.board.Tile;
import catan.model.card.Card;
import catan.model.card.KnightCard;
import catan.model.card.ProgressCard;
import catan.model.other.Action;
import catan.model.other.Pair;
import catan.model.other.Resource;

public class Human extends Player {
	public Human(String s, Game g) {
		super(s, g);
	}

	public boolean producingPhase() {
		System.out.println("===================");
		System.out.println("| PRODUCING PHASE |");
		System.out.println("===================\n");

		// on affiche ce que le joueur peut faire
		System.out.println("Vous pouvez :");
		printPlayableCards();
		System.out.println("  * (L) Lancer un d�\n");

		// on demande ce que le joueur veut faire
		String action = null;
		while (action == null)
			action = askProducingPhase();
		System.out.println();

		// on regarde si le joueur a demande de lancer le de
		if (action.equals("L"))
			return true;

		// on regarde si le joueur a demand� de jouer une carte et on renvoie s'il a gagn� ou non
		return playACard();
	}


	// Joue une carte du joueur et renvoie si le joueur a gagn� ou non
	public boolean playACard() {
		afficheCartesJouables();

		// on demande la carte que le joueur veut jouer
		String carte = null;
		while (carte == null)
			carte = askCardToPlay();

		// on regarde si le joueur a demande de revenir
		if (carte.equals("retour"))
			return hasWon();

		// sinon, on la retire des cartes actuelles du joueur et on la joue
		for (Card c : cards) {
			if (c.name.equals(carte)) {
				cards.remove(c);
				c.play(actualGame, this);
				return hasWon();
			}
		}

		return hasWon();
	}

	public boolean tradingPhase() {
		System.out.println("=================");
		System.out.println("| TRADING PHASE |");
		System.out.println("=================\n");

		// on affiche ce que le joueur peut faire
		System.out.println("Vous pouvez :");
		printPlayableCards();
		System.out.println("  * (M) Marchander");
		System.out.println("  * (P) Passer � la phase de construction\n");

		// on demande ce que le joueur veut faire
		String action = null;
		while (action == null)
			action = askTradingPhase();
		System.out.println();

		// on regarde si le joueur a demande de passer a la phase de construction
		if (action.equals("P"))
			return true;

		// on regarde si le joueur a demande de jouer une carte, et on renvoie s'il a gagn� ou non
		if (action.equals("J"))
			return playACard();

		// le joueur a demand� de marchander
		trade();

		// on renvoie si le joueur a gagn� ou non
		return hasWon();
	}

	public void trade() {
		Resource resourceToGive = null;
		Resource resourceToReceive = null;

		// on affiche les taux d'echanges du joueur
		printTradeRate();

		// on demande ce que le joueur veut echanger
		while (resourceToGive == null) resourceToGive = askResourceToGive(0);
		System.out.println();

		// on regarde s'il a demande de retourner au menu d'action
		// if (resourceToGive.equals("retour"))
		//	 return;

		// on demande contre quoi le joueur veut echanger
		while (resourceToReceive == null) resourceToReceive = askResourceToReceive(0);
		System.out.println();

		// on regarde s'il a demande de retourner au menu d'action
		// if (resourceToReceive.equals("retour"))
		// 	return;

		// sinon, on procede a l'echange
		loseResources(resourceToGive, tradeRate.get(resourceToGive));
		receiveResources(resourceToReceive, 1);
	}

	public boolean buildingPhase() {
		System.out.println("==================");
		System.out.println("| BUILDING PHASE |");
		System.out.println("==================\n");

		// on affiche ce que le joueur peut faire
		System.out.println("Vous pouvez actuellement : ");
		printPossibleActions();

		// on demande ce que le joueur veut faire
		Action action = null;
		while (action == null) action = askBuildingPhase();
		System.out.println();

		// on effectue l'action demandee
		switch (action) {
			case BUILDROAD: return build(Construction.ROAD, false);
			case BUILDSETTLEMENT: return build(Construction.SETTLEMENT, false);
			case BUILDCITY: return build(Construction.CITY, false);
			case BUYCARD: return buyACard();
			case PLAYCARD: return playACard();
			default: return true;
		}
	}

	// Demande au joueur la position o� il souhaite construire (0 : route / 1 : colonie / 2 : ville) et renvoie s'il a gagn� ou non
	public boolean build(Construction c, boolean phaseInitiale) {
		if (c == Construction.NOTHING) return hasWon();

		if (c == Construction.ROAD && noRoadBuilt >= Game.maxAmountOfRoadForEachPlayer) {
			System.out.println("Vous avez atteint le nombre maximum de routes.");
			return hasWon();
		}

		if (c == Construction.SETTLEMENT && noSettlementBuilt >= Game.maxAmountOfSettlementForEachPlayer) {
			System.out.println("Vous avez atteint le nombre maximum de colonies.");
			return hasWon();
		}

		if (c == Construction.CITY && noCityBuilt >= Game.maxAmountOfCityForEachPlayer) {
			System.out.println("Vous avez atteint le nombre maximum de villes.");
			return hasWon();
		}

		Pair position = null;
		String direction = null;

		// on demande a l'utilisateur la tuile autour duquelle il veut construire
		while (position == null)
			position = askPosition(0, c, phaseInitiale);

		// on regarde s'il a demande de revenir
		if (position.x == -1 && position.y == -1)
			return hasWon();

		// on demande la direction dans laquelle il veut construire
		while (direction == null)
			direction = askDirection(position, c, phaseInitiale);

		// on regarde s'il a demande de revenir
		if (direction.equals("RETOUR"))
			return hasWon();

		// sinon, on construit
		Buildable construction = actualGame.getBoard().getConstruction(position, direction);
		construction.build(actualGame, this, c);

		// on renvoie si le joueur a gagne ou non
		return hasWon();
	}





	// METHODES D'AFFICHAGE

	// Affiche si le joueur peut jouer une carte
	public void printPlayableCards() {
		for (Card c : cards) {
			if (c instanceof KnightCard || c instanceof ProgressCard) {
				System.out.println("  * (J) Jouer une carte");
				return;
			}
		}
	}

	// Affiche les cartes jouables du joueur
	public void afficheCartesJouables() {
		for (Card c : cards) {
			if (c instanceof KnightCard || c instanceof ProgressCard)
				System.out.println("  * " + c);
		}
		System.out.println();
	}

	// Affiche ce que le joueur peut faire pendant la phase de construction
	public void printPossibleActions() {
		// on recupere son amount actuel de resource
		int laine = resources.get(Resource.WOOL);
		int bois = resources.get(Resource.LUMBER);
		int argile = resources.get(Resource.BRICK);
		int ble = resources.get(Resource.GRAIN);
		int minerai = resources.get(Resource.ORE);

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
		printPlayableCards();

		System.out.println("  * (P) Passer son tour\n");
	}

	// Affiche les ressources
	public void printResources() {
		System.out.println("Ressources actuelles :");
		for (Resource r : resources.keySet())
			System.out.println("  * " + resources.get(r) + " " + r);
		System.out.println();
	}

	public void printPoints() {
		System.out.println("Vous avez " + victoryPoints + " points de victoire.\n");
	}

	public void printCards() {
		if (cards.size() != 0) {
			System.out.println("\nCartes actuelles : ");
			for (Card c : cards)
				System.out.println("  * " + c);
		}
		System.out.println();
	}

	public void printTradeRate() {
		System.out.println("Taux d'�change :");

		for (Resource r : tradeRate.keySet()) {
			String avecMaj = r.toString().toUpperCase().charAt(0) + r.toString().substring(1);
			System.out.println("  * " + avecMaj + " : " + tradeRate.get(r) + ":1");
		}

		System.out.println();
	}





	// METHODES DE DEMANDE AU JOUEUR

	public String askProducingPhase() {
		System.out.println("Que voulez-vous faire ? (Veuillez entrez la lettre capitale se trouvant avant l'action voulue)");
		String action = actualGame.sc.next().toUpperCase();

		// on regarde si le joueur a demande de passer a la prochaine phase
		if (action.equals("L"))
			return action;

		// on regarde si le joueur a demande de jouer une carte
		if (action.equals("J")) {

			// on regarde s'il a une carte jouable
			for (Card c : cards) {
				if (c instanceof KnightCard || c instanceof ProgressCard)
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

	public String askCardToPlay() {
		System.out.println("Quelle carte voulez-vous jouer ? (Entrez le nom de la carte � jouer, ou \"retour\" pour revenir)");
		String nomCarte = actualGame.sc.next().toLowerCase();

		// on regarde s'il s'agit d'un nom de carte jouable correcte, ou de "retour"
		if (!nomCarte.equals("retour") && !nomCarte.equals("chevalier") && !nomCarte.equals("construction") && !nomCarte.equals("monopole") && !nomCarte.equals("invention")) {
			System.out.println("Nom inconnu.\n");
			return null;
		}

		// on regarde s'il s'agit d'une carte que le joueur possede
		for (Card c : cards) {
			if (c.name.equals(nomCarte))
				return nomCarte;
		}

		// le joueur a rentre une carte qu'il ne possede pas
		System.out.println("Vous ne possedez pas cette carte.\n");
		return null;
	}

	private String askTradingPhase() {
		System.out.println("Que voulez-vous faire ? (Veuillez entrez la lettre capitale se trouvant avant l'action voulue)");
		String action = actualGame.sc.next().toUpperCase();

		// on regarde s'il a demande de marchander, ou de changer de phase
		if (action.equals("P") || action.equals("M"))
			return action;

		// on regarde s'il a demande d'utiliser une carte
		if (action.equals("J")) {

			// on regarde s'il a une carte jouable
			for (Card c : cards) {
				if (c instanceof KnightCard || c instanceof ProgressCard)
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

	private Action askBuildingPhase() {
		System.out.println("Que voulez-vous faire ? (Veuillez entrez la lettre capitale se trouvant avant l'action voulue)");
		String answer = actualGame.sc.next().toUpperCase();
		Action action = Action.toAction(answer);

		// on regarde s'il a demande de passer son tour
		if (action == Action.ENDTURN) return action;

		// on regarde si le joueur a demande une action coutant des ressources
		if (action == Action.BUILDROAD || action == Action.BUILDSETTLEMENT || action == Action.BUILDCITY || action == Action.BUYCARD) {

			// on regarde s'il a les ressources necessaires
			if (!hasTheResourcesTo(action)) {
				System.out.println("Ressources insuffisantes.\n");
				return null;
			}

			// si oui, on renvoie ce que le joueur a rentre
			return action;
		}

		// on regarde s'il a demande d'utiliser une carte
		if (action == Action.PLAYCARD) {

			// on regarde s'il a une carte jouable
			for (Card c : cards) {
				if (c instanceof KnightCard || c instanceof ProgressCard)
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
	public Pair askPosition(int type, Construction c, boolean phaseInitiale) {
		// on regarde s'il faut demander la tuile pour construire, ou la tuile pour placer le voleur
		if (type == 1)
			System.out.println("Sur quelle tuile voulez-vous placer le voleur ? (Veuillez entrer le nom de la tuile sous le format \"FO 6\", ou \"DES\" pour le d�sert)");

		else {
			if (phaseInitiale) {
				if (c == Construction.ROAD)
					System.out.println("Autour de quelle tuile voulez-vous construire votre route ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert)");
				else if (c == Construction.SETTLEMENT)
					System.out.println("Autour de quelle tuile voulez-vous construire votre colonie ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert)");
				else if (c == Construction.CITY)
					System.out.println("Autour de quelle tuile se trouve la colonie que vous souhaitez transformer en ville ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert)");
			}

			else {
				if (c == Construction.ROAD)
					System.out.println("Autour de quelle tuile voulez-vous construire votre route ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert, ou \"retour\" pour revenir)");
				else if (c == Construction.SETTLEMENT)
					System.out.println("Autour de quelle tuile voulez-vous construire votre colonie ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert, ou \"retour\" pour revenir)");
				else if (c == Construction.CITY)
					System.out.println("Autour de quelle tuile se trouve la colonie que vous souhaitez transformer en ville ? (Veuillez entrer le nom de la tuile sous le format \"FO6\", ou \"DES\" pour le d�sert, ou \"retour\" pour revenir)");
			}
		}

		// on recupere ce que l'utilisateur a rentre
		String position = actualGame.sc.next().toUpperCase();

		// si l'utilisateur ne doit pas placer le voleur, on regarde s'il a demande de retourner au menu d'action
		if ((type != 1 || !phaseInitiale) && position.equals("retour"))
			return new Pair(-1, -1);

		// on regarde si le joueur a rentr� uniquement 3 lettres
		if (position.length() != 3 && position.length() != 4) {
			System.out.println("Format incorrect.\n");
			return null;
		}

		// on regarde si le joueur a rentr� le d�sert
		if (position.equals("DES")) {
			System.out.println();
			return new Pair(3, 3);
		}

		// on parse ce que l'utilisateur a rentre pour obtenir le type de la tuile et le noero du jeton
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
		Pair pa = actualGame.getBoard().position(nom, jeton);

		// on renvoie la position de la tuile si elle existe
		if (pa != null) {
			System.out.println();
			return pa;
		}

		// sinon, le joueur a rentre une tuile inconnue
		System.out.println("Tuile inexistante.\n");
		return null;
	}

	public String askDirection(Pair positionTuile, Construction c, boolean phaseInitiale) {
		if (c == Construction.NOTHING) return null;

		System.out.println("Dans quelle direction voulez-vous construire ? (Veuillez entrez l'initial de la direction en format cardinal (N pour nord), ou \"retour\" pour revenir � la phase de construction)");
		String d = actualGame.sc.next().toUpperCase();

		// on regarde si l'utilisateur a demande de retourner au menu d'action
		if (d.equals("RETOUR")) {
			System.out.println();
			return d;
		}

		// on regarde si l'utilisateur veut construire une route
		if (c == Construction.ROAD) {

			// on regarde s'il lui reste des routes disponibles
			if (noRoadBuilt >= 15) {
				System.out.println("Vous avez atteint le nombre maximum de routes.\n");
				return null;
			}

			// on regarde si les directions correspondent
			if (!d.equals("N") && !d.equals("S") && !d.equals("E") && !d.equals("O")) {
				System.out.println("Direction inconnue\n");
				return null;
			}

			Tile t = actualGame.getBoard().getTiles()[positionTuile.x][positionTuile.y];

			// on regarde si la route que le joueur veut construire est deja construite
			if (t.getPath(d).player != null) {
				System.out.println("Cette route est deja occup�e.\n");
				return null;
			}

			// on regarde s'il s'agit de la phase initiale
			if (phaseInitiale) {
				// on r�cup�re les deux intersections en contact avec la route que le joueur veut construire
				Intersection[] intersections = actualGame.getBoard().getAllIntersectionsInContactWith(t.getPath(d));

				// on regarde si une de ces deux intersections appartient au joueur
				if (intersections[0].player != this && intersections[1].player != this) {
					System.out.println("La route n'est en contact avec aucune de vos intersections.\n");
					return null;
				}

				// on r�cup�re l'intersection construite par le joueur
				Intersection in;
				if (intersections[0].player == this)
					in = intersections[0];
				else
					in = intersections[1];

				// on regarde si cette intersection n'a pas d�j� une route construite par le joueur (pendant la phase initiale, il faut construire la route pr�s de la colonie qu'on vient de construire)
				Path[] r = actualGame.getBoard().getAllPathsInContactWith(in);
				for (int i = 0; i < r.length; i++) {
					if (r[i] != null && r[i].player == this) {
						System.out.println("player la phase initiale, veuillez placer votre route pr�s de la colonie que vous venez juste de construire.\n");
						return null;
					}
				}
			}

			else {
				// on recupere les 6 routes en contact avec la route a construire
				Path[] r = actualGame.getBoard().getAllPathsInContactWith(t.getPath(d));
				boolean enContact = false;

				// on regarde si une de ces routes appartient au joueur
				for (int i = 0; i < r.length; i++) {
					if (r[i].player == this)
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
			if (c == Construction.SETTLEMENT && noSettlementBuilt >= Game.maxAmountOfSettlementForEachPlayer) {
				System.out.println("Vous avez atteint le nombre maximum de colonie.\n");
				return null;
			}

			// on regarde s'il lui reste des villes disponibles s'il veut construire une ville
			else if (c == Construction.CITY && noCityBuilt >= Game.maxAmountOfCityForEachPlayer) {
				System.out.println("Vous avez atteint le nombre maximum de ville.\n");
				return null;
			}

			// on regarde si les directions correspondent
			if (!d.equals("NO") && !d.equals("NE") && !d.equals("SO") && !d.equals("SE")) {
				System.out.println("Direction inconnue.\n");
				return null;
			}

			// on recupere la tuile autour de laquelle on veut construire l'intersection
			Tile t = actualGame.getBoard().getTiles()[positionTuile.x][positionTuile.y];

			// si le joueur veut construire une ville
			if (c == Construction.CITY) {

				// on regarde si l'intersection qu'il a donne contient une de ses colonies
				if (t.getIntersection(d) == null || t.getIntersection(d).player != this || t.getIntersection(d).construction != Construction.SETTLEMENT) {
					System.out.println("Impossible de construire une ville dans cette position.\n");
					return null;
				}

				// si oui, on renvoie ce que le joueur a entre
				System.out.println();
				return d;
			}

			// s'il veut construire une colonie, on regarde si l'intersection est deja construite
			if (t.getIntersection(d).player != null) {
				System.out.println("Cette intersection est d�j� occup�e.\n");
				return null;
			}

			// si ce n'est pas le debut de la partie
			if (!phaseInitiale) {

				// on recupere les 4 routes qui menent a l'intersection que le joueur veut construire
				Path[] routes = actualGame.getBoard().getAllPathsInContactWith(t.getIntersection(d));
				boolean enContact = false;

				// on regarde si une de ces routes appartient au joueur
				for (int i = 0; i < routes.length; i++) {
					if (routes[i].player == this)
						enContact = true;
				}

				// si aucune de ces routes n'appartient au joueur, il ne peut pas construire
				if (!enContact) {
					System.out.println("La construction n'est en contact avec aucune autre de vos construction.\n");
					return null;
				}
			}

			// on regarde si la regle de distance est respecte
			if (!actualGame.getBoard().respectDistanceRule(t.getIntersection(d))) {
				System.out.println("La r�gle de distance n'est pas respect�e.\n");
				return null;
			}
		}

		// toutes les conditions de constructions sont correctes, on renvoie ce que l'utilisateur a entre
		System.out.println();
		return d;
	}

	// type = 0 marchander, type = 1 7 au de (pas de retour en arriere)
	public Resource askResourceToGive(int type) {
		// on regarde s'il s'agit d'un type accepte
		if (type != 0 && type != 1)
			throw new IllegalArgumentException("Type inconnu");

		// on regarde si le joueur peut revenir en arriere (s'il est en train de marchander ou s'il doit donner des ressources du a un 7 au de)
		if (type == 0)
			System.out.println("Quel resource voulez-vous donner ? (Veuillez entrer le nom entier de la resource � donner, ou \"retour\" pour revenir au menu d'action)");
		else
			System.out.println("Quel resource voulez-vous donner ? (Veuillez entrer le nom entier de la resource � donner)");

		String answer = actualGame.sc.next().toLowerCase();
		Resource resource = Resource.toResource(answer);

		// on regarde si le joueur est en train de marchander
		if (type == 0) {

			// on regarde s'il a demande de retourner au menu d'action
			if (answer.equals("retour")) {
				System.out.println();
				return null;
			}

			if (resource != null && (resources.get(resource) < tradeRate.get(resource))) {
				System.out.println("Nombre insuffisant de resource.\n");
				return null;
			}
		}

		// on regarde si le joueur a rentre un nom de resource correct
		if (!tradeRate.keySet().contains(resource)) {
			System.out.println("Ressource inconnue.\n");
			return null;
		}

		// toutes les conditions sont remplis, on renvoie ce que le joueur a rentre
		System.out.println();
		return resource;
	}

	// type = 0 marchander, type = 1 carte invention (pas de retour en arriere)
	public Resource askResourceToReceive(int type) {
		// on regarde s'il s'agit d'un type accept�
		if (type != 0 && type != 1)
			throw new IllegalArgumentException("Type inconnu");

		// on affiche le message correspondant
		if (type == 0)
			System.out.println("Quel resource souhaitez-vous recevoir ? (Veuillez entrer le nom entier de la resource voulue, ou \"retour\" pour revenir au menu d'action)");
		else
			System.out.println("Quel resource souhaitez-vous recevoir ? (Veuillez entrer le nom entier de la resource voulue)");

		// on r�cup�re ce que le joueur a entre
		String answer = actualGame.sc.next().toLowerCase();
		Resource resource = Resource.toResource(answer);

		// s'il est en train de marchander, on regarde s'il a demande de revenir en arriere
		if (type == 0 && answer.equals("retour")) {
			System.out.println();
			return null;
		}

		// on regarde sinon si le joueur a entre un nom de resource correct
		if (!tradeRate.keySet().contains(resource)) {
			System.out.println("Ressource inconnue.\n");
			return null;
		}

		// si oui, on renvoie ce qu'il a entr�
		System.out.println();
		return resource;
	}

	public Human askWhichPlayer(LinkedList<Human> joueurs) {
		// on affiche les joueurs possibles
		System.out.println("De quel joueur voulez-vous prendre une resource ? (Veuillez entrer le nom entier du joueur voulu)");
		for (Human j : joueurs)
			System.out.println("  * " + j.name + " (" + j.getNoOfResources() + " ressources)");

		String nom = actualGame.sc.next().toLowerCase();

		// on regarde s'il s'agit d'un nom d'un joueur possible
		for (Human j : joueurs) {

			// si oui, on renvoie ce que le joueur avait entre
			if (j.name.toLowerCase().equals(nom)) {
				System.out.println();
				return j;
			}
		}

		// sinon, il a entre quelque chose d'inconnu
		System.out.println("Nom inconnu.\n");
		return null;
	}

	@Override
	public String askResourceToTrade() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String askCombinedTradeBuildPhase() {
		// TODO Auto-generated method stub
		return null;
	}
}

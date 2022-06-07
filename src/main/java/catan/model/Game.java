package catan.model;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import catan.model.board.Intersection;
import catan.model.board.Board;
import catan.model.board.Buildable.Construction;
import catan.model.board.Tile;
import catan.model.card.Card;
import catan.model.card.KnightCard;
import catan.model.card.ProgressCard;
import catan.model.card.VictoryCard;
import catan.model.other.Pair;
import catan.model.player.Player;
import catan.model.player.Player.Resource;

public class Game {
	private Board board;
	private Player[] players;
	
	public Scanner sc;
	public LinkedList<Card> availableCards = new LinkedList<>();
	
	public Player playerWithLongestRoad;
	public Player playerWithLargestArmy;
	
	public static int maxAmountOfRoadForEachPlayer = 15;
	public static int maxAmountOfSettlementForEachPlayer = 5;
	public static int maxAmountOfCityForEachPlayer = 4;
	
	public static int minAmountOfRoadToGetLongestRoad = 5;
	public static int minAmountOfArmyToGetLargestArmy = 3;
	
	public Game(int n, Scanner s) {
		board = Board.iniBoard();
		players = new Player[n];
		
		sc = s;
		iniAvailableCards();
		
		playerWithLongestRoad = null;
		playerWithLargestArmy = null;
	}
	
	public void iniPlayers(LinkedList<Player> p) {
		// on verifie que la liste donnee contient le nombre de joueurs correspondant au jeu
		if (players.length != p.size())
			throw new IllegalArgumentException("The number of given players is different from the number of players awaited.");
		
		// on tire les joueurs aleatoirement, afin que le joueur qui commence soit aleatoire
		Random rd = new Random();

		for (int i = 0; i < players.length; i++) {
			int n = rd.nextInt(p.size());
			players[i] = p.get(n);
			p.remove(n);
		}
	}
	
	// Initialise les cartes disponibles a l'achat
	private void iniAvailableCards() {
		// 14 cartes chevalier
		for (int i = 0; i < 14; i++)
			availableCards.add(new KnightCard());
		
		// 6 cartes progres, 2 de chaque
		for (int i = 0; i < 2; i++)
			availableCards.add(new ProgressCard("construction"));
		for (int i = 0; i < 2; i++)
			availableCards.add(new ProgressCard("invention"));
		for (int i = 0; i < 2; i++)
			availableCards.add(new ProgressCard("monopole"));
		
		// 5 cartes victoires, 5 de chaque
		availableCards.add(new VictoryCard("bibliotheque"));
		availableCards.add(new VictoryCard("marche"));
		availableCards.add(new VictoryCard("parlement"));
		availableCards.add(new VictoryCard("eglise"));
		availableCards.add(new VictoryCard("universite"));
	}
	
	public Board getBoard() {
		return board;
	}

	public Player[] getPlayers() {
		return players;
	}

	
	
	
	
	public void assignLongestRoad() {
		// on recupere le nombre minimum de routes requis pour etre attribue la route la plus longue
		int max = minAmountOfRoadToGetLongestRoad;
		
		// on recupere le joueur qui a la route la plus longue
		Player avant = playerWithLongestRoad;
				
		// on attribue la route la plus longue au joueur ayant la plus longue route, atteignant au moins le nombre requis
		for (int i = 0; i < players.length; i++) {
			if (players[i].longestRoad > max) {
				max = players[i].longestRoad;
				playerWithLongestRoad = players[i];
			}
		}
		
		// si le joueur qui a la route la plus longue n'est plus le meme
		if (playerWithLongestRoad != avant) {
			
			// on enleve 2 points au joueur qui avait la route la plus longue
			if (avant != null)
				avant.points -= 2;
			
			// on ajoute 2 points au joueur qui a la route la plus longue
			if (playerWithLongestRoad != null)
				playerWithLongestRoad.points += 2;
		}
	}
	
	public void assignLargestArmy() {
		// on recupere le nombre minimum d'armee requis pour etre attribue l'armee la plus puissante
		int max = minAmountOfArmyToGetLargestArmy;
		
		// on recupere le joueur qui a l'armee la plus puissante
		Player avant = playerWithLargestArmy;
				
		// on attribue l'armee la plus puissante au joueur ayant joue le plus de cartes chevalier, atteignant au moins le nombre requis
		for (int i = 0; i < players.length; i++) {
			if (players[i].largestArmy > max) {
				max = players[i].largestArmy;
				playerWithLargestArmy = players[i];
			}
		}
		
		// si le joueur qui a l'armee la plus puissante n'est plus le meme
		if (playerWithLargestArmy != avant) {
			
			// on enleve 2 points au joueur qui avait l'armee la plus puissante
			if (avant != null)
				avant.points -= 2;
			
			// on ajoute 2 points au joueur qui a l'armee la plus puissante
			if (playerWithLargestArmy != null)
				playerWithLargestArmy.points += 2;
		}
	}
	
	public void game() {
		// on lance la phase initiale du jeu
		setupPhase();
		
		// on lance le tour de chacun des joueurs, jusqu'a ce qu'un des joueurs gagne
		int i = 0;
		while (!oneTurn(players[i])) {
			if (i == players.length - 1) i = 0;
			else i++;
		}
		
		// on affiche le gagnant
		System.out.println(players[i].name + " has won!");
	}
	
	// Lance la phase initiale du jeu
	private void setupPhase() {
		// 1er tour
		for (int i = 0; i < players.length; i++) {
			printTurnBanner(players[i]);
			board.printBoard();
			while (players[i].getNumSettlement() != 1)
				players[i].build(Construction.SETTLEMENT, true);  // on demande au joueur de placer une colonie
			board.printBoard();
			while (players[i].getNumRoad() != 1)
				players[i].build(Construction.ROAD, true);  // on demande au joueur de placer une route pres de la colonie qu'il vient de placer
		}
		
		// 2e tour
		for (int i = players.length - 1; i >= 0; i--) {
			printTurnBanner(players[i]);
			board.printBoard();
			while (players[i].getNumSettlement() != 2)
				players[i].build(Construction.SETTLEMENT, true);  // on demande au joueur de placer une colonie
			board.printBoard();
			while (players[i].getNumRoad() != 2)
				players[i].build(Construction.ROAD, true);  // on demande au joueur de placer une route pres de la colonie qu'il vient de placer
		}
	}
	
	private void printTurnBanner(Player p) {
		String s = "";
		for (int i = 0; i < p.name.length(); i++)
			s += "=";
		
		System.out.println("=========" + s + "==");
		System.out.println("| TURN OF " + p.name.toUpperCase() + " |");
		System.out.println("=========" + s + "==\n");
	}
	
	// Effectue un tour de jeu du joueur et renvoie si le joueur a gagne
	public boolean oneTurn(Player p) {
		// on affiche le nom du joueur actuel
		printTurnBanner(p);
		
		// on affiche l'etat du plateau
		board.printBoard();
		
		// on affiche ses points et ses cartes actuelles
		p.printCards();
		p.printPoints();
		
		// on lance sa phase de production
		boolean next = false;
		while (next == false) next = p.producingPhase();
		
		// on regarde si le joueur a gagne
		if (p.hasWon()) return true;
		
		// on lance le de et on effectue l'action correspondante
		int n = throwDice();
		System.out.println("You threw a " + n + ".");
		
		if (n == 7) sevenOnDice(p);
		else giveResources(n, p);
		
		// on affiche ses ressources et ses points
		p.printResources();
		p.printPoints();
		
		// on lance sa phase de commerce
		next = false;
		while (next == false) next = p.tradingPhase();
		
		// on regarde si le joueur a gagne
		if (p.hasWon()) return true;
		
		// on lance sa phase de construction
		next = false;
		while (next == false) next = p.buildingPhase();
		
		// on renvoie si le joueur a gagne
		return p.hasWon();
	}
	
	public int throwDice() {
		Random rd = new Random();
		return rd.nextInt(11) + 2;
	}
	
	private void sevenOnDice(Player p) {
		System.out.println("\n=================");
		System.out.println("| SEVEN ON DICE |");
		System.out.println("=================\n");
		
		// on regarde si un joueur a plus de 7 ressources
		for (Player pl : players) {
			if (pl.getNumResources() > 7) {
				// si oui, il doit se defausser de la moitie de ses ressources
				int n = pl.getNumResources() / 2;
				if (pl == p) System.out.println("You have more than 7 resources, please throw " + n + " resources.\n");
				else System.out.println(pl.name + " has more than 7 resources, he has to throw " + n + " resources.\n");
				
				// on affiche ses ressources
				pl.printResources();
				
				// on lui demande de se defausser d'une ressource a la fois
				for (int i = 0; i < n; i++) {
					Resource resource = null;
					while (resource == null) resource = pl.askResourceToGive(1);
					pl.loseResources(resource, 1);
				}
			}
		}
		// on deplace alors le voleur
		onTheRobber(p);
	}
	
	public void onTheRobber(Player p) {
		Pair pair = null;
		
		// on demande sur quelle tuile le joueur veut deplacer le voleur
		while (pair == null) {
			pair = p.askPosition(1, Construction.NOTHING, true);
			
			// on verifie que le voleur n'est pas deja sur la tuile (d'apres les regles, il est obligatoire de deplacer le voleur)
			if (board.getTiles()[pair.x][pair.y].robberIsHere) {
				System.out.println("The robber is already on this tile.\n");
				pair = null;
			}
		}
		
		// on deplace le voleur a la position
		board.removeRobber();
		board.placeRobber(pair);
		
		// on regarde s'il y a des intersections construites autour de la nouvelle position du voleur
		LinkedList<Intersection> intersections = board.getTiles()[pair.x][pair.y].getAllBuiltIntersections();
		
		if (!intersections.isEmpty()) {
			// on recupere tous les joueurs adverses qui ont une intersection construite autour de la nouvelle position du voleur
			LinkedList<Player> players = new LinkedList<>();
			
			for (Intersection in : intersections) {
				if (!players.contains(in.player) && in.player != p)
					players.add(in.player);
			}
			
			// on demande au joueur a quel joueur adverse il veut aleatoirement prendre une ressource
			Player opponent = null;
			while (opponent == null) opponent = p.askWhichPlayer(players);
			
			// on recupere une ressource du joueur adverse de facon aleatoire
			Resource resource = opponent.loseARandomResource();
			
			// s'il n'avait aucune ressource, on l'annonce
			if (resource == null) System.out.println("This player has no resources.\n");
			
			// sinon, on donne la ressource recupere au joueur actuel
			else p.receiveResource(resource, 1);
		}
	}
	
	public void giveResources(int n, Player p) {
		Tile[][] t = board.getTiles();
		
		// on parcourt le plateau
		for (int i = 0; i < t.length; i++) {
			for (int j = 0; j < t[i].length; j++) {
				
				// si le jeton de la tuile correspond et qu'il n'y a pas de voleur
				if (t[i][j].numToken == n && !t[i][j].robberIsHere) {
					
					// on recupere toutes les intersections construites autour et on donne les ressources selon le type de batiment construit
					for (Intersection in : t[i][j].getAllBuiltIntersections()) {
						Resource resource = null;
						
						switch (t[i][j].type) {
							case HILLS: resource = Resource.BRICK; break; 
							case FOREST: resource = Resource.LUMBER; break;
							case MOUNTAINS: resource = Resource.ORE; break;
							case FIELDS: resource = Resource.GRAIN; break;
							case PASTURE: resource = Resource.WOOL; break;
							default: resource = null;
						}
						
						if (resource != null) {
							if (in.player == p) System.out.println("You get " + in.construction + " " + resource + ".");
							else System.out.println(in.player.name + " get " + in.construction + " " + resource + ".");
							in.player.receiveResource(resource, in.construction.ordinal() - 1);
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

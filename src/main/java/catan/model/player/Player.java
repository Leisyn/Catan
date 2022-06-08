package catan.model.player;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Random;

import catan.model.Game;
import catan.model.board.Buildable;
import catan.model.board.Buildable.Construction;
import catan.model.board.Harbor.HarborType;
import catan.model.board.Intersection;
import catan.model.board.Path;
import catan.model.board.Tile;
import catan.model.board.Tile.TileType;
import catan.model.card.Card;
import catan.model.card.KnightCard;
import catan.model.card.ProgressCard;
import catan.model.card.VictoryCard;
import catan.model.other.Pair;

public abstract class Player {
	public enum Resource{BRICK, LUMBER, ORE, GRAIN, WOOL};
	public enum Action{THROWDICE, BUILDROAD, BUILDSETTLEMENT, BUILDCITY, PLAYCARD, BUYCARD, SKIPPHASE};

	public static int numPlayer = 0;
	public final int id;
	public final Game actualGame;

	public final String name;
	protected final LinkedList<Card> cards = new LinkedList<>();
	protected final HashMap<Resource, Integer> tradeRate = new HashMap<>();
	protected final HashMap<Resource, Integer> resources = new HashMap<>();
	protected int victoryPoints = 0;
	protected int personalLongestRoad = 0;
	protected int personalLargestArmy = 0;
	protected int numRoadBuilt = 0;
	protected int numSettlementBuilt = 0;
	protected int numCityBuilt = 0;

	public Player(String s, Game g) {
		numPlayer++;
		id = numPlayer;
		actualGame = g;
		name = s;

		tradeRate.put(Resource.BRICK, 4);
		tradeRate.put(Resource.LUMBER, 4);
		tradeRate.put(Resource.ORE, 4);
		tradeRate.put(Resource.GRAIN, 4);
		tradeRate.put(Resource.WOOL, 4);

		resources.put(Resource.BRICK, 0);
		resources.put(Resource.LUMBER, 0);
		resources.put(Resource.ORE, 0);
		resources.put(Resource.GRAIN, 0);
		resources.put(Resource.WOOL, 0);
	}

	public LinkedList<Card> getCards() {
		return cards;
	}

	public HashMap<Resource, Integer> getResources() {
		return resources;
	}

	public HashMap<Resource, Integer> getTradeRate() {
		return tradeRate;
	}

	public int getNumOfResources() {
		int n = 0;
		for (Integer i : resources.values()) n += i;
		return n;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void gotTheLongestRoad() {
		victoryPoints += 2;
	}

	public void lostTheLongestRoad() {
		victoryPoints -= 2;
	}

	public void gotTheLargestArmy() {
		victoryPoints += 2;
	}

	public void lostTheLargestArmy() {
		victoryPoints -= 2;
	}

	public int getPersonalLongestRoad() {
		return personalLongestRoad;
	}

	public void setPersonalLongestRoad(int n) {
		personalLongestRoad = n;
	}

	public int getPersonalLargestArmy() {
		return personalLargestArmy;
	}

	public void usedAKnightCard() {
		personalLargestArmy++;
	}

	public int getNumRoadBuilt() {
		return numRoadBuilt;
	}

	public void builtARoad() {
		numRoadBuilt++;
	}

	public int getNumSettlementBuilt() {
		return numSettlementBuilt;
	}

	public void builtASettlement() {
		victoryPoints++;
		numSettlementBuilt++;
	}

	public int getNumCityBuilt() {
		return numCityBuilt;
	}

	public void builtACity() {
		victoryPoints++;
		numCityBuilt++;
	}



	public boolean hasWon() {
		return victoryPoints >= 10;
	}

	public boolean hasAPlayableCard() {
		for (Card c : cards)
			if (c instanceof KnightCard || c instanceof ProgressCard) return true;
		return false;
	}

	// TODO: write a method that will play the card and remove it from the list
	public boolean playACard(Card c) {
		return true;
	}

	public void changeTradeRate(HarborType type) {
		switch (type) {
			case BRICK: tradeRate.replace(Resource.BRICK, 2); break;
			case LUMBER: tradeRate.replace(Resource.LUMBER, 2); break;
			case ORE: tradeRate.replace(Resource.ORE, 2); break;
			case GRAIN: tradeRate.replace(Resource.GRAIN, 2); break;
			case WOOL: tradeRate.replace(Resource.WOOL, 2); break;
			default:
				tradeRate.replace(Resource.BRICK, 3);
				tradeRate.replace(Resource.LUMBER, 3);
				tradeRate.replace(Resource.ORE, 3);
				tradeRate.replace(Resource.GRAIN, 3);
				tradeRate.replace(Resource.WOOL, 3);
		}
	}

	public void receiveResources(Resource r, int amount) {
		resources.replace(r, resources.get(r) + amount);
	}

	public void loseResources(Resource r, int amount) {
		resources.replace(r, resources.get(r) - amount);
	}

	public Resource loseARandomResource() {
		if (getNumOfResources() == 0) return null;
		Random rd = new Random();
		int n = 0;
		while (true) {
			n = rd.nextInt(5);
			switch (n) {
				case 0: if (resources.get(Resource.BRICK) > 0) {
					loseResources(Resource.BRICK, 1);
					return Resource.BRICK;
				}
				case 1: if (resources.get(Resource.LUMBER) > 0) {
					loseResources(Resource.LUMBER, 1);
					return Resource.LUMBER;
				}
				case 2: if (resources.get(Resource.ORE) > 0) {
					loseResources(Resource.ORE, 1);
					return Resource.ORE;
				}
				case 3: if (resources.get(Resource.GRAIN) > 0) {
					loseResources(Resource.GRAIN, 1);
					return Resource.GRAIN;
				}
				default: if (resources.get(Resource.WOOL) > 0) {
					loseResources(Resource.WOOL, 1);
					return Resource.WOOL;
				}
			}
		}
	}

	public boolean hasTheResourcesTo(Action a) {
		int brick = resources.get(Resource.BRICK);
		int lumber = resources.get(Resource.LUMBER);
		int ore = resources.get(Resource.ORE);
		int grain = resources.get(Resource.GRAIN);
		int wool = resources.get(Resource.WOOL);

		switch (a) {
			case BUILDROAD: if (brick >= 1 && lumber >= 1) return true; break;
			case BUILDSETTLEMENT: if (brick >= 1 && lumber >= 1 && grain >= 1 && wool >= 1) return true; break;
			case BUILDCITY: if (ore >= 3 && grain >= 2) return true; break;
			case BUYCARD: if (ore >= 1 && grain >= 1 && wool >= 1) return true; break;
			default: return true;
		}

		return false;
	}

	// TODO: write a method that will make a trade with the bank
	public boolean trade(Resource desiredResource, Resource tradingResource) {
		return true;
	}

	// TODO: write a method that will build a construction on the given buildable
	public boolean build(Buildable b, Construction c) {
		return true;
	}

	public boolean buyACard() {
		int numOfAvailableCards = actualGame.availableCards.size();
		if (numOfAvailableCards == 0) return hasWon();

		Random rd = new Random();
		int n = rd.nextInt(numOfAvailableCards);

		Card c = actualGame.availableCards.remove(n);
		cards.add(c);
		if (c instanceof VictoryCard) victoryPoints++;

		return hasWon();
	}

	// TODO: write a method that will execute the wanted action after asking the player
	public boolean producingPhase() {
		return true;
	}

	// TODO: write a method that will execute the wanted action after asking the player
	public boolean combinedTradeBuildPhase() {
		return true;
	}

	public abstract String askCardToPlay();
	public abstract String askResourceToTrade();
	public abstract String askProducingPhase();
	public abstract String askCombinedTradeBuildPhase();

	public static Resource stringToResource(String s) {
		s = s.toUpperCase();
		switch (s) {
			case "BRICK": return Resource.BRICK;
			case "LUMBER": return Resource.LUMBER;
			case "ORE": return Resource.ORE;
			case "GRAIN": return Resource.GRAIN;
			case "WOOL": return Resource.WOOL;
			default: return null;
		}
	}

	public static Action charToAction(char c) {
		c = Character.toUpperCase(c);
		switch (c) {
			case 'T': return Action.THROWDICE;
			case 'R': return Action.BUILDROAD;
			case 'S': return Action.BUILDSETTLEMENT;
			case 'C': return Action.BUILDCITY;
			case 'P': return Action.PLAYCARD;
			case 'B': return Action.BUYCARD;
			case 'K': return Action.SKIPPHASE;
			default: return null;
		}
	}
}

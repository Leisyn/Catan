package catan.model.player;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Random;

import catan.model.Game;
import catan.model.board.Buildable;
import catan.model.other.Construction;
import catan.model.other.HarborType;
import catan.model.card.Card;
import catan.model.card.KnightCard;
import catan.model.card.ProgressCard;
import catan.model.card.VictoryCard;
import catan.model.other.Action;
import catan.model.other.Resource;

public abstract class Player {
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

	/**
	 * Get the player's list of cards.
	 * @return the player's list of cards
	 */
	public LinkedList<Card> getCards() {
		return cards;
	}

	/**
	 * Get the player's resources.
	 * @return the player's resources
	 */
	public HashMap<Resource, Integer> getResources() {
		return resources;
	}

	/**
	 * Get the player's trade rate.
	 * @return the player's trade rate
	 */
	public HashMap<Resource, Integer> getTradeRate() {
		return tradeRate;
	}

	/**
	 * Get the player's number of resources.
	 * @return the player's number of resources
	 */
	public int getNumOfResources() {
		int n = 0;
		for (Integer i : resources.values()) n += i;
		return n;
	}

	/**
	 * Get the player's number of victory points
	 * @return the player's number of victory points
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * Assign 2 victory points to the player. This method should only be used if the player
	 * got a special card (Longest Road or Largest Army).
	 */
	public void gotASpecialCard() {
		victoryPoints += 2;
	}

	/**
	 * Remove 2 victory points from the player. This method should only be used if the player
	 * lost a special card (Longest Road or Largest Army).
	 */
	public void lostASpecialCard() {
		victoryPoints -= 2;
	}

	/**
	 * Get the player's longest road length.
	 * @return the player's longest road length.
	 */
	public int getPersonalLongestRoad() {
		return personalLongestRoad;
	}

	/**
	 * Set the player's longest road to the given integer.
	 * @param n the new length of the longest road
	 */
	public void setPersonalLongestRoad(int n) {
		personalLongestRoad = n;
	}

	/**
	 * Get the player's largest army.
	 * @return the player's largest army
	 */
	public int getPersonalLargestArmy() {
		return personalLargestArmy;
	}

	/**
	 * Raise by one the player's largest army. This method should only be used if the player
	 * used a knight card.
	 */
	public void usedAKnightCard() {
		personalLargestArmy++;
	}

	/**
	 * Get the player's number of road built.
	 * @return the player's number of road built
	 */
	public int getNumRoadBuilt() {
		return numRoadBuilt;
	}

	/**
	 * Raise by one the player's number of road built. This method should only be used if the
	 * player built a road.
	 */
	public void builtARoad() {
		numRoadBuilt++;
	}

	/**
	 * Get the player's number of settlement built.
	 * @return the player's number of settlement built.
	 */
	public int getNumSettlementBuilt() {
		return numSettlementBuilt;
	}

	/**
	 * Raise by one the player's victory points and number of settlement built. This method
	 * should only be used if the player built a settlement.
	 */
	public void builtASettlement() {
		victoryPoints++;
		numSettlementBuilt++;
	}

	/**
	 * Get the player's number of city built.
	 * @return the player's number of city built
	 */
	public int getNumCityBuilt() {
		return numCityBuilt;
	}

	/**
	 * Decrease by one the number of settlement built and raise by one the player's victory
	 * points and number of city built. This method should only be used if the player built a city.
	 */
	public void builtACity() {
		victoryPoints++;
		numSettlementBuilt--;
		numCityBuilt++;
	}



	/**
	 * Verify if the player has won the game.
	 * @return whether the player has won
	 */
	public boolean hasWon() {
		return victoryPoints >= 10;
	}

	/**
	 * Verify if the player has a playable card. Playable cards can be knight cards or progress cards.
	 * @return whether the player has a playable card
	 */
	public boolean hasAPlayableCard() {
		for (Card c : cards)
			if (c instanceof KnightCard || c instanceof ProgressCard) return true;
		return false;
	}

	/**
	 * Play the given card.
	 * @param c the card to play
	 * @return whether the player has won
	 */
	public boolean playACard(Card c) {
		c.play(actualGame, this);
		cards.remove(c);
		return hasWon();
	}

	/**
	 * Change the player's trade rate depending on the given harbor type.
	 * @param type the harbor the player has built on
	 */
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

	/**
	 * Give a certain amount of a resource to the player.
	 * @param r			the resource to give
	 * @param amount	the amount to give
	 */
	public void receiveResources(Resource r, int amount) {
		resources.replace(r, resources.get(r) + amount);
	}

	/**
	 * Take a certain amount of a resource from the player.
	 * @param r			the resource to take
	 * @param amount	the amount to take
	 */
	public void loseResources(Resource r, int amount) {
		resources.replace(r, resources.get(r) - amount);
	}

	/**
	 * Take a random resource from the player.
	 * @return the taken resource or null
	 */
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

	/**
	 * Verify if the player has the resources necessary to execute the given action.
	 * @param a	the action to execute
	 * @return whether the action can be executed
	 */
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

	/**
	 * Buy a card from the list of available cards of the game.
	 * @return whether the player has won
	 */
	public boolean buyACard() {
		int numOfAvailableCards = actualGame.availableCards.size();
		if (numOfAvailableCards == 0) return hasWon();

		Random rd = new Random();
		int n = rd.nextInt(numOfAvailableCards);

		Card c = actualGame.availableCards.remove(n);
		cards.add(c);
		if (c instanceof VictoryCard) victoryPoints++;
		
		loseResources(Resource.ORE, 1);
		loseResources(Resource.GRAIN, 1);
		loseResources(Resource.WOOL, 1);

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
}

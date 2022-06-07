package catan.model.board;

import java.util.LinkedList;

import catan.model.player.Player;

public class Tile {
	public enum TileType{SEA, HARBOR, HILLS, FOREST, MOUNTAINS, FIELDS, PASTURE, DESERT};
	
	public final TileType type;
	public final int numToken;
	public boolean robberIsHere;
	
	public Road roadN, roadS, roadE, roadW;
	public Intersection interNW, interNE, interSW, interSE;
	
	/**
	 * Creates a general terrain tile with a number token on it.
	 * @param type		the type of the tile
	 * @param numToken	the number token of the tile
	 */
	public Tile(TileType type, int numToken) {
		if (numToken < 2 || numToken > 12) throw new IllegalArgumentException("Can't create the tile: unreachable token number");
		if (numToken == 7) throw new IllegalArgumentException("Can't create the tile: a tile can't have 7 as a token number");
		
		this.type = type;
		this.numToken = numToken;
		this.robberIsHere = false;
	}
	
	/**
	 * Creates a tile with no number token on it.
	 * @param type			the type of the tile
	 * @param robberIsHere	whether the robber is here or not
	 */
	public Tile(TileType type, boolean robberIsHere) {
		if (type != TileType.SEA && type != TileType.HARBOR && type != TileType.DESERT)
			throw new IllegalArgumentException("Can't create the tile: given type not corresponding");
		
		this.type = type;
		this.numToken = 0;
		this.robberIsHere = robberIsHere;
	}
	
	/**
	 * Initialize all roads in contact with the tile.
	 * @param n	the north road
	 * @param s	the south road
	 * @param e	the east road
	 * @param o	the west road
	 */
	public void iniRoad(Road n, Road s, Road e, Road o) {
		roadN = n;
		roadS = s;
		roadE = e;
		roadW = o;
	}
	
	/**
	 * Initialize all intersections in contact with the tile.
	 * @param no	the north west road
	 * @param ne	the north east road
	 * @param sw	the south west road
	 * @param se	the south east road
	 */
	public void iniIntersection(Intersection nw, Intersection ne, Intersection sw, Intersection se) {
		interNW = nw;
		interNE = ne;
		interSW = sw;
		interSE = se;
	}
	
	/**
	 * Get the road on the given direction.
	 * @param s	the initial of the wanted direction
	 * @return the road on the given direction
	 */
	public Road getRoad(String s) {
		switch (s) {
			case "N": return roadN;
			case "S": return roadS;
			case "E": return roadE;
			case "O": return roadW;
			default: return null;
		}
	}
	
	/**
	 * Modify the road on the given direction.
	 * @param s	the initial of the wanted direction
	 * @param p	the player who built this road
	 */
	public void setRoad(String s, Player p) {
		switch (s) {
			case "N": roadN.player = p; break;
			case "S": roadS.player = p; break;
			case "E": roadE.player = p; break;
			case "O": roadW.player = p; break;
		}
	}
	
	/**
	 * Get the intersection on the given direction.
	 * @param s	the initial of the wanted direction
	 * @return the intersection on the given direction
	 */
	public Intersection getIntersection(String s) {
		switch (s) {
			case "NO": return interNW;
			case "NE": return interNE;
			case "SO": return interSW;
			case "SE": return interSE;
			default: return null;
		}
	}
	
	/**
	 * Modify the intersection on the given direction
	 * @param s		the initial of the wanted direction
	 * @param p		the player who built this intersection
	 * @param type	the type of building built
	 */
	public void setIntersection(String s, Player p, int type) {
		switch (s) {
			case "NO": interNW.player = p; interNW.building = type; break;
			case "NE": interNE.player = p; interNE.building = type; break;
			case "SO": interSW.player = p; interSW.building = type; break;
			case "SE": interSE.player = p; interSE.building = type; break;
		}
	}
	
	/**
	 * Get all the intersections that have a building on built on them.
	 * @return all the intersections that have been built
	 */
	public LinkedList<Intersection> getAllBuiltIntersections() {
		LinkedList<Intersection> res = new LinkedList<>();
		if (interNW.isBuilt()) res.add(interNW);
		if (interNE.isBuilt()) res.add(interNE);
		if (interSW.isBuilt()) res.add(interSW);
		if (interSE.isBuilt()) res.add(interSE);
		return res;
	}

	/**
	 * Print the name of the tile, with its number token if it has one.
	 */
	public void printName() {
		switch (type) {
			case HARBOR: System.out.print(" HARBOR "); return;
			case HILLS: System.out.print(" HI "); break;
			case FOREST: System.out.print(" FO "); break;
			case MOUNTAINS: System.out.print(" MO "); break;
			case FIELDS: System.out.print(" FI "); break;
			case PASTURE: System.out.print(" PA "); break;
			case DESERT: System.out.print("  DES  "); return;
			default: System.out.print("       "); return;
		}
		if (numToken < 10) System.out.print(" " + numToken + " ");
		else System.out.print(numToken + " ");
	}
	
	/**
	 * Print the detail of the tile. Unless the robber is present, only blank spaces will be printed.
	 */
	public void printDetail() {
		if (robberIsHere) System.out.print("   V   ");
		else System.out.print("       ");
	}
	
	/**
	 * Verify if the tile is a sea tile or a harbor.
	 * @return	whether the tile is a marine tile
	 */
	public boolean isMarine() {
		if (type == TileType.SEA || type == TileType.HARBOR)
			return true;
		return false;
	}
}

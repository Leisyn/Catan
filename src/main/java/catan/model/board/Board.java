package catan.model.board;

import catan.model.Game;
import catan.model.board.Tile.TileType;
import catan.model.other.Pair;
import catan.model.player.Player;

public class Board {
	private Tile[][] tiles;
	private Path[][] roads;
	private Intersection[][] intersections;

	// Cree le plateau de jeu, de taille 7x7
	public Board() {
		tiles = new Tile[7][7];
		roads = new Path[13][7];
		intersections = new Intersection[6][6];
	}

	public Tile[][] getTiles() {
		return tiles;
	}
	
	public Path[][] getRoads() {
		return roads;
	}
	
	public Intersection[][] getIntersections() {
		return intersections;
	}
	
	public Pair getXY(Path r) {
		for (int i = 0; i < roads.length; i++) {
			for (int j = 0; j < roads[i].length; j++) {
				if (roads[i][j] == r)
					return new Pair(i, j);
			}
		}
		
		return null;
	}
	
	// Renvoie les 4 routes en contact avec l'intersection donnee dans l'ordre : NO, NE, SO, SE
	public Tile[] getAllTilesInContactWith(Intersection in) {
		if (in == null)
			throw new IllegalArgumentException("Intersection equals to null");

		// on cherche l'intersection donnee
		for (int i = 0; i < intersections.length; i++) {
			for (int j = 0; j < intersections[i].length; j++) {
				if (intersections[i][j] == in) {
					Tile[] res = new Tile[4];
					
					// on ajoute les 4 tuiles en contact avec l'intersection
					res[0] = tiles[i][j];
					res[1] = tiles[i][j + 1];
					res[2] = tiles[i + 1][j];
					res[3] = tiles[i + 1][j + 1];
					
					return res;
				}
			}
		}
		
		// l'intersection n'a pas ete trouvee
		return null;
	}
	
	public Harbor getHarborInContactWith(Intersection in) {
		Tile[] t = getAllTilesInContactWith(in);
		for (int i = 0; i < t.length; i++) {
			if (t[i] instanceof Harbor)
				return (Harbor)t[i];
		}
		
		return null;
	}

	// Renvoie les 4 routes menant a l'intersection donnee dans l'ordre : N, S, O, E
	public Path[] getAllPathsInContactWith(Intersection in) {
		if (in == null)
			throw new IllegalArgumentException("Intersection equals to null");

		// on cherche l'intersection donnee
		for (int i = 0; i < intersections.length; i++) {
			for (int j = 0; j < intersections[i].length; j++) {
				if (intersections[i][j] == in) {
					Path[] res = new Path[4];
					int m = i * 2 + 1;
					int n = j;

					// on ajoute les 4 routes menant a cette intersections
					res[0] = roads[m][n];
					res[1] = roads[m - 1][n];
					res[2] = roads[m][n + 1];
					res[3] = roads[m + 1][n];

					return res;
				}
			}
		}

		return null;
	}

	// Renvoie les 6 routes en contact a la route donnee dans l'ordre : O, NO, SO,
	// E, NE, SE ou N, NO, NE, S, SO, SE
	public Path[] getAllPathsInContactWith(Path r) {
		if (r == null)
			throw new IllegalArgumentException("Road equals to null");

		// on cherche la route donnee
		for (int i = 1; i < roads.length - 1; i++) {
			for (int j = 0; j < roads[i].length - 1; j++) {
				if (roads[i][j] == r) {
					Path[] res = new Path[6];

					// s'il s'agit d'une route horizontale
					if (i % 2 == 1) {
						res[0] = roads[i][j - 1];  // on cherche la route a l'ouest
						res[1] = roads[i - 1][j - 1];  // on cherche la route au nord-ouest
						res[2] = roads[i + 1][j - 1];  // on cherche la route au sud-ouest
						res[3] = roads[i][j + 1];  // on cherche la route a l'est
						res[4] = roads[i - 1][j];  // on cherche la route au nord-est
						res[5] = roads[i + 1][j];  // on cherche la route au sud-est
						return res;  // on renvoie les 6 routes trouvees
					}

					// s'il s'agit d'une route verticale
					else {
						res[0] = roads[i - 2][j];  // on cherche la route au nord
						res[1] = roads[i - 1][j];  // on cherche la route au nord-ouest
						res[2] = roads[i - 1][j + 1];  // on cherche la route au nord-est
						res[3] = roads[i + 2][j];  // on cherche la route au sud
						res[4] = roads[i + 1][j];  // on cherche la route au sud-ouest
						res[5] = roads[i + 1][j + 1];  // on cherche la route au sud-est
						return res;  // on renvoie les 6 routes trouvees
					}
				}
			}
		}
		// on n'a pas trouve la route donnee
		return null;
	}

	// Renvoie les deux intersections se trouvant aux bouts de la route
	public Intersection[] getAllIntersectionsInContactWith(Path r) {
		if (r == null)
			throw new IllegalArgumentException("Road equals to null");

		// on cherche la route donnee
		for (int i = 1; i < roads.length - 1; i++) {
			for (int j = 0; j < roads[i].length - 1; j++) {
				if (roads[i][j] == r) {

					// on regarde s'il s'agit d'une route hozizontale
					if (i % 2 == 1) {
						// on renvoie les intersections nord et sud
						Intersection inO = intersections[i / 2][j - 1];
						Intersection inE = intersections[i / 2][j];
						Intersection[] res = {inO, inE};
						return res;
					}

					// sinon, il s'agit d'une route verticale : on renvoie les intersections ouest et est
					Intersection inN = intersections[i / 2 - 1][j];
					Intersection inS = intersections[i / 2][j];
					Intersection[] res = {inN, inS};
					return res;
				}
			}
		}
		// on n'a pas trouve la route
		return null;
	}

	// Renvoie la position ou se trouve la tuile demandee
	public Pair position(String typeTuile, int jeton) {
		if (jeton < 2 || jeton > 12)
			return null;

		TileType type;
		switch (typeTuile.toUpperCase()) {
			case "PR": type = TileType.PASTURE; break;
			case "FO": type = TileType.FOREST; break;
			case "CO": type = TileType.HILLS; break;
			case "CH": type = TileType.FIELDS; break;
			case "MO": type = TileType.MOUNTAINS; break;
			case "DES": type = TileType.DESERT;
			default: type = TileType.SEA;
		}

		if (type == TileType.SEA) return null;

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j].type == type && tiles[i][j].numToken == jeton)
					return new Pair(i, j);
			}
		}
		return null;
	}

	// Retire le voleur du plateau
	public void removeRobber() {
		for (int i = 1; i < tiles.length - 1; i++) {
			for (int j = 1; j < tiles[i].length - 1; j++) {
				if (tiles[i][j].robberIsHere) {
					tiles[i][j].robberIsHere = false;
					return;
				}
			}
		}
	}

	// Place le voleur a la position donnee
	public void placeRobber(Pair p) {
		tiles[p.x][p.x].robberIsHere = true;
	}
	
	public void placeRobber(Tile t) {
		for (int i = 1; i < tiles.length - 1; i++) {
			for (int j = 1; j < tiles[i].length - 1; j++) {
				if (tiles[i][j] == t) {
					tiles[i][j].robberIsHere = true;
					return;
				}
			}
		}
	}

	public boolean respectDistanceRule(Intersection in) {
		if (in == null)
			throw new IllegalArgumentException("Intersection null");
		
		// on cherche l'intersection en question
		for (int i = 0; i < intersections.length; i++) {
			for (int j = 0; j < intersections[i].length; j++) {

				// lorsqu'on l'a trouve
				if (intersections[i][j] == in) {

					// on regarde si l'intersection nord est construite
					if (i > 0 && intersections[i - 1][j] != null && intersections[i - 1][j].player != null)
						return false;

					// on regarde si l'intersection sud est construite
					if (i < intersections.length - 1 && intersections[i + 1][j] != null && intersections[i + 1][j].player != null)
						return false;

					// on regarde si l'intersection ouest est construite
					if (j > 0 && intersections[i][j - 1] != null && intersections[i][j - 1].player != null)
						return false;

					// on regarde si l'intersection est est construite
					if (j < intersections[i].length - 1 && intersections[i][j + 1] != null && intersections[i][j + 1].player != null)
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
	
	public boolean isInContactWithAHarbor(Intersection in) {
		Tile[] tilesInContactWith = getAllTilesInContactWith(in);
		
		for (int i = 0; i < tilesInContactWith.length; i++) {
			if (tilesInContactWith[i] instanceof Harbor)
				return true;
		}
		
		return false;
	}
	
	public Buildable getConstruction(Pair positionTile, String direction) {
		if (direction.equals("N") || direction.equals("S") || direction.equals("O") || direction.equals("E"))
			return tiles[positionTile.x][positionTile.y].getPath(direction);
		
		return tiles[positionTile.x][positionTile.y].getIntersection(direction);
	}

	public void build(Game game, Buildable construction, Player p, int type) {
		construction.build(game, p, type);
	}

	// Calcule la route la plus longue du joueur donn�
	public int calculateLongestRoad(Player p) {
		int max = 0;
		for (int i = 1; i < roads.length - 1; i++) {
			for (int j = 0; j < roads[i].length - 1; j++) {
				if (roads[i][j] != null && roads[i][j].player == p)
					max = Math.max(max, calculateRoadLength(roads[i][j]));
			}
		}
		return max;
	}

	// Calcule la longueur de la route de la route donn�e
	public int calculateRoadLength(Path r) {
		boolean[][] visitedRoad = new boolean[roads.length][roads[0].length];
		return calculateRoadLengthAux(visitedRoad, r.player, r, 0);
	}

	private int calculateRoadLengthAux(boolean[][] visitedRoad, Player p, Path r, int n) {
		// on regarde s'il s'agit d'une route existante
		if (r == null)
			return n;

		Pair pair = getXY(r);
		if (pair == null || visitedRoad[pair.x][pair.y])
			return n;
		
		visitedRoad[pair.x][pair.y] = true;
		
		// on regarde si la route appartient au joueur
		if (r.player != p)
			return n;

		// on r�cup�re les intersections au bout de la route
		Intersection[] in = getAllIntersectionsInContactWith(r);

		// on regarde si des batiments adverses bloquent les deux intersections
		if ((in[0].building != 0 && in[0].player != p) || (in[1].building != 0 && in[1].player != p))
			return n;

		// on r�cup�re les 6 routes li�es � la route actuelle
		Path[] routes = getAllPathsInContactWith(r);
		int[] max = new int[2];
		int m;

		// si aucun batiment adverse ne bloque la 1er intersection
		if (in[0].building == 0 || in[0].player == p) {

			// on r�cup�re la route la plus longue a partir de la route � l'ouest / au nord
			// et on la compare avec les deux routes les plus longues pr�c�dentes
			m = calculateRoadLengthAux(visitedRoad, p, routes[0], n);
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
			m = calculateRoadLengthAux(visitedRoad, p, routes[1], n);
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
			m = calculateRoadLengthAux(visitedRoad, p, routes[2], n);
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
		if (in[1].building == 0 || in[1].player == p) {

			// on r�cup�re la route la plus longue a partir de la route � l'est / au sud et
			// on la compare avec les deux routes les plus longues pr�c�dentes
			m = calculateRoadLengthAux(visitedRoad, p, routes[3], n);
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
			m = calculateRoadLengthAux(visitedRoad, p, routes[4], n);
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
			m = calculateRoadLengthAux(visitedRoad, p, routes[5], n);
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

	public void printBoard() {
		System.out.println("---------------------------------------------------------");
		for (int i = 0; i < tiles.length; i++) {
			printTileName(i);
			printTileDetails(i);
			if (i != tiles.length - 1) // il n'y a pas de routes et intersections sous la derniere ligne de tuile
				printRoadAndIntersection(i);
		}
		System.out.println("---------------------------------------------------------");
	}

	private void printTileName(int i) {
		System.out.print("|");
		for (int j = 0; j < tiles[i].length; j++) {
			tiles[i][j].printName();

			if (j != tiles[i].length - 1) {
				if (tiles[i][j].pathE == null)
					System.out.print(" ");
				else
					tiles[i][j].pathE.printV();
			}
		}
		System.out.println("|");
	}

	private void printTileDetails(int i) {
		System.out.print("|");
		for (int j = 0; j < tiles[i].length; j++) {
			tiles[i][j].printDetail();

			if (j != tiles[i].length - 1) {
				if (tiles[i][j].pathE == null)
					System.out.print(" ");
				else
					tiles[i][j].pathE.printV();
			}
		}
		System.out.println("|");
	}

	private void printRoadAndIntersection(int i) {
		System.out.print("|");
		for (int j = 0; j < tiles[i].length; j++) {
			if (tiles[i][j].pathS == null)
				System.out.print("       ");
			else
				tiles[i][j].pathS.printH();

			if (j != tiles[i].length - 1) {
				if (tiles[i][j].interSE == null)
					System.out.print(" ");
				else
					System.out.print(tiles[i][j].interSE);
			}
		}
		System.out.println("|");
	}

	// Initialise un plateau
	public static Board iniBoard() {
		Board res = new Board();
		iniRoads(res.roads);
		iniIntersections(res.intersections);
		iniTiles(res.tiles, res.roads, res.intersections);
		return res;
	}

	// Initialise les tuiles
		private static void iniTiles(Tile[][] t, Path[][] r, Intersection[][] in) {
			
			// on initialises les tuiles elles-memes
			for (int i = 0; i < t.length; i++) {
				for (int j = 0; j < t[i].length; j++) {
					if (i == 0) {
						switch(j) {
							case 2: t[i][j] = new Harbor(0); break;
							case 4: t[i][j] = new Harbor(1); break;
							default: t[i][j] = new Tile(TileType.SEA, false); break;
						}
					} else if (i == 1) {
						switch(j) {
							case 2: t[i][j] = new Tile(TileType.FOREST, 6); break;
							case 3: t[i][j] = new Tile(TileType.PASTURE, 3); break;
							case 4: t[i][j] = new Tile(TileType.FIELDS, 8); break;
							default : t[i][j] = new Tile(TileType.SEA, false); break;
						}
					} else if (i == 2) {
						switch(j) {
							case 0: t[i][j] = new Harbor(5); break;
							case 1: t[i][j] = new Tile(TileType.PASTURE, 2); break;
							case 2: t[i][j] = new Tile(TileType.HILLS, 4); break;
							case 3: t[i][j] = new Tile(TileType.MOUNTAINS, 5); break;
							case 4: t[i][j] = new Tile(TileType.FOREST, 9); break;
							case 5: t[i][j] = new Tile(TileType.MOUNTAINS, 11); break;
							case 6: t[i][j] = new Harbor(0); break;
						}
					} else if (i == 3) {
						switch(j) {
							case 1: t[i][j] = new Tile(TileType.FOREST, 5); break;
							case 2: t[i][j] = new Tile(TileType.MOUNTAINS, 3); break;
							case 3: t[i][j] = new Tile(TileType.DESERT, true); break;
							case 4: t[i][j] = new Tile(TileType.PASTURE, 12); break;
							case 5: t[i][j] = new Tile(TileType.HILLS, 6); break;
							default : t[i][j] = new Tile(TileType.SEA, false); break;
						}
					} else if (i == 4) {
						switch(j) {
							case 0: t[i][j] = new Harbor(4); break;
							case 1: t[i][j] = new Tile(TileType.FIELDS, 10); break;
							case 2: t[i][j] = new Tile(TileType.PASTURE, 4); break;
							case 3: t[i][j] = new Tile(TileType.HILLS, 8); break;
							case 4: t[i][j] = new Tile(TileType.MOUNTAINS, 10); break;
							case 5: t[i][j] = new Tile(TileType.FIELDS, 9); break;
							case 6: t[i][j] = new Harbor(2); break;
						}
					} else if (i == 5) {
						switch(j) {
							case 2: t[i][j] = new Tile(TileType.HILLS, 2); break;
							case 3: t[i][j] = new Tile(TileType.FIELDS, 11); break;
							case 4: t[i][j] = new Tile(TileType.FOREST, 12); break;
							default : t[i][j] = new Tile(TileType.SEA, false); break;
						}
					} else {
						switch(j) {
							case 2: t[i][j] = new Harbor(3); break;
							case 4: t[i][j] = new Harbor(0); break;
							default: t[i][j] = new Tile(TileType.SEA, false); break;
						}
					}
				}
			}
			
			// on pointe les routes et intersections des tuiles au bon endroit
			for (int i = 0; i < t.length; i++) {
				for (int j = 0; j < t[i].length; j++) {
					// on commence par les routes
					Path nord, sud, est, ouest;
					
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
					t[i][j].iniPath(nord, sud, est, ouest);
					
					
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
	private static void iniRoads(Path[][] r) {
		for (int i = 1; i < r.length - 1; i++) {

			// la 2e et avant-avant-derniere ligne n'ont que 3 routes construisibles
			if (i == 1 || i == 11) {
				for (int j = 2; j < r[i].length - 2; j++)
					r[i][j] = new Path();
			}

			// la 3e et avant-avant-avant derniere ligne n'ont que 4 routes construisibles
			else if (i == 2 || i == 10) {
				for (int j = 1; j < r[i].length - 2; j++)
					r[i][j] = new Path();
			}

			// les lignes impaires ont 5 routes construisibles
			else if (i % 2 != 0) {
				for (int j = 1; j < r[i].length - 1; j++)
					r[i][j] = new Path();
			}

			// les lignes paires ont 6 routes construisibles
			else {
				for (int j = 0; j < r[i].length - 1; j++)
					r[i][j] = new Path();
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

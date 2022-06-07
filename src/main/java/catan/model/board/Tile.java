package catan.model.board;

import java.util.LinkedList;

import catan.model.player.Player;

public class Tile {
	public final int type;  // 0 : mer / 1 : port / 2 : pre / 3 : foret /  4 : colline / 5 : champs / 6 : montagne / 7 : desert
	public final int token;  // le jeton de la tuile (0 si la tuile ne possede pas de jeton, entre 2 et 12 sinon (7 exclus))
	public boolean robberIsHere;  // indique si le voleur est present sur la tuile
	
	public Road roadN, roadS, roadE, roadW;  // les routes nord, surd, est et ouest
	public Intersection interNW, interNE, interSW, interSE;  // les intersections nord-ouest, nord-est, sud-ouest et sud-est
	
	// Constructeur general
	public Tile(int type, int token, boolean robberIsHere) {
		
		// Gestion des exceptions
		if (type < 0 || type > 7)
			throw new IllegalArgumentException("Can't create the tile: unknown given type");
		if (token < 2 || token > 12)
			throw new IllegalArgumentException("Can't create the tile: unreachable token number");
		if (token == 7)
			throw new IllegalArgumentException("Can't create the tile: a tile can't have 7 as a token number");
		
		this.type = type;
		this.token = token;
		this.robberIsHere = robberIsHere;
	}
	
	// Constructeur pour les tuiles speciales (mer, port et desert)
	public Tile(int type, boolean robberIsHere) {
		if (type != 0 && type != 1 && type != 7)
			throw new IllegalArgumentException("Can't create the tile: given type not corresponding");
		
		this.type = type;
		token = 0;
		this.robberIsHere = robberIsHere;
	}
	
	public void iniRoad(Road n, Road s, Road e, Road o) {
		roadN = n;
		roadS = s;
		roadE = e;
		roadW = o;
	}
	
	
	public void iniIntersection(Intersection no, Intersection ne, Intersection so, Intersection se) {
		interNW = no;
		interNE = ne;
		interSW = so;
		interSE = se;
	}
	
	public Road getRoad(String s) {
		switch (s) {
			case "N": return roadN;
			case "S": return roadS;
			case "E": return roadE;
			case "O": return roadW;
			default: throw new IllegalArgumentException("Can't get the road: unknown direction");
		}
	}
	
	public void setRoad(String s, Player p) {
		switch (s) {
			case "N": roadN.player = p; break;
			case "S": roadS.player = p; break;
			case "E": roadE.player = p; break;
			case "O": roadW.player = p; break;
			default: throw new IllegalArgumentException("Can't modify the road: unknown direction");
		}
	}
	
	public Intersection getIntersection(String s) {
		switch (s) {
			case "NO": return interNW;
			case "NE": return interNE;
			case "SO": return interSW;
			case "SE": return interSE;
			default: throw new IllegalArgumentException("Can't get the intersection: unknown direction");
		}
	}
	
	public void setIntersection(String s, Player p, int type) {
		switch (s) {
			case "NO": interNW.player = p; interNW.building = type; break;
			case "NE": interNE.player = p; interNE.building = type; break;
			case "SO": interSW.player = p; interSW.building = type; break;
			case "SE": interSE.player = p; interSE.building = type; break;
			default: throw new IllegalArgumentException("Can't modify the intersection: unknown direction");
		}
	}
	
	public LinkedList<Intersection> getAllBuiltIntersections() {
		LinkedList<Intersection> res = new LinkedList<>();
		
		// on regarde si un batiment est construit dans l'intersection NO
		if (interNW.isBuilt())
			res.add(interNW);
		
		// on regarde si un batiment est construit dans l'intersection NE
		if (interNE.isBuilt())
			res.add(interNE);
		
		// on regarde si un batiment est construit dans l'intersection SO
		if (interSW.isBuilt())
			res.add(interSW);
		
		// on regarde si un batiment est construit dans l'intersection SE
		if (interSE.isBuilt())
			res.add(interSE);
		
		return res;
	}

	public void printName() {
		switch (type) {
			case 1: System.out.print(" PORT  "); return;
			case 2: System.out.print(" PR "); break;
			case 3: System.out.print(" FO "); break;
			case 4: System.out.print(" CO "); break;
			case 5: System.out.print(" CH "); break;
			case 6: System.out.print(" MO "); break;
			case 7: System.out.print("  DES  "); return;
			default: System.out.print("       "); return;
		}
		if (token < 10) System.out.print(" " + token + " ");
		else System.out.print(token + " ");
	}
	
	public void printDetail() {
		if (robberIsHere) System.out.print("   V   ");
		else System.out.print("       ");
	}
	
	public boolean isMarine() {
		if (type == 0 || type == 1)
			return true;
		return false;
	}
}

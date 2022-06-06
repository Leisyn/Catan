import java.util.LinkedList;

public class Tuile {
	public final int type;  // 0 : mer / 1 : port / 2 : pre / 3 : foret /  4 : colline / 5 : champs / 6 : montagne / 7 : desert
	public final int jeton;  // le jeton de la tuile (0 si la tuile ne possede pas de jeton, entre 2 et 12 sinon (7 exclus))
	public boolean voleurEstIci;  // indique si le voleur est present sur la tuile
	
	public Route rouN, rouS, rouE, rouO;  // les routes nord, surd, est et ouest
	public Intersection interNO, interNE, interSO, interSE;  // les intersections nord-ouest, nord-est, sud-ouest et sud-est
	
	// Constructeur general
	public Tuile(int type, int jeton, boolean voleurEstIci) {
		
		// Gestion des exceptions
		if (type < 0 || type > 7)
			throw new IllegalArgumentException("Impossible de créer la tuile : type donné inconnu.");
		if (jeton < 2 || jeton > 12)
			throw new IllegalArgumentException("Impossible de créer la tuile : jeton non atteignable.");
		if (jeton == 7)
			throw new IllegalArgumentException("Impossible de créer la tuile : une tuile ne peut pas avoir un jeton égal à 7.");
		
		this.type = type;
		this.jeton = jeton;
		this.voleurEstIci = voleurEstIci;
	}
	
	// Constructeur pour les tuiles speciales (mer, port et desert)
	public Tuile(int type, boolean voleurEstIci) {
		if (type != 0 && type != 1 && type != 7)
			throw new IllegalArgumentException("Impossible de créer la tuile : type donné non correspondant.");
		
		this.type = type;
		jeton = 0;
		this.voleurEstIci = voleurEstIci;
	}
	
	public void iniRoute(Route n, Route s, Route e, Route o) {
		rouN = n;
		rouS = s;
		rouE = e;
		rouO = o;
	}
	
	
	public void iniIntersection(Intersection no, Intersection ne, Intersection so, Intersection se) {
		interNO = no;
		interNE = ne;
		interSO = so;
		interSE = se;
	}
	
	public Route getRoute(String s) {
		switch (s) {
			case "N": return rouN;
			case "S": return rouS;
			case "E": return rouE;
			case "O": return rouO;
			default: throw new IllegalArgumentException("Impossible d'obtenir la route : direction inconnue.");
		}
	}
	
	public void setRoute(String s, Joueur j) {
		switch (s) {
			case "N": rouN.joueur = j; break;
			case "S": rouS.joueur = j; break;
			case "E": rouE.joueur = j; break;
			case "O": rouO.joueur = j; break;
			default: throw new IllegalArgumentException("Impossible de modifier la route : direction inconnue.");
		}
	}
	
	public Intersection getIntersection(String s) {
		switch (s) {
			case "NO": return interNO;
			case "NE": return interNE;
			case "SO": return interSO;
			case "SE": return interSE;
			default: throw new IllegalArgumentException("Impossible d'obtenir l'intersection : direction inconnue.");
		}
	}
	
	public void setIntersection(String s, Joueur j, int type) {
		switch (s) {
			case "NO": interNO.joueur = j; interNO.batiment = type; break;
			case "NE": interNE.joueur = j; interNE.batiment = type; break;
			case "SO": interSO.joueur = j; interSO.batiment = type; break;
			case "SE": interSE.joueur = j; interSE.batiment = type; break;
			default: throw new IllegalArgumentException("Impossible de modifier l'intersection : direction inconnue.");
		}
	}
	
	public LinkedList<Intersection> getToutesInterConstruites() {
		LinkedList<Intersection> res = new LinkedList<>();
		
		// on regarde si un batiment est construit dans l'intersection NO
		if (interNO.estConstruit())
			res.add(interNO);
		
		// on regarde si un batiment est construit dans l'intersection NE
		if (interNE.estConstruit())
			res.add(interNE);
		
		// on regarde si un batiment est construit dans l'intersection SO
		if (interSO.estConstruit())
			res.add(interSO);
		
		// on regarde si un batiment est construit dans l'intersection SE
		if (interSE.estConstruit())
			res.add(interSE);
		
		return res;
	}

	public void afficheNom() {
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
		if (jeton < 10) System.out.print(" " + jeton + " ");
		else System.out.print(jeton + " ");
	}
	
	public void afficheDetail() {
		if (voleurEstIci) System.out.print("   V   ");
		else System.out.print("       ");
	}
	
	public boolean estMarine() {
		if (type == 0 || type == 1)
			return true;
		return false;
	}
}

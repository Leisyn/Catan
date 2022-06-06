import java.awt.*;
import javax.swing.JPanel;
import java.util.LinkedList;
import java.awt.geom.Ellipse2D;
import java.awt.event.*;
import java.util.Random;

public class PlateauPanel extends JPanel implements MouseListener {
	Vue vue;
	public int typeAction = 0;  // 0 : rien, 1 : construire route, 2 : construire colonie, 3 : construire ville, 4 : voleur
	public boolean phaseInitiale = false;
	
	LinkedList<Rectangle> routes = new LinkedList<>();
	LinkedList<Rectangle> tuiles = new LinkedList<>();
	LinkedList<Shape> intersections = new LinkedList<>();
	
	Color couleurRouteIntersectionDefaut = new Color(243, 214, 140);
	
	Color couleurTuileMarine = new Color(50, 143, 221);
	Color couleurTuilePre = new Color(88, 237, 105);
	Color couleurTuileForet = new Color(50, 176, 64);
	Color couleurTuileColline = new Color(159, 118, 48);
	Color couleurTuileChamps = new Color(231, 231, 120);
	Color couleurTuileMontagne = new Color(172, 172, 153);
	Color couleurTuileDesert = new Color(219, 219, 156);
	
	Color couleurJ1 = new Color(234, 234, 184);
	Color couleurJ2 = new Color(234, 73, 73);
	Color couleurJ3 = new Color(49, 158, 239);
	Color couleurJ4 = new Color(247, 200, 83);
	
	public PlateauPanel(Vue vue) {
		addMouseListener(this);
		this.vue = vue;
		
		setPreferredSize(new Dimension(685, 685));
		
		for (int i = 0; i <= 600; i += 100) {
			for (int j = 0; j <= 600; j += 100)
				tuiles.add(new Rectangle(j, i, 85, 85));
		}
		
		for (int i = 0; i <= 600; i += 100) {
			for (int j = 85; j <= 585; j += 100)
				routes.add(new Rectangle(j, i, 15, 85));
		}
		
		for (int i = 85; i <= 585; i += 100) {
			for (int j = 0; j <= 600; j += 100)
				routes.add(new Rectangle(j, i, 85, 15));
		}
		
		for (int i = 77; i <= 577; i += 100) {
			for (int j = 77; j <= 577; j += 100)
				intersections.add(new Ellipse2D.Double(j, i, 30, 30));
		}
	}
	
	
	
	public void paintComponent(Graphics g) {
		int n = 0;
		Plateau p = vue.jeu.getPlateau();
		
		n = 0;
		
		for (int i = 0; i < p.getTuiles().length; i++) {
			for (int j = 0; j < p.getTuiles()[i].length; j++) {
				// on met en place la couleur
				switch (p.getTuiles()[i][j].type) {
					case 2: g.setColor(couleurTuilePre); break;
					case 3: g.setColor(couleurTuileForet); break;
					case 4: g.setColor(couleurTuileColline); break;
					case 5: g.setColor(couleurTuileChamps); break;
					case 6: g.setColor(couleurTuileMontagne); break;
					case 7: g.setColor(couleurTuileDesert); break;
					default: g.setColor(couleurTuileMarine);
				}
				
				// on recupere et on dessine le rectangle correspondant
				Rectangle r = tuiles.get(n);
				g.fillRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
				
				n++;
			}
		}
		
		int a = 0;
		n = 0;
		
		for (int i = 0; i < p.getRoutes().length; i += 2) {
			if (i % 2 == 0) a = p.getRoutes()[i].length - 1;
			else a = p.getRoutes()[i].length;
			
			for (int j = 0; j < a; j++) {
				// on met en place la couleur
				if (p.getRoutes()[i][j] == null)
					g.setColor(couleurTuileMarine);
				else if (p.getRoutes()[i][j].joueur == null)
					g.setColor(couleurRouteIntersectionDefaut);
				else {
					switch (p.getRoutes()[i][j].joueur.id) {
						case 1: g.setColor(couleurJ1); break;
						case 2: g.setColor(couleurJ2); break;
						case 3: g.setColor(couleurJ3); break;
						default: g.setColor(couleurJ4);
					}
				}
				
				// on recupere et on dessine le rectangle correspondant
				Rectangle r = routes.get(n);
				g.fillRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
				
				n++;
			}
			if (i == p.getRoutes().length - 1) i = -1;
		}
		
		n = 0;

		for (int i = 0; i < p.getIntersections().length; i++) {
			for (int j = 0; j < p.getIntersections()[i].length; j++) {
				if (p.getIntersections()[i][j] == null)
					g.setColor(couleurTuileMarine);
				
				else if (p.getIntersections()[i][j].joueur == null)
					g.setColor(couleurRouteIntersectionDefaut);
				
				else {
					switch (p.getIntersections()[i][j].joueur.id) {
						case 1: g.setColor(couleurJ1); break;
						case 2: g.setColor(couleurJ2); break;
						case 3: g.setColor(couleurJ3); break;
						default: g.setColor(couleurJ4); break;
					}
				}
				
				if (p.getIntersections()[i][j] == null || p.getIntersections()[i][j].batiment != 2) {
					Ellipse2D e = (Ellipse2D)intersections.get(n);
					g.fillOval((int)e.getX(), (int)e.getY(), (int)e.getWidth(), (int)e.getHeight());
				}
				
				else {
					Rectangle r = (Rectangle)intersections.get(n);
					g.fillRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
				}
			
				n++;
			}
		}
		
		g.setFont(new Font(null, Font.BOLD, 20));
		
		for (int i = 0; i < p.getTuiles().length; i++) {
			for (int j = 0; j < p.getTuiles()[i].length; j++) {
				int jeton = p.getTuiles()[i][j].jeton;
				
				if (jeton > 1) {
					String nb = "" + jeton;
					int x = 35 + j * 100;
					int y = 45 + i * 100;
					
					if (nb.length() == 2)
						x -= 6;
					
					if (jeton == 6 || jeton == 8)
						g.setColor(Color.red);
					else
						g.setColor(Color.black);
					
					g.drawString(nb, x, y);
				}
			}
		}
	}

	public Object getObject(int x, int y) {
		Plateau p = vue.jeu.getPlateau();
		int a = x % 100;
		int b = y % 100;
		
		if (a % 75 == a && b % 75 == b)
			return p.getTuiles()[y / 100][x / 100];
		
		if (a % 75 != a && b % 75 != b)
			return p.getIntersections()[y / 100][x / 100];
		
		if (a % 75 == a)
			return p.getRoutes()[(y / 100) * 2 + 1][x / 100];
		
		return p.getRoutes()[(y / 100) * 2][x / 100];
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (typeAction == 0)
			return;
		
		int x = e.getX();
		int y = e.getY();

		Object o = getObject(x, y);
		Joueur j = vue.jeu.getJoueurs()[vue.indiceJoueurActuel];
		
		if (typeAction == 1) {
			if (!(o instanceof Route))
				vue.descriptifJeu.setText("Veuillez cliquer sur une route.\n");
			
			else {
				if (phaseInitiale) {
					Intersection[] in = vue.jeu.getPlateau().getAllIntersections((Route)o);
					if (in[0].joueur != j && in[1].joueur != j) {
						vue.descriptifJeu.setText("Veuillez construire votre route en contact d'une colonie.");
						return;
					}
				}
				
				else {
					Route[] r = vue.jeu.getPlateau().getAllRoutes((Route)o);
					if (r[0].joueur != j && r[1].joueur != j && r[2].joueur != j && r[3].joueur != j
							&& r[4].joueur != j && r[5].joueur != j) {
						vue.descriptifJeu.setText("Veuillez construire votre route en contact d'une autre.");
						return;
					}
				}
				
				((Route) o).construire(vue.jeu, vue.jeu.getJoueurs()[vue.indiceJoueurActuel], 0);
				vue.descriptifJeu.setText("");
				repaint();
				vue.control.setCompteursJoueur();
				vue.control.setRessourcesJoueur();
				
				if (phaseInitiale)
					vue.passer.setEnabled(true);
				else
					phaseInitiale = false;
				typeAction = 0;
			}
		}

		if (typeAction == 2 || typeAction == 3) {
			if (!(o instanceof Intersection))
				vue.descriptifJeu.setText("Veuillez cliquer sur une intersection.");
			
			else {
				if (!phaseInitiale) {
					if (typeAction == 3) {
						if (((Intersection)o).joueur != j || ((Intersection)o).batiment != 1) {
							vue.descriptifJeu.setText("Vous ne possédez pas de colonie à cette endroit.");
							return;
						}
					}
					
					else {
						Route[] r = vue.jeu.getPlateau().getAllRoutes((Intersection)o);
						if (r[0].joueur != j && r[1].joueur != j && r[2].joueur != j && r[3].joueur != j) {
							vue.descriptifJeu.setText("Veuillez construire votre intersection en contact d'une de vos route.");
							return;
						}
						
						if (!vue.jeu.getPlateau().respecteRegleDistance((Intersection)o)) {
							vue.descriptifJeu.setText("Veuillez respectez la règle de distance.");
							return;
						}
					}
				}
					
				((Intersection) o).construire(vue.jeu, vue.jeu.getJoueurs()[vue.indiceJoueurActuel], typeAction - 1);
				vue.descriptifJeu.setText("");
				repaint();
				vue.control.setCompteursJoueur();
				vue.control.setRessourcesJoueur();
				if (phaseInitiale)
					vue.construireRouteInitiale.setEnabled(true);
				else
					phaseInitiale = false;
				typeAction = 0;
			}
			
		}
		
		if (typeAction == 4) {
			if (!(o instanceof Tuile))
				vue.descriptifJeu.setText("Veuillez cliquer sur une tuile.\n");
				
			else if (((Tuile)o).voleurEstIci)
				vue.descriptifJeu.setText("Le voleur se trouve déjà sur la tuile.\n");
			
			else {
				Plateau p = vue.jeu.getPlateau();
				
				p.retireVoleur();
				p.placeVoleur((Tuile)o);
				
				// on regarde s'il y a des intersections construites autour de la nouvelle position du voleur
				LinkedList<Intersection> intersections = ((Tuile)o).getToutesInterConstruites();
				
				if (!intersections.isEmpty()) {
					
					// on recupere tous les joueurs adverses qui ont une intersection construite autour de la nouvelle position du voleur
					LinkedList<Joueur> joueurs = new LinkedList<>();
					
					for (Intersection in : intersections) {
						if (!joueurs.contains(in.joueur) && in.joueur != vue.jeu.getJoueurs()[vue.indiceJoueurActuel])
							joueurs.add(in.joueur);
					}
					
					// on choisit un adversaire aléatoire
					Random rd = new Random();
					int n = rd.nextInt(joueurs.size());
					
					// on recupere une ressource du joueur adverse de facon aleatoire
					String ressource = joueurs.get(n).perdRessourceAleatoire();
					
					// s'il n'avait aucune ressource, on l'annonce
					if (ressource.equals("rien"))
						System.out.println("Ce joueur ne possédait aucune ressource.\n");
					
					// sinon, on donne la ressource recupere au joueur actuel
					else
						vue.jeu.getJoueurs()[vue.indiceJoueurActuel].obtient(ressource, 1);
				}
				
				typeAction = 0;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}

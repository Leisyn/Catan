import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.util.Scanner;
import java.util.LinkedList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Vue {
	public Scanner sc;
	public Controleur control = new Controleur(this);
	
	private JFrame lancement = new JFrame();
	private BufferedImage imageDeFond;
	
	public JPanel listePanel = new JPanel();
	public JPanel listePanelConfig = new JPanel();
	public JPanel listePanelChoix = new JPanel();
	public JPanel listePanelPlateauMarchand = new JPanel();
	
	public CardLayout cMenu = new CardLayout();
	public String[] listeMenu = {"lancement", "configuration", "jeu"};
	
	public CardLayout cConfig = new CardLayout();
	public String[] listeConfig = {"nbJoueur", "nbIA", "nomJoueur"};
	
	public CardLayout cChoix = new CardLayout();
	public String[] listeChoix = {"phaseInitiale", "phaseProduction", "phaseCommerce", "phaseConstruction"};
	
	public CardLayout cPlateauMarchand = new CardLayout();
	public String[] listePlateauMarchand = {"plateau", "marchander"};
	
	public GridBagConstraints gb = new GridBagConstraints();
	
	private JPanel menuLancement;
	
	private JPanel menuConfiguration = new JPanel(); 
	private JPanel choixNbJoueur = new JPanel();
	public JPanel choixNbIA = new JPanel();
	public JPanel choixNomJoueur = new JPanel();
	
	public JLabel descriptifJeu = new JLabel();
	
	public JPanel menuJeu = new JPanel();
	public PlateauPanel plateau = new PlateauPanel(this);
	private JPanel marchander = new JPanel();
	private JPanel compteursActuel = new JPanel();
	private JPanel ressources = new JPanel();
	private JPanel cartes = new JPanel();
	private JPanel compteursJeu = new JPanel();
	private JPanel choixPhaseInitiale = new JPanel();
	private JPanel choixPhaseProduction = new JPanel();
	private JPanel choixPhaseCommerce = new JPanel();
	private JPanel choixPhaseConstruction = new JPanel();
	
	public JLabel nomJoueurActuel = new JLabel();
	public JLabel nbPointsActuel = new JLabel();
	public JLabel routeLaPlusLongueActuel = new JLabel();
	public JLabel armeeLaPlusPuissanteActuel = new JLabel();
	public JLabel routeLaPlusLongueJeu = new JLabel();
	public JLabel armeeLaPlusPuissanteJeu = new JLabel();
	public JLabel nbLaineActuel = new JLabel();
	public JLabel nbBoisActuel = new JLabel();
	public JLabel nbBleActuel = new JLabel();
	public JLabel nbArgileActuel = new JLabel();
	public JLabel nbMineraiActuel = new JLabel();
	public JLabel listeCarte = new JLabel();
	
	public JButton construireColonieInitiale;
	public JButton construireRouteInitiale;
	public JButton passer;
	
	public int nbJoueurs;
	public int nbIA;
	public LinkedList<Joueur> listeJoueurs = new LinkedList<>();
	
	public Jeu jeu;
	public int indiceJoueurActuel = 0;
	
	public Vue(Scanner sc) {	
		this.sc = sc;
		
		// Mise en place du menu de lancement
		JButton start = new JButton("COMMENCER");
		start.setBackground(new Color(75, 75, 75));
		start.setForeground(Color.WHITE);
		start.setFont(new Font(null, Font.BOLD, 25));
		start.addActionListener(event -> cMenu.show(listePanel, listeMenu[1]));
		
		try {
			imageDeFond = ImageIO.read(new File("./Catane.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		menuLancement = new ImageDeFondPanel(imageDeFond);
		menuLancement.add(BorderLayout.SOUTH, start);
		
		JButton lancerDe = new JButton("Lancer les dés");
		
		JTextArea nom1 = new JTextArea();
		JTextArea nom2 = new JTextArea();
		JTextArea nom3 = new JTextArea();
		JTextArea nom4 = new JTextArea();
		
		JButton valide1 = new JButton("Valider");
		valide1.addActionListener(event -> control.recupereNomJoueurEtPasse(nom1, valide1));
		
		JButton valide2 = new JButton("Valider");
		valide2.addActionListener(event -> control.recupereNomJoueurEtPasse(nom2, valide2));
		
		JButton valide3 = new JButton("Valider");
		valide3.addActionListener(event -> control.recupereNomJoueurEtPasse(nom3, valide3));
		
		JButton valide4 = new JButton("Valider");
		valide4.addActionListener(event -> control.recupereNomJoueurEtPasse(nom4, valide4));
		
		// Mise en place du choix du nombre d'IA
		JButton zeroIA = new JButton("0 IA");
		changeStyle(zeroIA);
		zeroIA.addActionListener(event -> control.changeNbIAEtPasse(0, nom1, nom2, nom3, nom4, valide1, valide2, valide3, valide4));
		
		JButton unIA = new JButton("1 IA");
		changeStyle(unIA);
		unIA.addActionListener(event -> control.changeNbIAEtPasse(1, nom1, nom2, nom3, nom4, valide1, valide2, valide3, valide4));
		
		JButton deuxIA = new JButton("2 IA");
		changeStyle(deuxIA);
		deuxIA.addActionListener(event -> control.changeNbIAEtPasse(2, nom1, nom2, nom3, nom4, valide1, valide2, valide3, valide4));
		
		JButton troisIA = new JButton("3 IA");
		changeStyle(troisIA);
		troisIA.addActionListener(event -> control.changeNbIAEtPasse(3, nom1, nom2, nom3, nom4, valide1, valide2, valide3, valide4));
		
		JButton quatreIA = new JButton("4 IA");
		changeStyle(quatreIA);
		quatreIA.addActionListener(event -> control.changeNbIAEtPasse(4, nom1, nom2, nom3, nom4, valide1, valide2, valide3, valide4));
		
		choixNbIA.setLayout(new GridBagLayout());

		gb.gridx = 0;  // place sur la 1er colonne
		gb.gridy = 0;  // place sur la 1er ligne
		gb.weightx = 1;
		gb.weighty = 1;
		gb.insets = new Insets(50, 50, 25, 25);
		gb.fill = GridBagConstraints.BOTH;
		gb.anchor = GridBagConstraints.CENTER;  // ancre au milieu-gauche de la fenetre
		choixNbIA.add(zeroIA, gb);
		
		gb.gridx = 1;
		gb.insets = new Insets(50, 25, 25, 50);
		choixNbIA.add(unIA, gb);
		
		gb.gridx = 0;  
		gb.gridy = 1;  // place sur la 2e ligne
		gb.insets = new Insets(25, 50, 25, 25);
		choixNbIA.add(deuxIA, gb);
		
		gb.gridx = 1;
		gb.insets = new Insets(25, 25, 25, 50);
		choixNbIA.add(troisIA, gb);
		
		// Le bouton quatreIA est mis en place dans la méthode changeNbJoueurEtPasse, dans Controleur
		
		
		
		// Mise en place du choix du nombre de joueurs
		JButton troisJoueurs = new JButton("3 JOUEURS");
		changeStyle(troisJoueurs);
		troisJoueurs.addActionListener(event -> control.changeNbJoueurEtPasse(3, quatreIA));
		
		JButton quatreJoueurs = new JButton("4 JOUEURS");
		changeStyle(quatreJoueurs);
		quatreJoueurs.addActionListener(event -> control.changeNbJoueurEtPasse(4, quatreIA));
		
		choixNbJoueur.setLayout(new GridBagLayout());
		
		gb.gridx = 0;  // place sur la 1er colonne
		gb.gridy = 0;  // place sur la 1er ligne
		gb.weightx = 1;
		gb.weighty = 1;
		gb.insets = new Insets(50, 50, 50, 50);  // padding
		gb.fill = GridBagConstraints.BOTH;  // remplit verticalement et horizontalement l'espace
		gb.anchor = GridBagConstraints.LINE_START;
		choixNbJoueur.add(troisJoueurs, gb);
		
		gb.gridx = 1;  // place sur la 2e colonne
		gb.anchor = GridBagConstraints.LINE_END;  // ancre en bas-droite de la fenetre
		choixNbJoueur.add(quatreJoueurs, gb);
		
		
		
		// Mise en place du compteur du joueur actuel
		nomJoueurActuel.setHorizontalAlignment(JLabel.CENTER);
		nbPointsActuel.setHorizontalAlignment(JLabel.CENTER);
		routeLaPlusLongueActuel.setHorizontalAlignment(JLabel.CENTER);
		armeeLaPlusPuissanteActuel.setHorizontalAlignment(JLabel.CENTER);
		
		compteursActuel.setLayout(new GridLayout(4, 1));
		compteursActuel.setPreferredSize(new Dimension(250, 100));
		compteursActuel.add(nomJoueurActuel);
		compteursActuel.add(nbPointsActuel);
		compteursActuel.add(routeLaPlusLongueActuel);
		compteursActuel.add(armeeLaPlusPuissanteActuel);
		
		
		
		// Mise en place du compteur du jeu
		routeLaPlusLongueJeu.setHorizontalAlignment(JLabel.CENTER);
		armeeLaPlusPuissanteJeu.setHorizontalAlignment(JLabel.CENTER);

		compteursJeu.setLayout(new GridLayout(2, 1));
		compteursJeu.setPreferredSize(new Dimension(250, 100));
		compteursJeu.add(routeLaPlusLongueJeu);
		compteursJeu.add(armeeLaPlusPuissanteJeu);
		
		
		
		// Mise en place des ressources du joueur
		nbLaineActuel.setHorizontalAlignment(JLabel.CENTER);
		nbBoisActuel.setHorizontalAlignment(JLabel.CENTER);
		nbBleActuel.setHorizontalAlignment(JLabel.CENTER);
		nbArgileActuel.setHorizontalAlignment(JLabel.CENTER);
		nbMineraiActuel.setHorizontalAlignment(JLabel.CENTER);
		
		ressources.setLayout(new GridLayout(5, 1));
		ressources.setPreferredSize(new Dimension(250, 200));
		ressources.add(nbArgileActuel);
		ressources.add(nbBleActuel);
		ressources.add(nbBoisActuel);
		ressources.add(nbLaineActuel);
		ressources.add(nbMineraiActuel);
		
		
		
		JLabel phaseProduction, phaseCommerce, phaseConstruction, etiqMarchander, phaseInitiale;
		
		JButton jouerCarte1, jouerCarte2, boutonMarchander,
		passerPhaseConstruction, construireRoute, construireColonie, construireVille,
		acheterCarte, jouerCarte3, passerTour, donnerLaine, donnerBois, donnerBle,
		donnerArgile, donnerMinerai, recevoirLaine, recevoirBois, recevoirBle,
		recevoirArgile, recevoirMinerai, retour;
		
		
		descriptifJeu.setHorizontalAlignment(JLabel.CENTER);
		
		
		phaseInitiale = new JLabel("PHASE INITIALE");
		phaseInitiale.setHorizontalAlignment(JLabel.CENTER);
		
		construireColonieInitiale = new JButton("Construire colonie");
		
		construireRouteInitiale = new JButton("Construire route");
		construireRouteInitiale.setEnabled(false);
		
		passer = new JButton("Passer");
		passer.setEnabled(false);
		
		construireColonieInitiale.addActionListener(event -> {
			construireColonieInitiale.setEnabled(false);
			descriptifJeu.setText("Veuillez cliquer sur la colonie à construire.");
			plateau.typeAction = 2;
			plateau.phaseInitiale = true;
		});
		
		construireRouteInitiale.addActionListener(event -> {
			construireRouteInitiale.setEnabled(false);
			descriptifJeu.setText("Veuillez cliquer sur la route à construire.");
			plateau.typeAction = 1;
		});
		
		passer.addActionListener(event -> {
			passer.setEnabled(false);
			control.changeJoueurOuPassePhaseProduction();
			control.setCompteursJoueur();
			control.setRessourcesJoueur();
			construireColonieInitiale.setEnabled(true);
		});
		
		choixPhaseInitiale.setLayout(new GridBagLayout());
		choixPhaseInitiale.setPreferredSize(new Dimension(250, 200));
		
		gb.gridx = 0;
		gb.gridy = 0;
		gb.gridheight = 1;
		gb.gridwidth = 2;
		gb.weightx = 1;
		gb.weighty = 0.1;
		gb.fill = GridBagConstraints.BOTH;
		gb.insets = new Insets(15, 0, 5, 0);
		gb.anchor = GridBagConstraints.PAGE_START;
		choixPhaseInitiale.add(phaseInitiale, gb);
		
		gb.gridy = 1;
		gb.gridwidth = 1;
		gb.weighty = 1;
		gb.insets = new Insets(10, 10, 10, 10);
		gb.anchor = GridBagConstraints.CENTER;
		choixPhaseInitiale.add(construireColonieInitiale, gb);
		
		gb.gridx = 1;
		choixPhaseInitiale.add(construireRouteInitiale, gb);
		
		gb.gridx = 0;
		gb.gridy = 2;
		gb.gridwidth = 2;
		choixPhaseInitiale.add(passer, gb);
		
		
		// Mise en place des choix du joueur pendant la phase de production
		phaseProduction = new JLabel("PHASE DE PRODUCTION");
		phaseProduction.setHorizontalAlignment(JLabel.CENTER);
		
		jouerCarte1 = new JButton("Jouer une carte");
		jouerCarte1.setEnabled(false);
		
		lancerDe.addActionListener(event -> {
			int n = jeu.lanceDe();
			cChoix.show(listePanelChoix, listeChoix[2]);
		});
		
		choixPhaseProduction.setLayout(new GridBagLayout());
		choixPhaseProduction.setPreferredSize(new Dimension(250, 200));
		
		gb.gridx = 0;
		gb.gridy = 0;
		gb.gridwidth = 2;
		gb.weightx = 1;
		gb.weighty = 0.1;
		gb.fill = GridBagConstraints.BOTH;
		gb.insets = new Insets(15, 0, 5, 0);
		gb.anchor = GridBagConstraints.PAGE_START;
		choixPhaseProduction.add(phaseProduction, gb);
		
		gb.gridy = 1;
		gb.gridwidth = 1;
		gb.weighty = 1;
		gb.insets = new Insets(70, 10, 70, 10);
		gb.anchor = GridBagConstraints.CENTER;
		choixPhaseProduction.add(jouerCarte1, gb);
		
		gb.gridx = 1;
		choixPhaseProduction.add(lancerDe, gb);
		
		
		
		// Mise en place des choix du joueur pendant la phase de commerce
		phaseCommerce = new JLabel("PHASE DE COMMERCE");
		phaseCommerce.setHorizontalAlignment(JLabel.CENTER);
		
		jouerCarte2 = new JButton("Jouer une carte");
		jouerCarte2.setEnabled(false);
		
		boutonMarchander = new JButton("Marchander");
		
		passerPhaseConstruction = new JButton("Passer à la phase de construction");
		
		passerPhaseConstruction.addActionListener(event -> {
			cPlateauMarchand.show(listePanelPlateauMarchand, listePlateauMarchand[0]);
			cChoix.show(listePanelChoix, listeChoix[3]);
		});
		
		boutonMarchander.addActionListener(event -> cPlateauMarchand.show(listePanelPlateauMarchand, listePlateauMarchand[1]));
		
		choixPhaseCommerce.setLayout(new GridBagLayout());
		choixPhaseCommerce.setPreferredSize(new Dimension(250, 200));
		
		gb.gridx = 0;
		gb.gridy = 0;
		gb.gridwidth = 2;
		gb.weighty = 0.1;
		gb.insets = new Insets(15, 0, 5, 0);
		gb.anchor = GridBagConstraints.PAGE_START;
		choixPhaseCommerce.add(phaseCommerce, gb);
		
		gb.gridy = 1;
		gb.gridwidth = 1;
		gb.weighty = 1;
		gb.insets = new Insets(10, 10, 10, 10);
		gb.anchor = GridBagConstraints.CENTER;
		choixPhaseCommerce.add(jouerCarte2, gb);
		
		gb.gridx = 1;
		choixPhaseCommerce.add(boutonMarchander, gb);
		
		gb.gridx = 0;
		gb.gridy = 2;
		gb.gridwidth = 2;
		choixPhaseCommerce.add(passerPhaseConstruction, gb);
		
		
		
		// Mise en place des choix du joueur pendant la phase de construction
		phaseConstruction = new JLabel("PHASE DE CONSTRUCTION");
		phaseConstruction.setHorizontalAlignment(JLabel.CENTER);
		
		construireRoute = new JButton("Construire route");
		construireRoute.setEnabled(false);
		construireRoute.addActionListener(event -> {
			plateau.typeAction = 1;
		});
		
		construireColonie = new JButton("Construire colonie");
		construireColonie.setEnabled(false);
		construireColonie.addActionListener(event -> {
			plateau.typeAction = 2;
		});
		
		construireVille = new JButton("Construire ville");
		construireVille.setEnabled(false);
		construireVille.addActionListener(event -> {
			plateau.typeAction = 3;
		});
		
		acheterCarte = new JButton("Acheter une carte");
		acheterCarte.setEnabled(false);
		
		jouerCarte3 = new JButton("Jouer une carte");
		jouerCarte3.setEnabled(false);
		
		passerTour = new JButton("Passer");
		passerTour.addActionListener(event -> {
			if (jeu != null && jeu.getJoueurs().length > 0) {
				indiceJoueurActuel = (indiceJoueurActuel + 1) % jeu.getJoueurs().length;
				control.setAllCompteurs();
				cChoix.show(listePanelChoix, listeChoix[1]);
			}
		});
		
		choixPhaseConstruction.setLayout(new GridBagLayout());
		choixPhaseConstruction.setPreferredSize(new Dimension(250, 200));
		
		gb.gridx = 0;
		gb.gridy = 0;
		gb.gridwidth = 2;
		gb.weighty = 0.1;
		gb.insets = new Insets(15, 0, 5, 0);
		gb.anchor = GridBagConstraints.PAGE_START;
		choixPhaseConstruction.add(phaseConstruction, gb);
		
		gb.gridy = 1;
		gb.gridwidth = 1;
		gb.weighty = 1;
		gb.insets = new Insets(10, 10, 10, 10);
		gb.anchor = GridBagConstraints.CENTER;
		choixPhaseConstruction.add(construireRoute, gb);
		
		gb.gridx = 1;
		choixPhaseConstruction.add(construireColonie, gb);
		
		gb.gridx = 0;
		gb.gridy = 2;
		choixPhaseConstruction.add(construireVille, gb);
		
		gb.gridx = 1;
		choixPhaseConstruction.add(acheterCarte, gb);
		
		gb.gridx = 0;
		gb.gridy = 3;
		choixPhaseConstruction.add(jouerCarte3, gb);
		
		gb.gridx = 1;
		choixPhaseConstruction.add(passerTour, gb);
		
		// Mise en place de l'echange
		etiqMarchander = new JLabel("Marchander");
		etiqMarchander.setHorizontalAlignment(JLabel.CENTER);
		
		donnerLaine = new JButton("4 laine");
		donnerLaine.setEnabled(false);
		
		donnerBois = new JButton("4 bois");
		donnerBois.setEnabled(false);
		
		donnerBle = new JButton("4 blé");
		donnerBle.setEnabled(false);
		
		donnerArgile = new JButton("4 argile");
		donnerArgile.setEnabled(false);
		
		donnerMinerai = new JButton("4 minerai");
		donnerMinerai.setEnabled(false);
		
		recevoirLaine = new JButton("1 laine");
		recevoirLaine.setEnabled(false);
		
		recevoirBois = new JButton("1 bois");
		recevoirBois.setEnabled(false);
		
		recevoirBle = new JButton("1 blé");
		recevoirBle.setEnabled(false);
		
		recevoirArgile = new JButton("1 argile");
		recevoirArgile.setEnabled(false);
		
		recevoirMinerai = new JButton("1 minerai");
		recevoirMinerai.setEnabled(false);
		
		retour = new JButton("Revenir");
		retour.addActionListener(event -> cPlateauMarchand.show(listePanelPlateauMarchand, listePlateauMarchand[0]));
		
		marchander.setLayout(new GridBagLayout());
		marchander.setPreferredSize(new Dimension(600, 600));
		
		gb.gridx = 0;
		gb.gridy = 0;
		gb.gridwidth = 5;
		gb.weighty = 0.1;
		gb.insets = new Insets(10, 0, 0, 0);
		gb.anchor = GridBagConstraints.PAGE_START;
		marchander.add(etiqMarchander, gb);

		gb.gridy = 1;
		gb.gridwidth = 1;
		gb.weighty = 1;
		gb.insets = new Insets(30, 10, 30, 10);
		gb.anchor = GridBagConstraints.CENTER;
		marchander.add(donnerArgile, gb);
		
		gb.gridx = 1;
		marchander.add(donnerBle, gb);
		
		gb.gridx = 2;
		marchander.add(donnerBois, gb);
		
		gb.gridx = 3;
		marchander.add(donnerLaine, gb);
		
		gb.gridx = 4;
		marchander.add(donnerMinerai, gb);
		
		gb.gridx = 0;
		gb.gridy = 2;
		marchander.add(recevoirArgile, gb);
		
		gb.gridx = 1;
		marchander.add(recevoirBle, gb);
		
		gb.gridx = 2;
		marchander.add(recevoirBois, gb);
		
		gb.gridx = 3;
		marchander.add(recevoirLaine, gb);
		
		gb.gridx = 4;
		marchander.add(recevoirMinerai, gb);
		
		gb.gridx = 0;
		gb.gridy = 3;
		gb.gridwidth = 5;
		gb.weighty = 0.1;
		gb.insets = new Insets(0, 10, 10, 10);
		marchander.add(retour, gb);
		
		
		
		
		
		JLabel configuration = new JLabel("Configuration du Jeu");
		configuration.setHorizontalAlignment(JLabel.CENTER);
		configuration.setFont(new Font(null, Font.BOLD, 25));
		configuration.setBorder(new EmptyBorder(40, 0, 0, 0));
		
		JLabel nomJeu = new JLabel("Les Colons de Catane");
		nomJeu.setHorizontalAlignment(JLabel.CENTER);


		
		// Mise en place du choix du nombre de joueurs
		JLabel etiqCartes = new JLabel("Liste de cartes :");
		etiqCartes.setHorizontalAlignment(JLabel.CENTER);
		listeCarte.setHorizontalAlignment(JLabel.CENTER);
		
		cartes.add(BorderLayout.NORTH, etiqCartes);
		cartes.add(BorderLayout.CENTER, listeCarte);
		
		
		menuJeu.setLayout(new GridBagLayout());
		menuJeu.setBackground(Color.red);
		
		gb.gridx = 0;  // place sur la 1er colonne
		gb.gridy = 0;  // place sur la 1er ligne
		gb.gridheight = 1;
		gb.gridwidth = 3;  // prend 3 colonne en largeur
		gb.fill = GridBagConstraints.BOTH;
		gb.weighty = 0.1;
		gb.insets = new Insets(15, 15, 15, 15);
		gb.anchor = GridBagConstraints.PAGE_START;
		menuJeu.add(nomJeu, gb);
		
		gb.gridy = 1;
		gb.gridwidth = 1;
		gb.weightx = 0.2;
		gb.weighty = 0.2;
		menuJeu.add(compteursActuel, gb);
		
		gb.gridy = 2;
		gb.weighty = 0.4;
		menuJeu.add(ressources, gb);
		
		gb.gridy = 3;
		gb.anchor = GridBagConstraints.PAGE_END;
		menuJeu.add(cartes, gb);
		
		gb.gridx = 2;
		gb.gridy = 1;
		gb.weighty = 0.1;
		gb.anchor = GridBagConstraints.PAGE_START;
		menuJeu.add(compteursJeu, gb);
		
		gb.gridy = 2;
		gb.weighty = 0.4;
		menuJeu.add(descriptifJeu, gb);
		
		gb.gridy = 3;
		gb.anchor = GridBagConstraints.PAGE_END;
		menuJeu.add(listePanelChoix, gb);
		
		gb.gridx = 1;
		gb.gridy = 1;
		gb.gridheight = 3;
		gb.weighty = 0.9;
		gb.anchor = GridBagConstraints.CENTER;
		menuJeu.add(listePanelPlateauMarchand, gb);
		
		
		
		
		menuConfiguration.setLayout(new BorderLayout(0, 50));
		menuConfiguration.add(BorderLayout.NORTH, configuration);
		menuConfiguration.add(BorderLayout.CENTER, listePanelConfig);
		
		
		// Mise en place du menu
		listePanel.setLayout(cMenu);
		listePanel.add(menuLancement, listeMenu[0]);
		listePanel.add(menuConfiguration, listeMenu[1]);
		listePanel.add(menuJeu, listeMenu[2]);
		
		listePanelConfig.setLayout(cConfig);
		listePanelConfig.add(choixNbJoueur, listeConfig[0]);
		listePanelConfig.add(choixNbIA, listeConfig[1]);
		listePanelConfig.add(choixNomJoueur, listeConfig[2]);
		
		listePanelChoix.setLayout(cChoix);
		listePanelChoix.add(choixPhaseInitiale, listeChoix[0]);
		listePanelChoix.add(choixPhaseProduction, listeChoix[1]);
		listePanelChoix.add(choixPhaseCommerce, listeChoix[2]);
		listePanelChoix.add(choixPhaseConstruction, listeChoix[3]);
		
		plateau.setPreferredSize(new Dimension(600, 600));
		listePanelPlateauMarchand.setLayout(cPlateauMarchand);
		listePanelPlateauMarchand.add(plateau, listePlateauMarchand[0]);
		listePanelPlateauMarchand.add(marchander, listePlateauMarchand[1]);
		
		// Mise en place de la fenetre
		lancement.setTitle("Les Colons de Catane");
		lancement.setSize(imageDeFond.getWidth(), imageDeFond.getHeight());
		lancement.setVisible(true);
		lancement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		lancement.getContentPane().add(listePanel);
	}
	
	private void changeStyle(JButton bouton) {
		bouton.setBackground(new Color(162, 107, 95));
		bouton.setForeground(Color.WHITE);
		bouton.setFont(new Font(null, Font.BOLD, 25));
	}
	
	private class ImageDeFondPanel extends JPanel {
		Image image;
		
		ImageDeFondPanel(Image image) {
			this.image = image;
			setLayout(new BorderLayout());
		}
		
		@Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, this);
	    }
	}
}

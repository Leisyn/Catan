package catan.controller;

import javax.swing.*;

import catan.model.Game;
import catan.model.card.Card;
import catan.model.player.Player;
import catan.model.player.Robot;
import catan.model.player.Player.Resource;
import catan.view.View;

import java.awt.*;

public class Controller {
	private View vue;
	
	public Controller(View vue) {
		this.vue = vue;
	}
	
	public void changeNbJoueurEtPasse(int n, JButton quatreIA) {
		vue.nbJoueurs = n;
		vue.jeu = new Game(vue.nbJoueurs, vue.sc);
		
		vue.cConfig.show(vue.listePanelConfig, vue.listeConfig[1]);
		
		if (vue.nbJoueurs == 4) {
			vue.gb.gridx = 0;
			vue.gb.gridy = 2;  // place sur la 3e ligne
			vue.gb.gridwidth = 2;
			vue.gb.insets = new Insets(25, 50, 50, 50);
			vue.choixNbIA.add(quatreIA, vue.gb);
		}
	}
	
	public void changeNbIAEtPasse(int n, JTextArea nom1, JTextArea nom2, JTextArea nom3,
			JTextArea nom4, JButton valide1, JButton valide2, JButton valide3, JButton valide4) {
		
		vue.nbIA = n;
		int nbHumain = vue.nbJoueurs - n;
		
		if (nbHumain == 0) {
			for (int i = 1; i <= vue.nbJoueurs; i++)
				vue.listeJoueurs.add(new Robot(("IA " + i), vue.jeu));
			
			vue.jeu.iniPlayers(vue.listeJoueurs);
			vue.cMenu.show(vue.listePanel, vue.listeMenu[2]);
			setAllCompteurs();
			return;
		}
		
		vue.choixNomJoueur.setLayout(new GridBagLayout());
		
		vue.gb.gridx = 0;
		vue.gb.gridy = 0;
		vue.gb.gridwidth = 1;
		vue.gb.gridheight = 1;
		vue.gb.weightx = 1;
		vue.gb.weighty = 1;
		vue.gb.insets = new Insets(50, 50, 50, 50);
		vue.gb.anchor = GridBagConstraints.PAGE_START;
		vue.choixNomJoueur.add(nom1, vue.gb);
		
		vue.gb.gridy = 1;
		vue.gb.insets = new Insets(10, 50, 10, 50);
		vue.gb.anchor = GridBagConstraints.PAGE_END;
		vue.choixNomJoueur.add(valide1, vue.gb);
		
		if (nbHumain == 2 || nbHumain == 3 || nbHumain == 4) {
			vue.gb.gridx = 1;
			vue.gb.gridy = 0;
			vue.gb.insets = new Insets(50, 50, 50, 50);
			vue.gb.anchor = GridBagConstraints.PAGE_START;
			vue.choixNomJoueur.add(nom2, vue.gb);
			
			vue.gb.gridy = 1;
			vue.gb.insets = new Insets(10, 50, 10, 50);
			vue.gb.anchor = GridBagConstraints.PAGE_END;
			vue.choixNomJoueur.add(valide2, vue.gb);
		}
		
		if (nbHumain == 3 || nbHumain == 4) {
			vue.gb.gridx = 0;
			vue.gb.gridy = 2;
			vue.gb.insets = new Insets(50, 50, 50, 50);
			vue.gb.anchor = GridBagConstraints.PAGE_START;
			if (nbHumain == 3) vue.gb.gridwidth = 2;
			vue.choixNomJoueur.add(nom3, vue.gb);
			
			vue.gb.gridy = 3;
			vue.gb.insets = new Insets(10, 50, 10, 50);
			vue.gb.anchor = GridBagConstraints.PAGE_END;
			vue.choixNomJoueur.add(valide3, vue.gb);
		}
		
		if (nbHumain == 4) {
			vue.gb.gridx = 1;
			vue.gb.gridy = 2;
			vue.gb.insets = new Insets(50, 50, 50, 50);
			vue.gb.anchor = GridBagConstraints.PAGE_START;
			vue.choixNomJoueur.add(nom4, vue.gb);
			
			vue.gb.gridy = 3;
			vue.gb.insets = new Insets(10, 50, 10, 50);
			vue.gb.anchor = GridBagConstraints.PAGE_END;
			vue.choixNomJoueur.add(valide4, vue.gb);
		}
		
		
		vue.cConfig.show(vue.listePanelConfig, vue.listeConfig[2]);
	}
	
	public void recupereNomJoueurEtPasse(JTextArea nom, JButton valide) {
		if (nom.getText() == null)
			return;
		
		int nbHumain = vue.nbJoueurs - vue.nbIA;
		
		String n = nom.getText();
		valide.setEnabled(false);
		vue.listeJoueurs.add(new Player(n, vue.jeu));
		
		if (vue.listeJoueurs.size() == vue.nbJoueurs) {
			vue.jeu.iniPlayers(vue.listeJoueurs);
			vue.cMenu.show(vue.listePanel, vue.listeMenu[2]);
			setAllCompteurs();
		}
		
		else if (vue.listeJoueurs.size() == nbHumain) {
			for (int i = nbHumain + 1; i <= vue.nbJoueurs; i++)
				vue.listeJoueurs.add(new Player("Robot " + i, vue.jeu));
			vue.jeu.iniPlayers(vue.listeJoueurs);
			vue.cMenu.show(vue.listePanel, vue.listeMenu[2]);
			setAllCompteurs();
		}
	}
	
	public void setAllCompteurs() {
		setCompteursJoueur();
		setRessourcesJoueur();
		setCartesJoueur();
		setCompteursJeu();
	}
	
	public void joueurAChange() {
		setCompteursJoueur();
		setRessourcesJoueur();
		setCartesJoueur();
	}
	
	public void setCompteursJoueur() {
		Player j = vue.jeu.getPlayers()[vue.indiceJoueurActuel];

		vue.nomJoueurActuel.setText("Tour de " + j.name);
		vue.nbPointsActuel.setText("Nombre de points : " + j.points);
		vue.routeLaPlusLongueActuel.setText("Route la plus longue : " + j.longestRoad);
		vue.armeeLaPlusPuissanteActuel.setText("Arm�e la plus puissante : " + j.largestArmy);
	}
	
	public void setCompteursJeu() {
		if (vue.jeu.playerWithLongestRoad != null)
			vue.routeLaPlusLongueJeu.setText("Route la plus longue : " + vue.jeu.playerWithLongestRoad + " (" + vue.jeu.playerWithLongestRoad.longestRoad + ")");
		else
			vue.routeLaPlusLongueJeu.setText("Route la plus longue : personne");
		
		if (vue.jeu.playerWithLargestArmy != null)
			vue.armeeLaPlusPuissanteJeu.setText("Arm�e la plus puissante : " + vue.jeu.playerWithLargestArmy + " (" + vue.jeu.playerWithLargestArmy.largestArmy + ")");
		else
			vue.armeeLaPlusPuissanteJeu.setText("Arm�e la plus puissante : personne");
	}
	
	public void setRessourcesJoueur() {
		Player j = vue.jeu.getPlayers()[vue.indiceJoueurActuel];
		
		vue.nbArgileActuel.setText("Argile : " + j.getResources().get(Resource.BRICK));
		vue.nbBleActuel.setText("Bl� : " + j.getResources().get(Resource.GRAIN));
		vue.nbBoisActuel.setText("Bois : " + j.getResources().get(Resource.LUMBER));
		vue.nbLaineActuel.setText("Laine : " + j.getResources().get(Resource.WOOL));
		vue.nbMineraiActuel.setText("Minerai : " + j.getResources().get(Resource.ORE));
	}
	
	public void setCartesJoueur() {
		Player j = vue.jeu.getPlayers()[vue.indiceJoueurActuel];
		
		for (Card c : j.cards)
			vue.listeCarte.setText(vue.listeCarte.getText() + "\n* " + c);
	}
	
	public void autorisePhaseProductionEtCommerce(JButton jouerCarte) {
		Player j = vue.jeu.getPlayers()[vue.indiceJoueurActuel];
		
		if (j.hasAPlayableCard())
			jouerCarte.setEnabled(true);
		else
			jouerCarte.setEnabled(false);
	}
	
	public void autoriseMarchander(JButton donnerArg, JButton donnerBle, JButton donnerBois,
			JButton donnerLaine, JButton donnerMin, JButton recevoirArg, JButton recevoirBle,
			JButton recevoirBois, JButton recevoirLaine, JButton recevoirMin) {
		Player j = vue.jeu.getPlayers()[vue.indiceJoueurActuel];
		boolean peutEchanger = false;
		
		if (j.getResources().get(Resource.BRICK) < j.getTradeRate().get(Resource.BRICK))
			donnerArg.setEnabled(false);
		
		else {
			peutEchanger = true;
			donnerArg.setEnabled(true);
		}
		
		if (j.getResources().get(Resource.GRAIN) < j.getTradeRate().get(Resource.GRAIN))
			donnerBle.setEnabled(false);
		
		else {
			peutEchanger = true;
			donnerBle.setEnabled(true);
		}
		
		if (j.getResources().get(Resource.LUMBER) < j.getTradeRate().get(Resource.LUMBER))
			donnerBois.setEnabled(false);
		
		else {
			peutEchanger = true;
			donnerBois.setEnabled(true);
		}
		
		if (j.getResources().get(Resource.WOOL) < j.getTradeRate().get(Resource.WOOL))
			donnerLaine.setEnabled(false);
		
		else {
			peutEchanger = true;
			donnerLaine.setEnabled(true);
		}
		
		if (j.getResources().get(Resource.ORE) < j.getTradeRate().get(Resource.ORE))
			donnerMin.setEnabled(false);
		
		else {
			peutEchanger = true;
			donnerMin.setEnabled(true);
		}
		
		if (peutEchanger) {
			recevoirArg.setEnabled(true);
			recevoirBle.setEnabled(true);
			recevoirBois.setEnabled(true);
			recevoirLaine.setEnabled(true);
			recevoirMin.setEnabled(true);
		} else {
			recevoirArg.setEnabled(false);
			recevoirBle.setEnabled(false);
			recevoirBois.setEnabled(false);
			recevoirLaine.setEnabled(false);
			recevoirMin.setEnabled(false);
		}
	}
	
	public void autorisePhaseConstruction(JButton route, JButton colonie,
			JButton ville, JButton achete, JButton jouerCarte) {
		Player j = vue.jeu.getPlayers()[vue.indiceJoueurActuel];
		
		if (j.hasTheResourcesTo("R"))
			route.setEnabled(true);
		else
			route.setEnabled(false);
		
		if (j.hasTheResourcesTo("C"))
			colonie.setEnabled(true);
		else
			colonie.setEnabled(false);
		
		if (j.hasTheResourcesTo("V"))
			ville.setEnabled(true);
		else
			ville.setEnabled(false);
		
		if (j.hasTheResourcesTo("A"))
			achete.setEnabled(true);
		else
			achete.setEnabled(false);
		
		if (j.hasAPlayableCard())
			jouerCarte.setEnabled(true);
		else
			jouerCarte.setEnabled(false);
	}
	
	public void changeJoueurOuPassePhaseProduction() {
		int c = vue.jeu.getPlayers()[vue.indiceJoueurActuel].getNumSettlement();
		
		if (c == 2 && vue.indiceJoueurActuel == 0) {
			vue.cChoix.show(vue.listePanelChoix, vue.listeChoix[1]);
			return;
		}
		
		if (c == 1 && vue.indiceJoueurActuel < vue.nbJoueurs - 1) {
			System.out.println("?b?");
			vue.indiceJoueurActuel++;
			joueurAChange();
		}
		
		else if (c == 2 && vue.indiceJoueurActuel > 0) {
			System.out.println("??");
			vue.indiceJoueurActuel--;
			joueurAChange();
		}
	}
	
	public boolean phaseInitialeFini() {
		for (int i = 0; i < vue.nbJoueurs; i++) {
			if (vue.jeu.getPlayers()[i].getNumSettlement() != 2 || vue.jeu.getPlayers()[i].getNumRoad() != 2)
				return false;
		}
		return true;
	}
	
	public void lanceDe() {
		int n = vue.jeu.throwDice();
		vue.descriptifJeu.setText("Un " + n + " a �t� lanc� !\n");
		
		if (n != 7)
			vue.jeu.giveResources(n, vue.jeu.getPlayers()[vue.indiceJoueurActuel]);
		else
			auVoleur();
	}
	
	public void auVoleur() {
		vue.descriptifJeu.setText(vue.descriptifJeu.getText() + "\nAu voleur !\nCliquez sur une tuile pour d�placer le voleur.");
		vue.plateau.typeAction = 4;
	}
}

package catan;

import java.util.Scanner;

import catan.model.Jeu;
import catan.model.player.Joueur;
import catan.model.player.Robot;
import catan.view.Vue;

import java.awt.EventQueue;
import java.util.LinkedList;

public class Lanceur {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String reponse = null;
		
		while (reponse == null) {
			System.out.println("Voulez-vous jouer sous interface graphique, ou sous interface textuelle ? [G / T]");
			reponse = sc.next().toUpperCase();
			if (!reponse.equals("G") && !reponse.equals("T")) {
				System.out.println("Erreur.\n");
				reponse = null;
			}
		}
		
		if (reponse.equals("G")) {
			new Vue(sc);
		}
		
		else {
			// on demande le nombre de joueurs que l'utilisateur souhaite
			while (reponse == null) {
				System.out.println("Combien de joueurs voulez-vous dans la partie ? (3 ou 4)");
				reponse = sc.next();
				if (!reponse.equals("3") && !reponse.equals("4")) {
					System.out.println("Erreur.\n");
					reponse = null;
				}
			}
			System.out.println();
			
			// on cr�e le jeu contenant le nombre de joueurs souhait�s
			int nbJoueur = Integer.parseInt(reponse);
			Jeu jeu = new Jeu(nbJoueur, sc);
	
			reponse = null;
			
			// on demande a l'utilisateur le nombre d'IA qu'il souhaite avoir dans son jeu
			while (reponse == null) {
				System.out.println("Combien d'IA souhaitez-vous avoir dans la partie ? " + "(" + nbJoueur + " maximum)");
				reponse = sc.next();
				if (!reponse.matches("\\d*")) {
					System.out.println("Erreur.\n");
					reponse = null;
				} else if (Integer.parseInt(reponse) < 0 || Integer.parseInt(reponse) > nbJoueur) {
					System.out.println("Erreur.\n");
					reponse = null;
				}
			}
			System.out.println();
			
			int nbIA = Integer.parseInt(reponse);
			
			LinkedList<Joueur> joueurs = new LinkedList<>();
	
			int nbHumain = nbJoueur - nbIA;
				
			// pour chaque joueur humain, on demande leur nom et on les ajoute aux joueurs de la partie
			for (int i = 1; i <= nbHumain; i++) {
				System.out.println("Joueur " + i + " : Quel est votre nom ?");
				joueurs.add(new Joueur(sc.next(), jeu));
				System.out.println();
			}
			
			// on ajoute les IA, s'il y en a, aux joueurs de la partie
			for (int i = nbHumain + 1; i <= nbJoueur; i++)
				joueurs.add(new Robot("IA " + i, jeu));
	
			// on ajoute les joueurs au jeu
			jeu.iniJoueurs(joueurs);
			
			// on lance le jeu
			jeu.partie();
		}
		
		sc.close();
	}
}

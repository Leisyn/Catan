package catan.model.board;

import catan.model.Jeu;
import catan.model.player.Joueur;

public interface IConstructible {
	public void construire(Jeu jeu, Joueur j, int type);
}

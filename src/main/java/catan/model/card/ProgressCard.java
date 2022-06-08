package catan.model.card;

import catan.model.Game;
import catan.model.board.Buildable.Construction;
import catan.model.player.Player;
import catan.model.player.Player.Resource;

public class ProgressCard extends Card {

	public ProgressCard(String name) {
		super(1, name);
	}

	public void jouer(Game game, Player p) {
		// s'il s'agit d'une carte construction
		if (this.name.equals("construction")) {
			
			// on construit 2 routes
			//p.build(Construction.ROAD, false);
			//p.build(Construction.ROAD, false);
		}
		
		// s'il s'agit d'une carte invention
		if (this.name.equals("invention")) {
			
			// on demande la ressource que le joueur veut recevoir et on lui donne
			Resource askedResource = null;
			//while (askedResource == null) askedResource = p.askResourceToReceive(1);
			
			// on donne au joueur la ressource qu'il a souhait�
			p.receiveResources(askedResource, 1);

			
			// on demande la 2e ressource que le joueur veut recevoir et on lui donne
			Resource askedResource2 = null;
			//while (askedResource2 == null) askedResource2 = p.askResourceToReceive(1);
			
			// on donne au joueur la ressource qu'il a souhait�
			p.receiveResources(askedResource2, 1);
		}
	
		// s'il s'agit d'une carte monopole
		if (this.name.equals("monopole")) {

			// on demande la ressource que le joueur veut recevoir
			Resource askedResource = null;
			//while (askedResource == null) askedResource = p.askResourceToReceive(1);
			
			// on donne au joueur la ressource qu'il a souhait�
			p.receiveResources(askedResource, 1);		

			// on compte et on retire toutes les ressources de ce type que les autres joueurs ont
			int cmpt = 0;
			int cmpt2 = 0;
			for (int i = 0; i < game.getPlayers().length; i++) {
				cmpt2 = cmpt2 + game.getPlayers()[i].getResources().get(askedResource);
				cmpt = game.getPlayers()[i].getResources().get(askedResource);
				game.getPlayers()[i].loseResources(askedResource, cmpt);
			}
			
			// on donne le nombre compt� au joueur
			p.receiveResources(askedResource, cmpt2);
		}

	}

	@Override
	public String toString() {
		if (this.name.equals("construction")) {
			return "Carte Progr�s - Construction de Routes";
		}
		
		if (this.name.equals("invention")) {
			return "Carte Progr�s - Invention";
		}
		
		return "Carte Progr�s - Monopole";
	}
}


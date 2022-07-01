package catan.model.card;

import catan.model.Game;
import catan.model.other.CardName;
import catan.model.player.Player;
import catan.model.other.Resource;

public class ProgressCard extends Card {
	
	public ProgressCard(CardName name) {
		super(name);
	}

	public void play(Game game, Player p) {
		if (name == CardName.ROADBUILDING) {
			
			// on construit 2 routes
			//p.build(Construction.ROAD, false);
			//p.build(Construction.ROAD, false);
		}
		
		if (name == CardName.YEAROFPLENTY) {
			
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
	
		if (name == CardName.MONOPOLY) {

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
		if (name == CardName.ROADBUILDING) return "Progress Card - Road Building";
		if (name == CardName.YEAROFPLENTY) return "Progress Card - Year Of Plenty";
		return "Progress Card - Monopoly";
	}
}


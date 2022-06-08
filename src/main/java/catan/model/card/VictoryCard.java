package catan.model.card;

import catan.model.Game;
import catan.model.player.Human;

public class VictoryCard extends Card {

	public VictoryCard(String name) {
		super(2, name);
	}

	@Override
	public void jouer(Game game, Human p) {}

	@Override
	public String toString() {
		if (this.name.equals("bibliotheque")) {
			return "CarteVictoire - Biblioth�que";
		}
		
		if (this.name.equals("marche")) {
			return "CarteVictoire - Place du march�";
		}
		
		if (this.name.equals("parlement")){
			return "CarteVictoire - Parlement";
		}
		
		if (this.name.equals("eglise")) {
			return "CarteVictoire - �glise";
		}
		
		return "CarteVictoire - Universit�";
	}
}


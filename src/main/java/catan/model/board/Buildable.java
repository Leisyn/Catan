package catan.model.board;

import catan.model.Game;
import catan.model.player.Player;

public interface Buildable {
	public enum Construction{NOTHING, ROAD, SETTLEMENT, CITY};
	
	public void build(Game game, Player p, Construction c);
}

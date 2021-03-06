package catan.model.board;

import catan.model.Game;
import catan.model.other.Construction;
import catan.model.player.Player;

public interface Buildable {
	public void build(Game game, Player p, Construction c);
}

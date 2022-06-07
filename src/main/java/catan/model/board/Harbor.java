package catan.model.board;

public class Harbor extends Tile {
	public enum HarborType{GENERIC, BRICK, LUMBER, ORE, GRAIN, WOOL};
	
	public final HarborType harborType;
	
	public Harbor(HarborType type) {
		super(TileType.HARBOR, false);
		harborType = type;
	}
	
	@Override
	public void printDetail() {
		switch (harborType) {
			case BRICK: System.out.print("  BRI  "); return;
			case LUMBER: System.out.print("  LUM  "); return;
			case ORE: System.out.print("  ORE  "); return;
			case GRAIN: System.out.print("  GRA  "); return;
			case WOOL: System.out.print("  WOO  "); return;
			default: System.out.print("  3-1  ");
		}
	}
}

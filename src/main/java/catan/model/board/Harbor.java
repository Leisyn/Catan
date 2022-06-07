package catan.model.board;

public class Harbor extends Tile {
	public final int typeHarbor;  // 0 : 3-1 / 1 : laine / 2 : argile / 3 : bois / 4 : ble / 5 : minerai
	
	public Harbor(int t) {
		super(TileType.HARBOR, false);
		typeHarbor = t;
	}
	
	@Override
	public void printDetail() {
		switch (typeHarbor) {
			case 1: System.out.print("  LAI  "); return;
			case 2: System.out.print("  ARG  "); return;
			case 3: System.out.print("  BOI  "); return;
			case 4: System.out.print("  BLE  "); return;
			case 5: System.out.print("  MIN  "); return;
			default: System.out.print("  3-1  ");
		}
	}
}

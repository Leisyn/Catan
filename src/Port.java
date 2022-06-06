
public class Port extends Tuile {
	public final int typePort;  // 0 : 3-1 / 1 : laine / 2 : argile / 3 : bois / 4 : ble / 5 : minerai
	
	public Port(int t) {
		super(1, false);
		typePort = t;
	}
	
	@Override
	public void afficheDetail() {
		switch (typePort) {
			case 1: System.out.print("  LAI  "); return;
			case 2: System.out.print("  ARG  "); return;
			case 3: System.out.print("  BOI  "); return;
			case 4: System.out.print("  BLE  "); return;
			case 5: System.out.print("  MIN  "); return;
			default: System.out.print("  3-1  ");
		}
	}
}

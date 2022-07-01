package catan.model.other;

public enum Resource {
	BRICK, LUMBER, ORE, GRAIN, WOOL;
	
	public static Resource toResource(String s) {
		s = s.toLowerCase();
		switch (s) {
			case "BRICK": return BRICK;
			case "LUMBER": return LUMBER;
			case "ORE": return ORE;
			case "GRAIN": return GRAIN;
			case "WOOL": return WOOL;
			default: return null;
		}
	}
}

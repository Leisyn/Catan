package catan.model.other;

public enum CardName {
	KNIGHT, ROADBUILDING, YEAROFPLENTY, MONOPOLY, MARKET, UNIVERSITY, GREATHALL, CHAPEL, LIBRARY;
	
	public static CardName toCardName(String s) {
		s = s.toUpperCase();
		switch (s) {
			case "KNIGHT": return KNIGHT;
			case "ROADBUILDING": return ROADBUILDING;
			case "YEAROFPLENTY": return YEAROFPLENTY;
			case "MONOPOLY": return MONOPOLY;
			case "MARKET": return MARKET;
			case "UNIVERSITY": return UNIVERSITY;
			case "GREATHALL": return GREATHALL;
			case "CHAPEL": return LIBRARY;
			default: return null;
		}
	}
}

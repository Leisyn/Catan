package catan.model.other;

public enum Action {
	THROWDICE, BUILDROAD, BUILDSETTLEMENT, BUILDCITY, PLAYCARD, BUYCARD, ENDTURN, RETURN;
	
	public static Action toAction(String s) {
		s = s.toUpperCase();
		switch (s) {
			case "THROWDICE": return THROWDICE;
			case "BUILDROAD": return BUILDROAD;
			case "BUILDSETTLEMENT": return BUILDSETTLEMENT;
			case "BUILDCITY": return BUILDCITY;
			case "PLAYCARD": return PLAYCARD;
			case "BUYCARD": return BUYCARD;
			case "ENDTURN": return ENDTURN;
			case "RETURN": return RETURN;
			default: return null;
		}
	}
}

package fr.univartois.sonargo.coverage;

public class LinePredicate {
    private static boolean funcFound = false;

    public static void init() {
	funcFound = false;
    }

    public static boolean filterLine(LineCoverage l) {
	String s = l.getLine().trim();
	if (s.matches("^func.*")) {
	    funcFound = true;
	    return false;
	}
	return funcFound && !s.isEmpty() && !"}".equals(s) && !s.matches("^type.*") && !s.matches("^func.*")
		&& !s.matches("^import.*") && !s.matches("^package.*");
    }
}

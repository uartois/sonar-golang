package fr.univartois.sonargo.coverage;

public class LinePredicate {
    private static boolean funcFound = false;
    private static boolean commentFound = false;
    private static boolean typeFound = false;

    private LinePredicate() {
	throw new IllegalStateException("Utility class");
    }

    public static void init() {
	funcFound = false;
	commentFound = false;
	typeFound = false;
    }

    public static boolean filterLine(LineCoverage l) {
	String s = l.getLine().trim();
	if (s.matches("^func.*")) {
	    funcFound = true;
	    return false;
	}
	if (s.matches("^//.*")) {
	    return false;
	}

	if (s.matches("^/\\*.*")) {
	    commentFound = true;
	    return false;
	}

	if (s.matches("^\\*/.*")) {
	    commentFound = false;
	    return false;
	}

	if (s.matches("^type.*") || s.matches("^interface.*") || s.matches("^struct.*")) {
	    typeFound = true;
	    return false;
	}

	if (s.matches("}.*") && typeFound) {
	    typeFound = true;
	    return false;
	}

	return funcFound && !commentFound && !typeFound && !s.isEmpty() && !"}".equals(s) && !s.matches("^type.*")
		&& !s.matches("^func.*") && !s.matches("^import.*") && !s.matches("^package.*");
    }
}

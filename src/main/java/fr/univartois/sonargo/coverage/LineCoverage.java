package fr.univartois.sonargo.coverage;

public class LineCoverage {
	private int lineNumber;
	private int hits;

	public LineCoverage(int lineNumber, int hits) {
		super();
		this.lineNumber = lineNumber;
		this.hits = hits;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public int getHits() {
		return hits;
	}

	@Override
	public String toString() {
		return "LineCoverage [lineNumber=" + lineNumber + ", hits=" + hits + "]";
	}

}

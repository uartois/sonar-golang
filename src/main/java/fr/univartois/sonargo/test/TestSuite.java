package fr.univartois.sonargo.test;

public class TestSuite {
	private int nbTotalTest = 0;
	private int nbFailureTest = 0;
	private int skipped = 0;
	private String file;
	private Double time;

	public TestSuite(int nbTotalTest, int nbFailureTest, int skipped, String file, double time) {
		super();
		this.nbTotalTest = nbTotalTest;
		this.nbFailureTest = nbFailureTest;
		this.skipped = skipped;
		this.file = file;
		this.time = time;
	}

	public int getNbTotalTest() {
		return nbTotalTest;
	}

	public void setNbTotalTest(int nbTotalTest) {
		this.nbTotalTest = nbTotalTest;
	}

	public int getNbFailureTest() {
		return nbFailureTest;
	}

	public void setNbFailureTest(int nbFailureTest) {
		this.nbFailureTest = nbFailureTest;
	}

	public int getSkipped() {
		return skipped;
	}

	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public long getTime() {
		return (long) (time * 1000);
	}

	public void setTime(Double time) {
		this.time = time;
	}

}

package fr.univartois.sonargo.gotest;

import java.util.ArrayList;

public class GoTestFile {
    private ArrayList<GoTestCase> list;
    private String file;

    public GoTestFile() {
	super();
	list = new ArrayList<>();
    }

    public int getNbTotalTest() {
	return list.size();
    }

    public void addTestCase(GoTestCase t) {
	list.add(t);
    }

    public int getNbFailureTest() {
	int nbFailure = 0;
	for (GoTestCase tc : list) {
	    if (tc.isFail())
		nbFailure++;
	}
	return nbFailure;
    }

    public int getSkipped() {
	int nbSkipped = 0;
	for (GoTestCase tc : list) {
	    if (tc.isSkipped())
		nbSkipped++;
	}
	return nbSkipped;
    }

    public String getFile() {
	return file;
    }

    public void setFile(String file) {
	this.file = file;
    }

    public long getTime() {
	double time = 0.0;
	for (GoTestCase tc : list) {
	    time += tc.getTime();
	}
	return (long) (time * 1000);
    }

}

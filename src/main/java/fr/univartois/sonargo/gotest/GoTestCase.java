/*******************************************************************************
 * Copyright 2017 - Université d'Artois
 *
 * This file is part of SonarQube Golang plugin (sonar-golang).
 *
 * Sonar-golang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sonar-golang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar-golang.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *            Thibault Falque (thibault_falque@ens.univ-artois.fr)
 *******************************************************************************/
package fr.univartois.sonargo.gotest;

public class GoTestCase {
    private int nbTotalTest = 0;
    private int nbFailureTest = 0;
    private int skipped = 0;
    private String file;
    private Double time;

    public GoTestCase(int nbTotalTest, int nbFailureTest, int skipped, String file, double time) {
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

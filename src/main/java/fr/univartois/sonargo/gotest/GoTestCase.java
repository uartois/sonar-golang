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
    private boolean fail;
    private boolean skipped;
    private Double time;

    public GoTestCase(boolean fail, boolean skipped, Double time) {
	super();
	this.fail = fail;
	this.skipped = skipped;
	this.time = time;
    }

    public boolean isFail() {
	return fail;
    }

    public void setFail(boolean fail) {
	this.fail = fail;
    }

    public boolean isSkipped() {
	return skipped;
    }

    public void setSkipped(boolean skipped) {
	this.skipped = skipped;
    }

    public Double getTime() {
	return time;
    }

    public void setTime(Double time) {
	this.time = time;
    }

}

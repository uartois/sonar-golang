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
package fr.univartois.sonargo.core.language;

import org.apache.commons.lang3.StringUtils;
import org.sonar.api.resources.AbstractLanguage;

/**
 * This class defines the Golang language
 * 
 * @author thibault
 *
 */
public class GoLanguage extends AbstractLanguage {

    public static final String NAME = "GO";
    public static final String KEY = "go";

    private static final String[] DEFAULT_FILE_SUFFIXES = { "go" };

    /**
     * Create the GoLangage
     */
    public GoLanguage() {
	super(KEY, NAME);
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public String[] getFileSuffixes() {
	return DEFAULT_FILE_SUFFIXES;
    }

    /**
     * Allows to know if the given file name has a valid suffix
     * 
     * @param filename
     *            String representing the file name
     * @return boolean <code>true</code> if the file name's suffix is known,
     *         <code>false</code> any other way
     */
    public boolean hasValidSuffixes(String filename) {
	for (String str : DEFAULT_FILE_SUFFIXES) {
	    if (StringUtils.lowerCase(filename).endsWith("." + str)) {
		return true;
	    }
	}
	return false;
    }

}

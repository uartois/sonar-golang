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
package fr.univartois.sonargo.core.rules;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * This class load the file key.properties where there is a map that can
 * correspond lint a internal key with a Pattern for match with the message of
 * the report
 * 
 * @author thibault
 *
 */
public class GoKeyRule {
	public static final String PATH_FILE = "/key.properties";
	private static final Logger LOGGER = Loggers.get(GoKeyRule.class);
	private static Properties prop;

	private GoKeyRule() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Allow to load the key.properties file where there is a map that can
	 * correspond lint a internal key with a Pattern for match with the message
	 * of the report
	 */
	public static synchronized void init() {
		if (prop == null) {
			prop = new Properties();
			try {
				LOGGER.info("Load " + PATH_FILE);

				InputStream input = GoKeyRule.class.getResourceAsStream(PATH_FILE);

				if (input == null) {
					throw new FileNotFoundException(PATH_FILE);
				}

				prop.load(input);

				LOGGER.info("loaded " + prop.keySet().size());
			} catch (IOException e) {
				LOGGER.error("Unable to load the config file", e);
			}
		}
	}

	public static Properties getProp() {
		return prop;
	}

	/**
	 * Allow to get the corresponding key of the message of the error
	 * 
	 * @param error
	 * @return
	 */
	public static String getKeyFromError(GoError error) {
		Pattern pattern;
		Matcher matcher;

		for (Entry<Object, Object> e : prop.entrySet()) {

			pattern = Pattern.compile((String) e.getValue());
			matcher = pattern.matcher(error.getMessage());
			if (!matcher.matches())
				continue;
			return (String) e.getKey();
		}

		LOGGER.warn("This description \"" + error.getMessage() + "\" is not usable");
		return null;

	}
}

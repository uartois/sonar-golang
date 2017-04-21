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
package fr.univartois.sonargo;

import static fr.univartois.sonargo.GoLintRulesDefinition.REPO_KEY;
import static fr.univartois.sonargo.GoLintRulesDefinition.REPO_NAME;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * The class define all rules that will detect by the sensor, it's not the same
 * of {@link GoLintRulesDefinition}
 * 
 * @author thibault
 *
 */
public final class GoQualityProfile extends ProfileDefinition {
	private static final Logger LOGGER = Loggers.get(GoQualityProfile.class);
	public static final String PROFILE_PATH = "/profile.properties";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RulesProfile createProfile(ValidationMessages validation) {

		LOGGER.info("Golint Quality profile");
		RulesProfile profile = RulesProfile.create("Golint Rules", GoLanguage.KEY);
		profile.setDefaultProfile(Boolean.TRUE);

		Properties prop = new Properties();

		try {
			prop.load(GoQualityProfile.class.getResourceAsStream(GoQualityProfile.PROFILE_PATH));

			for (Entry<Object, Object> e : prop.entrySet()) {
				if (Boolean.TRUE.equals(Boolean.valueOf((String) e.getValue()))) {
					profile.activateRule(Rule.create(REPO_KEY, (String) e.getKey(), REPO_NAME), null);
				}
			}

		} catch (IOException e) {
			LOGGER.error((new StringBuilder()).append("Unable to load ").append(PROFILE_PATH).toString(), e);
		}

		LOGGER.info((new StringBuilder()).append("Profil generate: ").append(profile.getActiveRules()).toString());

		return profile;
	}

}

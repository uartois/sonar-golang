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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Predicate;

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.utils.ValidationMessages;

import fr.univartois.sonargo.core.language.GoQualityProfile;

public class GoQualityProfileTest {
    private GoQualityProfile goprofile = new GoQualityProfile();

    @Test
    public void testLoadingQualityProfile() {

	RulesProfile rule = goprofile.createProfile(ValidationMessages.create());
	assertFalse(rule.getActiveRules().isEmpty());
	Properties prop = new Properties();

	try {
	    prop.load(GoQualityProfileTest.class.getResourceAsStream(GoQualityProfile.PROFILE_PATH));
	    Predicate<? super Object> predicate = s -> Boolean.TRUE.equals(Boolean.parseBoolean((String) s));
	    assertEquals(prop.values().stream().filter(predicate).count(), rule.getActiveRules().size());

	    for (ActiveRule r : rule.getActiveRules()) {
		assertTrue(r.isEnabled());
		assertTrue(r.getRule().isEnabled());
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}

	assertTrue(rule.getDefaultProfile());
    }

}

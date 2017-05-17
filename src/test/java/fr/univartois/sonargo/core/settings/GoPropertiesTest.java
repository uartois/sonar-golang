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
package fr.univartois.sonargo.core.settings;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.sonar.api.config.PropertyDefinition;

import fr.univartois.sonargo.core.settings.GoProperties;

public class GoPropertiesTest {

    @Test
    public void test() {
	List<PropertyDefinition> l = GoProperties.getProperties();
	assertEquals(3, l.size());
	assertEquals(GoProperties.REPORT_PATH_DEFAULT, l.get(0).defaultValue());
	assertEquals(GoProperties.COVERAGE_REPORT_PATH_DEFAULT, l.get(1).defaultValue());
	assertEquals(GoProperties.JUNIT_REPORT_PATH_DEFAULT, l.get(2).defaultValue());

    }

}

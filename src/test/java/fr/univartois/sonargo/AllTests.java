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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.univartois.sonargo.coverage.CoverageParser;
import fr.univartois.sonargo.coverage.CoverageSensor;
import fr.univartois.sonargo.language.GoLanguageTest;
import fr.univartois.sonargo.language.GoQualityProfileTest;
import fr.univartois.sonargo.rules.EvalTest;
import fr.univartois.sonargo.rules.GoErrorTest;
import fr.univartois.sonargo.rules.GoLintIssueLoaderSendorTest;
import fr.univartois.sonargo.rules.GoLintRulesDefinitionTest;
import fr.univartois.sonargo.settings.GoPropertiesTest;

@RunWith(Suite.class)
@SuiteClasses({ EvalTest.class, GoErrorTest.class, GoLanguageTest.class, GoQualityProfileTest.class,
		GoLintIssueLoaderSendorTest.class, GoLintRulesDefinitionTest.class, GoPluginTest.class, GoPropertiesTest.class,
		CoverageParser.class, CoverageSensor.class })
public class AllTests {
}

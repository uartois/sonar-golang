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

import fr.univartois.sonargo.core.language.GoLanguageTest;
import fr.univartois.sonargo.core.language.GoLexerTest;
import fr.univartois.sonargo.core.language.GoQualityProfileTest;
import fr.univartois.sonargo.core.rules.EvalTest;
import fr.univartois.sonargo.core.rules.GoErrorTest;
import fr.univartois.sonargo.core.rules.GoLintIssueLoaderSendorTest;
import fr.univartois.sonargo.core.rules.GoLintRulesDefinitionTest;
import fr.univartois.sonargo.core.settings.GoPropertiesTest;
import fr.univartois.sonargo.coverage.CoverageSensorTest;
import fr.univartois.sonargo.gotest.FunctionFinderTest;

@RunWith(Suite.class)
@SuiteClasses({ EvalTest.class, GoErrorTest.class, GoLanguageTest.class, GoQualityProfileTest.class,
		GoLintIssueLoaderSendorTest.class, GoLintRulesDefinitionTest.class, GoPluginTest.class, GoPropertiesTest.class,
		CoverageSensorTest.class, GoLexerTest.class, FunctionFinderTest.class })
public class AllTests {
}

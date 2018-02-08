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

import static java.util.Arrays.asList;

import java.util.List;

import org.sonar.api.CoreProperties;
import org.sonar.api.config.PropertyDefinition;

public class GoProperties {
    public static final String REPORT_PATH_KEY = "sonar.golint.reportPath";
    public static final String REPORT_PATH_DEFAULT = "report.xml";

    public static final String COVERAGE_REPORT_PATH_KEY = "sonar.coverage.reportPath";
    public static final String COVERAGE_REPORT_PATH_DEFAULT = "coverage.xml";

    public static final String JUNIT_REPORT_PATH_KEY = "sonar.test.reportPath";
    public static final String JUNIT_REPORT_PATH_DEFAULT = "test.xml";

    public static final String DTD_VERIFICATION_KEY = "sonar.coverage.dtdVerification";
    public static final String DTD_VERIFICATION_DEFAULT = "true";

    public static final String HIGHLIGHTING_KEY = "sonar.highlighting";
    public static final String HIGHLIGHTING_DEFAULT = "true";

    private GoProperties() {

    }

    public static List<PropertyDefinition> getProperties() {
	return asList(
		PropertyDefinition.builder(REPORT_PATH_KEY).defaultValue(REPORT_PATH_DEFAULT).category("Go")
			.name("Report path of Golint").description("relative path for golint report").build(),
		PropertyDefinition.builder(COVERAGE_REPORT_PATH_KEY).defaultValue(COVERAGE_REPORT_PATH_DEFAULT)
			.category("Go").name("Report path of coverage report")
			.description("relative path for coverage report").build(),
		PropertyDefinition.builder(JUNIT_REPORT_PATH_KEY).defaultValue(JUNIT_REPORT_PATH_DEFAULT).category("Go")
			.name("Report path of JUnit report").description("relative path for JUnit report").build(),
		PropertyDefinition.builder(DTD_VERIFICATION_KEY).defaultValue(DTD_VERIFICATION_DEFAULT).category("Go")
			.name("Boolean for DTD verification")
			.description("false if you want disabled the DTD verification for coverage file").build(),
		PropertyDefinition.builder(HIGHLIGHTING_KEY).defaultValue(HIGHLIGHTING_DEFAULT).category("Go")
			.name("Boolean for highlighting verification")
			.description("false if you want disabled the highlighting").build(),
		PropertyDefinition.builder(CoreProperties.PROJECT_INCLUSIONS_PROPERTY).defaultValue("**/**.go").build(),
		PropertyDefinition.builder(CoreProperties.PROJECT_TEST_INCLUSIONS_PROPERTY)
			.defaultValue("**/**_test.go").build());

    }
}

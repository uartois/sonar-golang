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

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.xml.sax.SAXException;

import fr.univartois.sonargo.core.language.GoLanguage;
import fr.univartois.sonargo.core.settings.GoProperties;

public class GoTestSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(GoTestSensor.class);

    @Override
    public void describe(SensorDescriptor descriptor) {
	descriptor.onlyOnLanguage(GoLanguage.KEY).name("Go test JUnit loader sensor");

    }

    @Override
    public void execute(SensorContext context) {
	String reportPath = context.settings().getString(GoProperties.JUNIT_REPORT_PATH_KEY);

	if (reportPath == null || !(new File(reportPath)).exists()) {
	    LOGGER.info("no junit report");
	    return;
	}
	FunctionFinder ff;
	try {
	    ff = new FunctionFinder(context);

	    Map<String, String> nameFunction = ff.searchFunction();

	    LOGGER.debug(nameFunction.toString());

	    GoJunitParser junitParser = new GoJunitParser(nameFunction);

	    junitParser.parse(reportPath);

	    GoTestReportSaver.save(context, junitParser.getListTestSuite());

	} catch (ParserConfigurationException | SAXException | IOException e) {
	    LOGGER.error("Parse exception ", e);
	}
    }

}

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

import org.sonar.api.Plugin;

import fr.univartois.sonargo.core.language.GoLanguage;
import fr.univartois.sonargo.core.language.GoQualityProfile;
import fr.univartois.sonargo.core.metrics.GoMetricSensor;
import fr.univartois.sonargo.core.rules.GoLintIssueLoaderSensor;
import fr.univartois.sonargo.core.rules.GoLintRulesDefinition;
import fr.univartois.sonargo.core.settings.GoProperties;
import fr.univartois.sonargo.coverage.CoverageSensor;
import fr.univartois.sonargo.gotest.GoTestSensor;
import fr.univartois.sonargo.highlighter.HighlighterSensor;

/**
 * This class is the entry point of the plugin
 * 
 * 
 * 
 * @author thibault
 *
 */
public class SonarGoPlugin implements Plugin {

	@Override
	public void define(Context context) {
		context.addExtensions(GoProperties.getProperties());
		context.addExtensions(GoLanguage.class, GoQualityProfile.class);
		context.addExtensions(GoLintRulesDefinition.class, GoLintIssueLoaderSensor.class);
		context.addExtension(CoverageSensor.class);
		context.addExtension(GoTestSensor.class);
		context.addExtension(HighlighterSensor.class);
		context.addExtension(GoMetricSensor.class);

	}

}

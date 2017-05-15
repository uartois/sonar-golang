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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.core.language.GoLanguage;

/**
 * This class read the "/rules/golint-rules.xml" file and create all the rule
 * than can detect by the Sensor
 * 
 * @author thibault
 *
 */
public class GoLintRulesDefinition implements RulesDefinition {
	private static final String PATH_TO_RULES_XML = "/rules/golint-rules.xml";
	private static final Logger LOGGER = Loggers.get(GoLintRulesDefinition.class);
	protected static final String KEY = "go";
	protected static final String NAME = "Go";

	public static final String REPO_KEY = GoLanguage.KEY + "-" + KEY;
	public static final String REPO_NAME = GoLanguage.KEY + "-" + NAME;

	protected String rulesDefinitionFilePath() {
		return PATH_TO_RULES_XML;
	}

	private void defineRulesForLanguage(Context context, String repositoryKey, String repositoryName,
			String languageKey) {
		NewRepository repository = context.createRepository(repositoryKey, languageKey).setName(repositoryName);

		LOGGER.info("Repository " + repositoryName + " created with the key " + repositoryKey + " " + repository);

		InputStream rulesXml = this.getClass().getResourceAsStream(rulesDefinitionFilePath());
		if (rulesXml != null) {
			RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
			rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
		} else {
			LOGGER.warn("The file " + PATH_TO_RULES_XML + " is not loading ");
		}

		repository.done();

		LOGGER.info("Repository " + repositoryName + " done: " + repository.rules() + " " + repository.rules().size());

	}

	@Override
	public void define(Context context) {
		defineRulesForLanguage(context, REPO_KEY, REPO_NAME, GoLanguage.KEY);
	}
}

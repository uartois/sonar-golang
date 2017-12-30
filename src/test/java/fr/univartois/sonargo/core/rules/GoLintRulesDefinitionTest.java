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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;

import fr.univartois.sonargo.core.language.GoLanguage;

public class GoLintRulesDefinitionTest {
	@Test
	public void testForVersion6() {
		GoLintRulesDefinition definition = new GoLintRulesDefinition();
		RulesDefinition.Context context = new RulesDefinition.Context();
		definition.define(context);
		RulesDefinition.Repository repository = context.repository(GoLintRulesDefinition.REPO_KEY);

		assertEquals(GoLintRulesDefinition.REPO_NAME, repository.name());
		assertEquals(GoLanguage.KEY, repository.language());
		GoKeyRule.init();
		assertEquals(GoKeyRule.getProp().size(), repository.rules().size());

		for (Object key : GoKeyRule.getProp().keySet()) {
			assertNotNull(repository.rule((String) key));
		}
	}
}

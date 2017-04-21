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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.Version;

public class GoLintRulesDefinitionTest {
	private Version sqVersion60 = Version.create(6, 0);
	private Version sqVersion56 = Version.create(5, 6);

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

		// assertThat(repository.rules()).hasSize(CheckList.getChecks().size());
		//
		// RulesDefinition.Rule unusedLabelRule = repository.rule("S1065");
		// assertThat(unusedLabelRule).isNotNull();
		// assertThat(unusedLabelRule.type()).isEqualTo(RuleType.CODE_SMELL);
		// assertThat(unusedLabelRule.internalKey()).isNull();
		// assertThat(unusedLabelRule.name()).isEqualTo("Unused labels should be
		// removed");
		// assertThat(repository.rule("S2095").type()).isEqualTo(RuleType.BUG);
		// assertThat(repository.rule("S2095").activatedByDefault())
		// .isEqualTo(version.isGreaterThanOrEqual(Version.create(6, 0)));
		// RulesDefinition.Rule magicNumber = repository.rule("S109");
		// assertThat(magicNumber.params()).isNotEmpty();
		// assertThat(magicNumber.activatedByDefault()).isFalse();
		//
		// // check if a rule using a legacy key is also enabled
		// RulesDefinition.Rule unusedPrivateMethodRule =
		// repository.rule("UnusedPrivateMethod");
		// assertThat(unusedPrivateMethodRule.activatedByDefault())
		// .isEqualTo(version.isGreaterThanOrEqual(Version.create(6, 0)));
		//
		// // Calling definition multiple time should not lead to failure:
		// thanks
		// // C# plugin !
		// definition.define(new RulesDefinition.Context());
	}
}

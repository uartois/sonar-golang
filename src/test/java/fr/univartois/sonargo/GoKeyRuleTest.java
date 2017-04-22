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
/**
 * 
 */
package fr.univartois.sonargo;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * @author thibault
 *
 */
@RunWith(Parameterized.class)
public class GoKeyRuleTest {
	private GoLintRulesDefinition rulesDefinition;
	private RulesDefinition.Repository repository;

	private GoError error;

	private String expected;

	public GoKeyRuleTest(GoError error, String expected) {
		super();
		this.error = error;
		this.expected = expected;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		rulesDefinition = new GoLintRulesDefinition();
		RulesDefinition.Context context = new RulesDefinition.Context();
		rulesDefinition.define(context);
		repository = context.repository(GoLintRulesDefinition.REPO_KEY);
		GoKeyRule.init();
	}

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ new GoError(10, "exported method ImageService.ApplyFilter should have comment or be unexported",
						"warning", "serverImage.go"), "ExportedHaveComment" },
				{ new GoError(10, "not found message", "warning", "test.go"), null } });
	}

	@Test
	public void testGetKey() {
		assertEquals(expected, GoKeyRule.getKeyFromError(error));
	}

}

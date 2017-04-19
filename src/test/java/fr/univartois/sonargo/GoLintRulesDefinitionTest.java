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

import static org.junit.Assert.*;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition.Context;

public class GoLintRulesDefinitionTest {
	private GoLintRulesDefinition rulesDefinition=new GoLintRulesDefinition();
	private Context context=new Context();

	@Test
	public void testLoadintOfRulesDefinition() {
		rulesDefinition.define(context);
		assertFalse(context.repositories().isEmpty());
		
	}

}

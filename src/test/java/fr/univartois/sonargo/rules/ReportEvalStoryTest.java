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
package fr.univartois.sonargo.rules;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class ReportEvalStoryTest {
	private String eval;

	@Given("Given a Key finder")
	public void newInterpreter() {
		GoKeyRule.init();
	}

	@When("the expression entered is $expression")
	public void eval(String expression) {
		eval = GoKeyRule.getKeyFromError(new GoError(10, expression, null, null));

	}

	@Then("the result should be $expectedValue")
	public void checkValue(String expectedValue) {
		assertThat(String.valueOf(eval), equalTo(expectedValue));
		System.out.println(String.valueOf(eval));
	}

}

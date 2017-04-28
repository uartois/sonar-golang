package fr.univartois.sonargo.rules;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import fr.univartois.sonargo.rules.GoError;
import fr.univartois.sonargo.rules.GoKeyRule;

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

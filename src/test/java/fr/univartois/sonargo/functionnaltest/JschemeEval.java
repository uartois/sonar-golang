package fr.univartois.sonargo.functionnaltest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import jscheme.JScheme;
import migl.lisp.Lisp;
import migl.lisp.LispFactory;

public class JschemeEval {

    private JScheme js;
    private Lisp lisp;

    private Object eval;
    private Throwable ex;
    private Object expected;

    @Given("jscheme and a lisp interpreter")
    public void newInterpreter() {
        lisp = LispFactory.makeIntepreter();
        js = new JScheme();
    }

    @When("the expression evaluated is $expression")
    public void eval(String expression) {
        try {
            ex = null;
            eval = null;
            expected = null;
            eval = lisp.eval(expression);
            expected = js.eval(expression);
        } catch (Throwable e) {
            ex = e;
        }
    }

    @Then("the results should be identical")
    public void checkResultsAreIdentifcal() {
        assertThat("No own eval?", eval, is(notNullValue()));
        assertThat("No jscheme eval?", expected, is(notNullValue()));
        assertThat(eval.toString(), is(equalTo(expected.toString())));
    }

    @Then("the results should be different")
    public void checkResultsAreDifferent() {
        assertThat("No own eval?", eval, is(notNullValue()));
        assertThat("No jscheme eval?", expected, is(notNullValue()));
        assertThat(eval.toString(), is(not(equalTo(expected.toString()))));
    }

    @Then("the error message should be identical")
    public void checkError() {
        assertThat("No exception launched?", ex, is(notNullValue()));
        assertThat(ex.getMessage(), is(equalTo(expected.toString())));
    }
}

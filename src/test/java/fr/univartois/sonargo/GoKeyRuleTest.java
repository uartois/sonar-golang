/**
 * 
 */
package fr.univartois.sonargo;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition.Context;

import fr.univartois.sonargo.GoLintRulesDefinition;

/**
 * @author thibault
 *
 */
public class GoKeyRuleTest {
	private GoLintRulesDefinition rulesDefinition;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		rulesDefinition=new GoLintRulesDefinition();
		
	}

	@Test
	public void test() {
		rulesDefinition.define(new Context());
		
	}

}

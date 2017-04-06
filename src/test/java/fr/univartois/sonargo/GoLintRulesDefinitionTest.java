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

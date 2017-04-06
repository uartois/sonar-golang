package fr.univartois.sonargo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;

public class GoQualityProfileTest {
	private GoQualityProfile goprofile=new GoQualityProfile();
	@Test
	public void test() {
		
		RulesProfile rule=goprofile.createProfile(ValidationMessages.create());
		assertFalse(rule.getActiveRules().isEmpty());
		assertEquals(4,rule.getActiveRules().size());
		assertEquals(4,rule.getActiveRulesByRepository(GoLintRulesDefinition.REPO_KEY).size());
		assertTrue(rule.getDefaultProfile());
	}

}

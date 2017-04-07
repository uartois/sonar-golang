package fr.univartois.sonargo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Predicate;

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.utils.ValidationMessages;

public class GoQualityProfileTest {
	private GoQualityProfile goprofile=new GoQualityProfile();
	@Test
	public void testLoadingQualityProfile() {
		
		RulesProfile rule=goprofile.createProfile(ValidationMessages.create());
		assertFalse(rule.getActiveRules().isEmpty());
		Properties prop=new Properties();

		try {
			prop.load(GoQualityProfileTest.class.getResourceAsStream(GoQualityProfile.PROFILE_PATH));
			Predicate<? super Object> predicate = s-> Boolean.TRUE.equals(Boolean.parseBoolean((String) s));
			assertEquals(prop.values().stream().filter(predicate).count(),rule.getActiveRules().size());
		
			for(ActiveRule r:rule.getActiveRules()){
				assertTrue(r.isEnabled());
				assertTrue(r.getRule().isEnabled());
				
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		assertTrue(rule.getDefaultProfile());
	}

}

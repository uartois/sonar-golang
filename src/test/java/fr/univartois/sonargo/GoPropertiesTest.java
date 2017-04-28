package fr.univartois.sonargo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.sonar.api.config.PropertyDefinition;

import fr.univartois.sonargo.settings.GoProperties;

public class GoPropertiesTest {

	@Test
	public void test() {
		List<PropertyDefinition> l = GoProperties.getProperties();
		assertEquals(3, l.size());
		assertEquals(GoProperties.REPORT_PATH_DEFAULT, l.get(0).defaultValue());
		assertEquals(GoProperties.COVERAGE_REPORT_PATH_DEFAULT, l.get(1).defaultValue());
		assertEquals(GoProperties.JUNIT_REPORT_PATH_DEFAULT, l.get(2).defaultValue());

	}

}

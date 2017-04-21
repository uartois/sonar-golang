package fr.univartois.sonargo;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.sonar.api.Plugin.Context;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

public class GoPluginTest {

	@Test
	public void test() {
		SonarGoPlugin goPlugin = new SonarGoPlugin();
		SensorContextTester contextSensor = SensorContextTester.create(new File("./test.go"));
		Context c = new Context(contextSensor.runtime());
		goPlugin.define(c);
		assertEquals(6, c.getExtensions().size());

	}

}

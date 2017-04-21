package fr.univartois.sonargo;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

public class GoLintIssueLoaderSendorTest {
	private GoLintIssueLoaderSensor sensor;
	private SensorContextTester context;

	@Before
	public void setUp() throws Exception {
		DefaultFileSystem fs = new DefaultFileSystem(new File("./test.go"));

		context = SensorContextTester.create(new File("./test.go"));
		context.settings().appendProperty(GoProperties.REPORT_PATH_KEY, "./" + GoProperties.REPORT_PATH_DEFAULT);
		sensor = new GoLintIssueLoaderSensor(context.settings(), fs);
	}

	@Test
	public void testSensor() {
		sensor.execute(context);
		// assertEquals(2, context.allIssues().size());
	}

}

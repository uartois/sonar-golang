package fr.univartois.sonargo;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;

public class GoLintIssueLoaderSendorTest {
	private DefaultFileSystem fileSystem;
	private GoLintIssueLoaderSensor sensor;

	@Before
	public void setUp() {
		fileSystem = new DefaultFileSystem((File) null);
		sensor = new GoLintIssueLoaderSensor(new Settings(), fileSystem);
	}

	@Test
	public void describe() {
		SensorDescriptor sensorDescriptor = mock(SensorDescriptor.class);
		assertNotNull(sensorDescriptor);
		assertNotNull(sensor);

	}

}

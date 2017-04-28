package fr.univartois.sonargo;

import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

public class AbstractSonarTest {
	protected DefaultFileSystem fileSystem;
	protected SensorContextTester testerContext;

	public AbstractSonarTest() {
		this(TestUtils.getDefaultFileSystem());
	}

	public AbstractSonarTest(DefaultFileSystem fileSystem) {
		testerContext = SensorContextTester.create(fileSystem.baseDir());
	}

}

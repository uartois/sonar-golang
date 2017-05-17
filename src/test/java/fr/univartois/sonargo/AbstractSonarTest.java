package fr.univartois.sonargo;

import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

import fr.univartois.sonargo.core.settings.GoProperties;

public class AbstractSonarTest {
    protected DefaultFileSystem fileSystem;
    protected SensorContextTester testerContext;

    public void init() {
	init(TestUtils.getDefaultFileSystem());
    }

    public void init(DefaultFileSystem fileSystem) {
	this.fileSystem = fileSystem;
	testerContext = SensorContextTester.create(fileSystem.baseDir());

	testerContext.settings().appendProperty(GoProperties.COVERAGE_REPORT_PATH_KEY,
		GoProperties.COVERAGE_REPORT_PATH_DEFAULT);
	testerContext.settings().appendProperty(GoProperties.REPORT_PATH_KEY, GoProperties.REPORT_PATH_DEFAULT);
	testerContext.settings().appendProperty(GoProperties.JUNIT_REPORT_PATH_KEY,
		GoProperties.JUNIT_REPORT_PATH_DEFAULT);

    }

}

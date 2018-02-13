package fr.univartois.sonargo;

import org.sonar.api.CoreProperties;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

import fr.univartois.sonargo.core.settings.GoProperties;

public abstract class AbstractSonarTest {
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

	testerContext.settings().appendProperty(CoreProperties.PROJECT_EXCLUSIONS_PROPERTY, "vendor/**,.git/**");
	testerContext.settings().appendProperty(CoreProperties.PROJECT_TEST_INCLUSIONS_PROPERTY, "**/**_test.go");
	testerContext.settings().appendProperty(CoreProperties.PROJECT_INCLUSIONS_PROPERTY, "**/**.go");
	testerContext.settings().appendProperty("sonar.sources", "./");
	testerContext.settings().appendProperty("sonar.tests", "./");

	testerContext.setFileSystem(fileSystem);

    }

}

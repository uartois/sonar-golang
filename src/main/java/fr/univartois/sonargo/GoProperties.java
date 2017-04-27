package fr.univartois.sonargo;

import static java.util.Arrays.asList;

import java.util.List;

import org.sonar.api.config.PropertyDefinition;

public class GoProperties {
	public static final String REPORT_PATH_KEY = "sonar.golint.reportPath";
	public static final String REPORT_PATH_DEFAULT = "report.xml";

	public static final String COVERAGE_REPORT_PATH_KEY = "sonar.coverage.reportPath";
	public static final String COVERAGE_REPORT_PATH_DEFAULT = "coverage.xml";

	public static final String JUNIT_REPORT_PATH_KEY = "sonar.test.reportPath";
	public static final String JUNIT_REPORT_PATH_DEFAULT = "test.xml";

	private GoProperties() {

	}

	public static List<PropertyDefinition> getProperties() {
		return asList(
				PropertyDefinition.builder(REPORT_PATH_KEY).defaultValue(REPORT_PATH_DEFAULT).category("Go")
						.name("Report path of Golint").description("relative path for golint report").build(),
				PropertyDefinition.builder(COVERAGE_REPORT_PATH_KEY).defaultValue(COVERAGE_REPORT_PATH_DEFAULT)
						.category("Go").name("Report path of coverage report")
						.description("relative path for coverage report").build());
	}
}

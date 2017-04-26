package fr.univartois.sonargo.coverage;

import java.io.File;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.GoLanguage;
import fr.univartois.sonargo.GoProperties;

public class CoverageSensor implements Sensor {

	private static final Logger LOGGER = Loggers.get(CoverageSensor.class);

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Go Coverage").onlyOnFileType(InputFile.Type.MAIN).onlyOnLanguage(GoLanguage.KEY);
	}

	@Override
	public void execute(SensorContext context) {
		String reportPath = context.settings().getString(GoProperties.COVERAGE_REPORT_PATH_KEY);

		if (reportPath != null) {
			File coverFile = getIOFile(context.fileSystem(), reportPath);
			CoverageReportParser parser = new CoverageReportParser(coverFile, context);
			if (coverFile.exists()) {
				LOGGER.info("Analyzing Coverage report: " + reportPath);
				parser.parseReport();
			} else {
				LOGGER.info("Coverage report not found: " + reportPath);
			}
		} else {
			LOGGER.info("No Coverage report provided (see '" + GoProperties.COVERAGE_REPORT_PATH_KEY + "' property)");
		}
	}

	/**
	 * Returns a java.io.File for the given path. If path is not absolute,
	 * returns a File with module base directory as parent path.
	 */
	private static File getIOFile(FileSystem fileSystem, String path) {
		File file = new File(path);
		if (!file.isAbsolute()) {
			file = new File(fileSystem.baseDir(), path);
		}

		return file;
	}

}

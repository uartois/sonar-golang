package fr.univartois.sonargo.coverage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.xml.sax.SAXException;

import fr.univartois.sonargo.language.GoLanguage;
import fr.univartois.sonargo.settings.GoProperties;

public class CoverageSensor implements Sensor {

	private static final Logger LOGGER = Loggers.get(CoverageSensor.class);

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Go Coverage").onlyOnFileType(InputFile.Type.MAIN).onlyOnLanguage(GoLanguage.KEY);
	}

	@Override
	public void execute(SensorContext context) {

		try (Stream<Path> paths = Files.walk(Paths.get(context.fileSystem().baseDir().getPath()))) {
			paths.filter(p -> !p.startsWith(".") || p.getFileName().startsWith(".")).forEach(filePath -> {
				if (Files.isDirectory(filePath)) {
					String reportPath = context.settings().getString(GoProperties.COVERAGE_REPORT_PATH_KEY);
					File f = new File(filePath + File.separator + reportPath);
					if (f.exists()) {
						LOGGER.info("Analyse for " + f.getPath());

						CoverageParser coverParser = new CoverageParser();
						try {
							coverParser.parse(f.getPath());

							CoverageRecorder.save(context, coverParser.getList(), coverParser.getFilepath());

						} catch (ParserConfigurationException | SAXException | IOException e) {
							LOGGER.error("Exception: ", e);
						}

					} else {
						LOGGER.info("no coverage file in package " + filePath);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("IO Exception " + context.fileSystem().baseDir().getPath());
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

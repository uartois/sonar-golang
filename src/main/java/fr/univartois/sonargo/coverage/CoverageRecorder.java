package fr.univartois.sonargo.coverage;

import java.io.File;
import java.util.List;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.GoLanguage;

public class CoverageRecorder {
	private static final Logger LOGGER = Loggers.get(CoverageRecorder.class);

	public static void save(SensorContext context, List<LineCoverage> lines, String filePath) {
		FileSystem fileSystem = context.fileSystem();
		FilePredicates predicates = fileSystem.predicates();
		String key = filePath.startsWith(File.separator) ? filePath : (File.separator + filePath);
		InputFile inputFile = fileSystem
				.inputFile(predicates.and(predicates.matchesPathPattern("file:**" + key.replace(File.separator, "/")),
						predicates.hasType(InputFile.Type.MAIN), predicates.hasLanguage(GoLanguage.KEY)));

		NewCoverage coverage = context.newCoverage().onFile(inputFile);

		for (LineCoverage line : lines) {
			coverage.lineHits(line.getLineNumber(), line.getHits());
			LOGGER.info(line.toString());
		}
		coverage.save();
	}
}

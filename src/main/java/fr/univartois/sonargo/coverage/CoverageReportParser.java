
package fr.univartois.sonargo.coverage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.stream.Stream;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.GoLanguage;

public class CoverageReportParser {

	private static final Logger LOGGER = Loggers.get(CoverageReportParser.class);

	private File coverFile;

	private SensorContext context;

	private InputFile inputFile;

	private NewCoverage coverage;

	public CoverageReportParser(File coverFile, final SensorContext context) {
		this.coverFile = coverFile;
		this.context = context;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(coverFile)))) {
			br.readLine();
			String firstLine = br.readLine();
			String[] split = firstLine.split(":");

			String fileName = split[0];
			FileSystem fileSystem = context.fileSystem();
			FilePredicates predicates = fileSystem.predicates();
			String key = fileName.startsWith(File.separator) ? fileName : (File.separator + fileName);
			inputFile = fileSystem.inputFile(
					predicates.and(predicates.matchesPathPattern("file:**" + key.replace(File.separator, "/")),
							predicates.hasType(InputFile.Type.MAIN), predicates.hasLanguage(GoLanguage.KEY)));

			coverage = this.context.newCoverage().onFile(inputFile).ofType(CoverageType.UNIT);
		} catch (IOException e) {
			LOGGER.error("IOException: " + coverFile.getPath(), e);
		}

	}

	/**
	 * Parse a Coverage file report and create measures accordingly
	 * 
	 * @throws IOException
	 */
	public void parseReport() {
		try (Stream<String> stream = Files.lines(coverFile.toPath())) {

			stream.forEach((line) -> {
				if (line.contains("mode"))
					return;
				parseLine(line);

			});
			coverage.save();

		} catch (IOException e) {
			LOGGER.error("IOException: " + coverFile.getPath(), e);
		}

	}

	private void parseLine(String line) {

		LOGGER.info(line);

		String[] splitTwoPoints = line.split(":");

		String numbers = splitTwoPoints[1];

		String[] splitByComma = numbers.split(",");

		String[] splitNumbers = splitByComma[0].split("\\.");
		int lineNumber1 = Integer.parseInt(splitNumbers[0]);

		String[] splitBySpace = splitByComma[1].split(" ");

		int statement = Integer.parseInt(splitBySpace[1]);

		splitNumbers = splitBySpace[0].split("\\.");
		int lineNumber2 = Integer.parseInt(splitNumbers[0]);
		int hits = Integer.parseInt(splitBySpace[splitBySpace.length - 1]);

		coverage.lineHits(lineNumber2, hits);
		// if (hits == 0) {
		/*
		 * for (int i = lineNumber1; i <= lineNumber2; i++) { LOGGER.info("" +
		 * i); coverage.lineHits(i, hits);
		 * 
		 * }
		 */

		LOGGER.info("" + lineNumber2);

	}

}

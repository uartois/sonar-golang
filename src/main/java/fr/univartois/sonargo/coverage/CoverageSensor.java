/*******************************************************************************
 * Copyright 2017 - Université d'Artois
 *
 * This file is part of SonarQube Golang plugin (sonar-golang).
 *
 * Sonar-golang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sonar-golang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar-golang.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *            Thibault Falque (thibault_falque@ens.univ-artois.fr)
 *******************************************************************************/
package fr.univartois.sonargo.coverage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.sonar.api.CoreProperties;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.core.language.GoLanguage;
import fr.univartois.sonargo.core.settings.GoProperties;

public class CoverageSensor implements Sensor {

	private static final Logger LOGGER = Loggers.get(CoverageSensor.class);

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Go Coverage").onlyOnFileType(InputFile.Type.MAIN).onlyOnLanguage(GoLanguage.KEY);
	}

	private List<String> getExcludedPath(SensorContext context) {

		String globalExcludedPath = context.settings().getString(CoreProperties.PROJECT_EXCLUSIONS_PROPERTY);
		List<String> listExcludedPath = Arrays.asList(globalExcludedPath.split(","));

		return listExcludedPath;
	}

	private boolean isAnExcludedPath(Path candidatePath, SensorContext context) {
		List<String> listExcludedPath = getExcludedPath(context);

		for (String s : listExcludedPath) {
			if (s.contains("*")) {
				PathMatcher pathMatcher = java.nio.file.FileSystems.getDefault().getPathMatcher("glob:" + s);
				if (pathMatcher.matches(candidatePath)) {
					return true;
				}
			}

			if ((candidatePath.isAbsolute() && candidatePath.endsWith(s)) || (candidatePath.getFileName().equals(s))) {
				return true;
			}
		}
		return false;

	}

	public Stream<Path> createStream(SensorContext context) throws IOException {
		final String fullPath = context.fileSystem().baseDir().getPath();

		return Files.walk(Paths.get(fullPath))
				.filter(p -> !p.getParent().toString().contains(".git") && !p.getParent().toString().contains(".sonar")
						&& !p.getParent().toString().contains(".scannerwork")
						&& !p.getFileName().toString().startsWith(".") && !isAnExcludedPath(p, context));

	}

	@Override
	public void execute(SensorContext context) {

		try (Stream<Path> paths = createStream(context)) {
			paths.forEach(filePath -> {

				LOGGER.debug("Path in stream" + filePath.toFile().getAbsolutePath());

				if (Files.isDirectory(filePath)) {
					final String reportPath = context.settings().getString(GoProperties.COVERAGE_REPORT_PATH_KEY);
					final File f = new File(filePath + File.separator + reportPath);
					if (f.exists()) {
						LOGGER.info("Analyse for " + f.getPath());

						final CoverageParser coverParser = new CoverageParser(context);

						try {
							coverParser.parse(f.getPath());
							save(context, coverParser.getCoveragePerFile());
						} catch (ParserConfigurationException | IOException e) {
							LOGGER.error("Exception: ", e);
						}

					} else {
						LOGGER.info("no coverage file in package " + f.getPath());
					}
				}
			});
		} catch (final IOException e) {
			LOGGER.error("IO Exception " + context.fileSystem().baseDir().getPath());
		}
	}

	public static void save(SensorContext context, Map<String, List<LineCoverage>> coveragePerFile) {
		for (Map.Entry<String, List<LineCoverage>> entry : coveragePerFile.entrySet()) {
			final String filePath = entry.getKey();
			final List<LineCoverage> lines = entry.getValue();
			final FileSystem fileSystem = context.fileSystem();
			final FilePredicates predicates = fileSystem.predicates();
			final InputFile inputFile = fileSystem.inputFile(predicates.hasPath(filePath));

			if (inputFile == null) {
				LOGGER.warn("unable to create InputFile object: " + filePath);
				return;
			}

			final NewCoverage coverage = context.newCoverage().onFile(inputFile);

			for (final LineCoverage line : lines) {
				try {
					coverage.lineHits(line.getLineNumber(), line.getHits());
				} catch (final Exception ex) {
					LOGGER.error(ex.getMessage() + line);
				}
			}
			coverage.ofType(CoverageType.UNIT);
			coverage.save();
		}
	}

}

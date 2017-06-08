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
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

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
import org.xml.sax.SAXException;

import fr.univartois.sonargo.core.language.GoLanguage;
import fr.univartois.sonargo.core.settings.GoProperties;

public class CoverageSensor implements Sensor {

    private static final Logger LOGGER = Loggers.get(CoverageSensor.class);

    @Override
    public void describe(SensorDescriptor descriptor) {
	descriptor.name("Go Coverage").onlyOnFileType(InputFile.Type.MAIN).onlyOnLanguage(GoLanguage.KEY);
    }

    public Stream<Path> createStream(SensorContext context) throws IOException {
	return Files.walk(Paths.get(context.fileSystem().baseDir().getPath()))
		.filter(p -> !p.getFileName().toString().startsWith("."));

    }

    @Override
    public void execute(SensorContext context) {

	try (Stream<Path> paths = createStream(context)) {
	    paths.forEach(filePath -> {
		if (Files.isDirectory(filePath)) {
		    final String reportPath = context.settings().getString(GoProperties.COVERAGE_REPORT_PATH_KEY);
		    final File f = new File(filePath + File.separator + reportPath);
		    if (f.exists()) {
			LOGGER.info("Analyse for " + f.getPath());

			final CoverageParser coverParser = new CoverageParser();
			try {
			    coverParser.parse(f.getPath());

			    save(context, coverParser.getList(), coverParser.getFilepath());

			} catch (ParserConfigurationException | SAXException | IOException e) {
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

    public static void save(SensorContext context, List<LineCoverage> lines, String filePath) {
	final FileSystem fileSystem = context.fileSystem();
	final FilePredicates predicates = fileSystem.predicates();
	final String key = filePath.startsWith(File.separator) ? filePath : File.separator + filePath;
	final InputFile inputFile = fileSystem
		.inputFile(predicates.and(predicates.matchesPathPattern("file:**" + key.replace(File.separator, "/")),
			predicates.hasType(InputFile.Type.MAIN), predicates.hasLanguage(GoLanguage.KEY)));

	if (inputFile == null) {
	    LOGGER.warn("unable to create InputFile object: " + filePath);
	    return;
	}

	final NewCoverage coverage = context.newCoverage().onFile(inputFile);

	for (final LineCoverage line : lines) {
	    coverage.lineHits(line.getLineNumber(), line.getHits());
	}
	coverage.ofType(CoverageType.UNIT);
	coverage.save();
    }

}

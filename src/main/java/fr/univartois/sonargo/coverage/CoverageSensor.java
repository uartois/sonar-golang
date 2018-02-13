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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.sonar.api.CoreProperties;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.core.ProjectExplorer;
import fr.univartois.sonargo.core.language.GoLanguage;
import fr.univartois.sonargo.core.settings.GoProperties;

public class CoverageSensor implements Sensor {

    private static final Logger LOGGER = Loggers.get(CoverageSensor.class);
    private Set<InputFile> inputFileWithCoverage = new HashSet<>();

    @Override
    public void describe(SensorDescriptor descriptor) {
	descriptor.name("Go Coverage").onlyOnFileType(InputFile.Type.MAIN).onlyOnLanguage(GoLanguage.KEY);
    }

    private List<String> getExcludedPath(SensorContext context) {

	String globalExcludedPath = context.settings().getString(CoreProperties.PROJECT_EXCLUSIONS_PROPERTY);

	if (globalExcludedPath == null) {
	    return new ArrayList<>();
	}

	return Arrays.asList(globalExcludedPath.split(","));
    }

    public boolean isAnExcludedPath(Path candidatePath, SensorContext context) {
	List<String> listExcludedPath = getExcludedPath(context);

	for (String s : listExcludedPath) {
	    Path pathAbsolute = candidatePath.toAbsolutePath();
	    Path pathBase = context.fileSystem().baseDir().toPath();
	    Path pathRelative = pathBase.relativize(pathAbsolute);
	    if (s.contains("*")) {
		PathMatcher pathMatcher = java.nio.file.FileSystems.getDefault().getPathMatcher("glob:" + s);
		if (pathMatcher.matches(pathRelative)) {
		    return true;
		}
	    }

	    if ((candidatePath.isAbsolute() && candidatePath.endsWith(s))
		    || (candidatePath.getFileName().toFile().getName().equals(s))) {
		return true;
	    }
	}
	return false;

    }

    public Stream<Path> createStream(SensorContext context) throws IOException {
	final String fullPath = context.fileSystem().baseDir().getPath();
	LOGGER.info(fullPath);
	return Files.walk(Paths.get(fullPath))
		.filter(p -> !p.getParent().toString().equals(".git") && !p.getParent().toString().contains(".sonar")
			&& !p.getParent().toString().contains(".scannerwork")
			&& !p.getFileName().toString().startsWith(".") && !isAnExcludedPath(p, context));

    }

    @Override
    public void execute(SensorContext context) {
	try (Stream<Path> paths = createStream(context)) {
	    paths.forEach(filePath -> {

		LOGGER.debug("Path in stream" + filePath.toFile().getAbsolutePath());

		if (filePath.toFile().isDirectory()) {
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

	    ProjectExplorer.searchByType(context, InputFile.Type.MAIN).stream()
		    .filter(i -> !inputFileWithCoverage.contains(i)).forEach(i -> saveForAllLine(context, i));

	} catch (final IOException e) {
	    LOGGER.error("IO Exception " + context.fileSystem().baseDir().getPath());
	}
    }

    public void saveForAllLine(final SensorContext context, final InputFile i) {
	final NewCoverage coverage = context.newCoverage().onFile(i);
	try (Stream<String> fileLine = Files.lines(i.path())) {
	    FactoryLine.init();
	    LinePredicate.init();
	    fileLine.map(FactoryLine::create).filter(LinePredicate::filterLine)
		    .forEach(l -> coverage.lineHits(l.getLineNumber(), l.getHits()));

	    coverage.ofType(CoverageType.UNIT);
	    coverage.save();
	} catch (IOException e) {
	    LOGGER.warn("IO Exception" + e);
	}

    }

    public void save(SensorContext context, Map<String, List<LineCoverage>> coveragePerFile) {
	for (Map.Entry<String, List<LineCoverage>> entry : coveragePerFile.entrySet()) {
	    final String filePath = entry.getKey();
	    final List<LineCoverage> lines = entry.getValue();
	    final InputFile inputFile = ProjectExplorer.getByPath(context, filePath);
	    inputFileWithCoverage.add(inputFile);
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

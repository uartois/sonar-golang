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
import java.util.List;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.core.language.GoLanguage;

public class CoverageRecorder {
    private static final Logger LOGGER = Loggers.get(CoverageRecorder.class);

    public static void save(SensorContext context, List<LineCoverage> lines, String filePath) {
	FileSystem fileSystem = context.fileSystem();
	FilePredicates predicates = fileSystem.predicates();
	String key = filePath.startsWith(File.separator) ? filePath : (File.separator + filePath);
	InputFile inputFile = fileSystem
		.inputFile(predicates.and(predicates.matchesPathPattern("file:**" + key.replace(File.separator, "/")),
			predicates.hasType(InputFile.Type.MAIN), predicates.hasLanguage(GoLanguage.KEY)));

	if (inputFile == null) {
	    LOGGER.warn("unable to create InputFile object: " + filePath);
	    return;
	}

	NewCoverage coverage = context.newCoverage().onFile(inputFile);

	for (LineCoverage line : lines) {
	    coverage.lineHits(line.getLineNumber(), line.getHits());
	    LOGGER.info(line.toString());
	}
	coverage.ofType(CoverageType.UNIT);
	coverage.save();
    }
}

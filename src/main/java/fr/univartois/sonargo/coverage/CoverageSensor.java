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
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

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

	public Stream<Path> createStream(SensorContext context) throws IOException {
		return Files.walk(Paths.get(context.fileSystem().baseDir().getPath()))
				.filter(p -> !p.getFileName().toString().startsWith("."));

	}

	@Override
	public void execute(SensorContext context) {

		try (Stream<Path> paths = createStream(context)) {
			paths.forEach(filePath -> {
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

}

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
package fr.univartois.sonargo.gotest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class GoTestReportSaver {
	private static final Logger LOGGER = Loggers.get(GoTestReportSaver.class);

	public static void save(SensorContext context, List<Map<String, GoTestFile>> list) {
		FilePredicates predicates = context.fileSystem().predicates();
		for (Map<String, GoTestFile> map : list) {
			for (Map.Entry<String, GoTestFile> entry : map.entrySet()) {
				String key = entry.getKey();
				LOGGER.debug("Key is " + key);
				GoTestFile value = entry.getValue();
				LOGGER.debug("saving measures for file " + value.getFile());
				if (value.getFile() == null) {
					LOGGER.warn("No file could be determined from " + value.getFile());
					continue;
				}
				InputFile file = context.fileSystem().inputFile(predicates.hasAbsolutePath(value.getFile()));
				if (file == null) {
					LOGGER.warn("File not found " + value.getFile());
					continue;
				}
				try {
					saveMeasure(value, context, file);
				} catch (Exception e) { // yes, I know this is naughty...
					LOGGER.warn(e.getMessage());
				}
			}
		}

	}

	private static void saveMeasure(GoTestFile t, SensorContext context, InputFile file) {
		saveMeasure(context, file, CoreMetrics.SKIPPED_TESTS, t.getSkipped());
		saveMeasure(context, file, CoreMetrics.TESTS, t.getNbTotalTest());
		saveMeasure(context, file, CoreMetrics.TEST_FAILURES, t.getNbFailureTest());
		saveMeasure(context, file, CoreMetrics.TEST_EXECUTION_TIME, t.getTime());
	}

    private static <T extends Serializable> void saveMeasure(SensorContext context, InputFile inputFile,
	    Metric<T> metric, T value) {
	LOGGER.warn(inputFile.absolutePath());
	LOGGER.warn(metric.toString());
	context.<T>newMeasure().forMetric(metric).on(inputFile).withValue(value).save();
    }
}

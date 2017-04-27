package fr.univartois.sonargo.test;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class TestReportSaver {
	private static final Logger LOGGER = Loggers.get(TestReportSaver.class);

	public static void save(SensorContext context, List<TestSuite> list) {
		for (TestSuite t : list) {

			File f = new File(new File(t.getFile()).getName());
			FilePredicates predicates = context.fileSystem().predicates();
			InputFile file = context.fileSystem().inputFile(predicates
					.hasAbsolutePath(f.getAbsolutePath() + File.separator + new File(t.getFile()).getName() + ".go"));
			if (file == null) {
				LOGGER.warn("file not found " + f.getAbsolutePath());
				return;
			}
			saveMeasure(t, context, file);
		}
	}

	private static void saveMeasure(TestSuite t, SensorContext context, InputFile file) {
		saveMeasure(context, file, CoreMetrics.SKIPPED_TESTS, t.getSkipped());
		saveMeasure(context, file, CoreMetrics.TESTS, t.getNbTotalTest());
		saveMeasure(context, file, CoreMetrics.TEST_FAILURES, t.getNbFailureTest());
		saveMeasure(context, file, CoreMetrics.TEST_EXECUTION_TIME, t.getTime().longValue());
	}

	private static <T extends Serializable> void saveMeasure(SensorContext context, InputFile inputFile,
			Metric<T> metric, T value) {
		LOGGER.info("Save measure: " + metric.toString() + " " + inputFile.path().toFile().getPath() + " "
				+ value.toString());
		context.<T>newMeasure().forMetric(metric).on(inputFile).withValue(value).save();
	}
}

package fr.univartois.sonargo.core.metrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.core.language.GoLanguage;

public class GoMetricSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(GoMetricSensor.class);
    private final FileLinesContextFactory fileLinesContextFactory;

    public GoMetricSensor(FileLinesContextFactory fileLinesContextFactory) {
	this.fileLinesContextFactory = fileLinesContextFactory;
    }

    public Stream<Path> createStream(SensorContext context) throws IOException {
	return Files.walk(Paths.get(context.fileSystem().baseDir().getPath()))
		.filter(p -> !p.getFileName().toString().endsWith(".go"));
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
	descriptor.name("Go Metrics Sensor");

    }

    @Override
    public void execute(SensorContext context) {
	final FileSystem fileSystem = context.fileSystem();
	final FilePredicates predicates = fileSystem.predicates();
	final Iterable<InputFile> files = fileSystem.inputFiles(predicates.all());
	final List<InputFile> listFiles = new ArrayList<>();
	files.forEach(listFiles::add);

	listFiles.stream().filter((i) -> {
	    return GoLanguage.KEY.equals(i.language());
	}).forEach((i) -> {
	    final GoLineMetrics goline = new GoLineMetrics(i, context);
	    goline.analyseFile();
	    saveMetrics(context, i, CoreMetrics.NCLOC, goline.getNumberLineOfCode());
	    saveMetrics(context, i, CoreMetrics.COMMENT_LINES, goline.getNumberLineComment());
	});
    }

    public void saveMetrics(SensorContext context, InputFile inputFile, Metric<Integer> metric, Integer value) {
	context.<Integer>newMeasure().withValue(value).forMetric(metric).on(inputFile).save();

    }

}

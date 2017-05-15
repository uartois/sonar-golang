package fr.univartois.sonargo.highlighter;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;

import fr.univartois.sonargo.core.language.GoLanguage;

public class HighlighterSensor implements Sensor {

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Go Highlighter Sensor");

	}

	@Override
	public void execute(SensorContext context) {
		FileSystem fileSystem = context.fileSystem();
		FilePredicates predicates = fileSystem.predicates();
		Iterable<InputFile> files = fileSystem.inputFiles(predicates.all());
		List<InputFile> listFiles = new ArrayList<>();
		files.forEach(listFiles::add);

		listFiles.stream().filter((i) -> {
			System.out.println(i.language());
			return GoLanguage.KEY.equals((i.language()));
		}).forEach((i) -> {
			Colorizer colo = new Colorizer(context);
			colo.colorize(i);
		});
	}

}

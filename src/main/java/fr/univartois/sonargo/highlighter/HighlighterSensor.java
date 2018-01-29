package fr.univartois.sonargo.highlighter;

import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.core.ProjectExplorer;
import fr.univartois.sonargo.core.settings.GoProperties;

public class HighlighterSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(HighlighterSensor.class);

    @Override
    public void describe(SensorDescriptor descriptor) {
	descriptor.name("Go Highlighter Sensor");

    }

    @Override
    public void execute(SensorContext context) {

	if (!context.settings().getBoolean(GoProperties.HIGHLIGHTING_KEY)) {
	    LOGGER.info("highlighting disabled");
	    return;
	}

	final List<InputFile> listFiles = ProjectExplorer.searchFileWithTypeMainOrTest(context);
	listFiles.forEach(i -> new Colorizer(context).colorize(i));
    }

}

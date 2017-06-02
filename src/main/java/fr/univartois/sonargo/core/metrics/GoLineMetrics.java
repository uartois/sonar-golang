package fr.univartois.sonargo.core.metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class GoLineMetrics {
    private static final Logger LOGGER = Loggers.get(GoLineMetrics.class);

    private int numberLineOfCode = 0;
    private int numberLineComment = 0;
    private final InputFile file;
    private final SensorContext context;

    public GoLineMetrics(InputFile f, SensorContext c) {
	file = f;
	context = c;
    }

    public void analyseFile() {

	final File f = file.file();

	try (final BufferedReader br = new BufferedReader(
		new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {

	    String line;
	    while ((line = br.readLine()) != null) {
		if (!line.startsWith("//")) {
		    numberLineOfCode++;
		} else {
		    numberLineComment++;
		}
	    }
	    br.close();
	} catch (final IOException e) {
	    LOGGER.error("IO Exception", e);
	}
    }

    public int getNumberLineOfCode() {
	return numberLineOfCode;
    }

    public int getNumberLineComment() {
	return numberLineComment;
    }

}

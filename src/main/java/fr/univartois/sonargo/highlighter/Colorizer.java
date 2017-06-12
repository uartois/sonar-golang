package fr.univartois.sonargo.highlighter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.core.language.GoLexer;

public class Colorizer {

	private static final Logger LOGGER = Loggers.get(Colorizer.class);
	private final NewHighlighting highlighting;

	public Colorizer(SensorContext context) {
		super();

		highlighting = context.newHighlighting();
	}

	public void colorize(InputFile i) {
		final File f = i.file();
		LOGGER.debug("Coloring the file: " + f.getPath());
		highlighting.onFile(i);
		try (final BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {

			String line;
			int lineNumber = 1;
			while ((line = br.readLine()) != null) {
				searchAndColor(line, lineNumber);
				lineNumber++;
			}
			br.close();
		} catch (final IOException e) {
			LOGGER.error("IO Exception", e);
		}
		try {
			highlighting.save();
		} catch (Exception ex) {
			LOGGER.error("Highlighting problem on save", ex);
		}

	}

	private boolean haveString(String s) {
		return s.chars().filter(i -> i == '"').count() > 0;
	}

	private void highlightingComment(int lineNumber, int end) {
		highlighting.highlight(lineNumber, 0, lineNumber, end, TypeOfText.COMMENT);

	}

	private int searchNextOccurrenceOfDoubleQuote(String line, int start) {
		int index = line.indexOf('"', start);
		LOGGER.debug(line + " " + index);
		while (index > 0 && line.charAt(index - 1) == '\\') {
			index = line.indexOf('"', index + 1);
			LOGGER.debug(line + " " + index);
		}
		return index;
	}

	private void highlightingStringInLine(String line, int lineNumber) {
		if (!haveString(line)) {
			return;
		}
		int start = 0;
		int index = searchNextOccurrenceOfDoubleQuote(line, start);
		int indexEnd = searchNextOccurrenceOfDoubleQuote(line, index + 1);

		int lastIndex = 0;
		int lastIndexEnd = 0;

		while (index != -1 && indexEnd != -1 && lastIndex != index && lastIndexEnd != indexEnd) {
			LOGGER.debug("index " + index + " indexEnd " + indexEnd);
			try {
				highlighting.highlight(lineNumber, index, lineNumber, indexEnd + 1, TypeOfText.STRING);
			} catch (Exception ex) {
				LOGGER.error("String highlighting problem", ex);
			}
			start = indexEnd + 1;
			lastIndex = index;
			lastIndexEnd = indexEnd;
			index = searchNextOccurrenceOfDoubleQuote(line, start);
			indexEnd = searchNextOccurrenceOfDoubleQuote(line, index + 1);
		}

	}

	private boolean isAString(String line, int index) {
		return line.substring(0, index).chars().filter(i -> i == '"').count() % 2 != 0;

	}

	private void highlightingKeyWord(String line, int lineNumber) {
		for (final GoLexer.Keyword k : GoLexer.Keyword.values()) {
			final String key = k.getValue();
			int index = 0;
			while ((index = line.indexOf(key, index)) != -1 && !isAString(line, index)) {

				LOGGER.debug("Line number " + lineNumber + " index start: " + index + " index end: "
						+ (index + key.length()));
				try {
					highlighting.highlight(lineNumber, index, lineNumber, index + key.length(), TypeOfText.KEYWORD);
				} catch (Exception ex) {
					LOGGER.error("Keyword highlighting problem", ex);
				}
				index = index + key.length();
			}
		}
	}

	private void searchAndColor(String s, int lineNumber) {
		if (s.trim().startsWith(GoLexer.COMMENT_SYMBOL)) {
			highlightingComment(lineNumber, s.length());
		} else {
			highlightingStringInLine(s, lineNumber);
			highlightingKeyWord(s, lineNumber);
		}
	}

}

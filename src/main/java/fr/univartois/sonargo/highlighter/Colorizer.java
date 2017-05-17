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
	LOGGER.info("Color the file: " + f.getPath());
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

	highlighting.save();

    }

    private boolean haveString(String s) {
	return s.chars().filter(i -> i == '"').count() > 0;
    }

    private void highlightingComment(int lineNumber, int end) {
	highlighting.highlight(lineNumber, 0, lineNumber, end, TypeOfText.COMMENT);

    }

    private void highlightingStringInLine(String line, int lineNumber) {
	if (!haveString(line)) {
	    return;
	}

	int index = line.indexOf('"');
	int indexEnd = line.indexOf('"', index + 1);

	highlighting.highlight(lineNumber, index, lineNumber, indexEnd + 1, TypeOfText.STRING);

	while ((index = line.indexOf('"', indexEnd + 1)) != -1) {
	    indexEnd = line.indexOf('"', index + 1);
	    highlighting.highlight(lineNumber, index, lineNumber, indexEnd + 1, TypeOfText.STRING);
	}

    }

    private boolean isAString(String line, int index) {
	return line.substring(0, index).chars().filter(i -> i == '"').count() % 2 != 0;
    }

    private void highlightingKeyWord(String line, int lineNumber) {
	for (final GoLexer.Keyword k : GoLexer.Keyword.values()) {
	    final String key = k.name();
	    int index = 0;
	    while ((index = line.indexOf(key, index)) != -1 && !isAString(line, index)) {

		LOGGER.debug("Line number " + lineNumber + " index start: " + index + " index end: "
			+ (index + key.length()));

		highlighting.highlight(lineNumber, index, lineNumber, index + key.length(), TypeOfText.KEYWORD);
		index = index + key.length();
	    }
	}
    }

    private boolean isAKeyWord(String line, int index) {
	for (final GoLexer.Keyword key : GoLexer.Keyword.values()) {
	    final String keyword = key.name().toLowerCase();
	    final int indexEnd = index + keyword.length() >= line.length() ? line.length() : index + keyword.length();

	    LOGGER.debug("substring " + line.substring(index, indexEnd));

	    if (line.substring(index, indexEnd).indexOf(keyword) != -1) {
		return true;
	    }
	}
	return false;
    }

    /*
     * private void highlightingType(String line, int lineNumber) { for (final
     * GoLexer.Keyword t : GoLexer.Keyword.values()) { final int index; final
     * String type = t.name().toLowerCase(); LOGGER.debug(type);
     * 
     * if ((index = line.indexOf(type)) != -1 && !isAKeyWord(line, index)) {
     * 
     * LOGGER.debug("Line number " + lineNumber + " index start: " + index +
     * " index end: " + (index + type.length()));
     * highlighting.highlight(lineNumber, index, lineNumber, index +
     * type.length(), TypeOfText.KEYWORD_LIGHT);
     * 
     * }
     * 
     * LOGGER.debug("position " + index + " line " + line);
     * 
     * } }
     */

    private void searchAndColor(String s, int lineNumber) {
	if (s.trim().startsWith(GoLexer.COMMENT_SYMBOL)) {
	    highlightingComment(lineNumber, s.length());
	} else {
	    highlightingStringInLine(s, lineNumber);
	    highlightingKeyWord(s, lineNumber);
	    // highlightingType(s, lineNumber);
	}
    }

}

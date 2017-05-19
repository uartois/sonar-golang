package fr.univartois.sonargo.highlighter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;

import fr.univartois.sonargo.AbstractSonarTest;
import fr.univartois.sonargo.TestUtils;

public class ColorizerTest extends AbstractSonarTest {
    private File file;
    private DefaultInputFile inputFile;

    @Override
    @Before
    public void init() {
	init(TestUtils.getColorizeDir());

	file = new File(testerContext.fileSystem().baseDir(), "/test.go");
	inputFile = new DefaultInputFile("moduleKey", file.getName())
		.initMetadata(new FileMetadata().readMetadata(file, StandardCharsets.UTF_8));

	testerContext.fileSystem().add(inputFile);
    }

    @Test
    public void testColorize() {

	file = new File(testerContext.fileSystem().baseDir(), "/test.go");
	inputFile = new DefaultInputFile("moduleKey", file.getName())
		.initMetadata(new FileMetadata().readMetadata(file, StandardCharsets.UTF_8));

	testerContext.fileSystem().add(inputFile);

	final Colorizer c = new Colorizer(testerContext);
	c.colorize(inputFile);
	final String componentKey = "moduleKey:" + file.getName();

	assertEquals(TypeOfText.KEYWORD, testerContext.highlightingTypeAt(componentKey, 1, 1).get(0));
	assertEquals(TypeOfText.KEYWORD, testerContext.highlightingTypeAt(componentKey, 3, 3).get(0));
    }

    @Test
    public void testBug1() {

	file = new File(testerContext.fileSystem().baseDir(), "/testbug1.go");
	inputFile = new DefaultInputFile("moduleKey", file.getName())
		.initMetadata(new FileMetadata().readMetadata(file, StandardCharsets.UTF_8));

	testerContext.fileSystem().add(inputFile);

	final Colorizer c = new Colorizer(testerContext);
	c.colorize(inputFile);

	checkOnRange(1, 0, 3, TypeOfText.KEYWORD); // var
	checkOnRange(1, 13, 6, TypeOfText.KEYWORD); // string
	checkOnRange(1, 23, 12, TypeOfText.STRING); // "test\"other"

    }

    private void checkOnRange(int line, int start, int length, TypeOfText typeOfText) {
	final String componentKey = "moduleKey:" + file.getName();

	List<TypeOfText> found;

	for (int column = start; column < start + length; column++) {
	    System.out.println(line + " " + column);
	    found = testerContext.highlightingTypeAt(componentKey, line, column);
	    assertFalse(found.isEmpty());
	    assertEquals(typeOfText, found.get(0));
	}

    }
}

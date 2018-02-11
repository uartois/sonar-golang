package fr.univartois.sonargo.coverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.google.common.collect.ImmutableMap;

import fr.univartois.sonargo.AbstractSonarTest;
import fr.univartois.sonargo.TestUtils;
import fr.univartois.sonargo.core.language.GoLanguage;

public class CoverageSensorTest extends AbstractSonarTest {
    private static final Logger LOGGER = Loggers.get(CoverageSensorTest.class);

    @Before
    @Override
    public void init() {
	init(TestUtils.getCoverageBaseDir());
    }

    @Test
    public void testCreateStream() {
	final CoverageSensor sensor = new CoverageSensor();
	try (Stream<Path> paths = sensor.createStream(testerContext)) {

	    assertNotNull(paths);

	} catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testExcludedPathProperty() {
	final CoverageSensor sensor = new CoverageSensor();
	boolean check = sensor.isAnExcludedPath(Paths.get(fileSystem.baseDir().getAbsolutePath(), "vendor/test"),
		testerContext);
	assertTrue(check);
    }

    @Test
    public void testIgnoreFileStream() {
	final CoverageSensor sensor = new CoverageSensor();
	Stream<Path> paths;
	try {
	    paths = sensor.createStream(testerContext);
	    assertEquals(33, paths.count());

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private void initFile() {
	String[] listFile = { "util/util.go", "mathutil/mathutil.go", "pixel/pixel.go",
		"mathutil/filewithnocoverage.go", "issue60/a.go", "issue61/simplelinecomment.go",
		"issue61/multilinecomment.go", "issue61/typestruct.go" };

	BufferedReader reader;
	try {

	    for (int i = 0; i < listFile.length; i++) {
		System.out.println(listFile[i]);
		reader = new BufferedReader(new FileReader(
			new File(CoverageSensor.class.getResource("/coverage/" + listFile[i]).getFile())));

		String sCurrentLine;

		StringBuilder sb = new StringBuilder();
		while ((sCurrentLine = reader.readLine()) != null) {
		    sb.append(sCurrentLine + "\n");
		}

		testerContext.fileSystem().add(new DefaultInputFile("myProjectKey", listFile[i])
			.setLanguage(GoLanguage.KEY).initMetadata(sb.toString()));

	    }

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testExecution() {
	final CoverageSensor sensor = new CoverageSensor();
	initFile();
	sensor.execute(testerContext);

	Map<String, Map<Integer, Integer>> map = new HashMap<>();
	map.put("myProjectKey:mathutil/mathutil.go", ImmutableMap.of(7, 1));
	map.put("myProjectKey:pixel/pixel.go", ImmutableMap.of(21, 0, 37, 0));

	Map<Integer, Integer> testValuesMap = new HashMap<>();

	testValuesMap.put(3, null);
	testValuesMap.put(1, null);
	testValuesMap.put(4, null);
	testValuesMap.put(8, 0);
	testValuesMap.put(12, 0);

	map.put("myProjectKey:mathutil/filewithnocoverage.go", testValuesMap);
	map.put("myProjectKey:issue60/a.go", ImmutableMap.of(8, 0, 9, 0));
	Map<Integer, Integer> testValuesMap2 = new HashMap<>();
	testValuesMap2.put(1, null);
	testValuesMap2.put(2, null);
	testValuesMap2.put(3, null);
	testValuesMap2.put(4, null);
	testValuesMap2.put(5, null);
	testValuesMap2.put(6, null);
	testValuesMap2.put(8, 0);
	testValuesMap2.put(9, 0);
	testValuesMap2.put(11, null);
	map.put("myProjectKey:issue61/simplelinecomment.go", testValuesMap2);

	Map<Integer, Integer> testValuesMap3 = new HashMap<>();

	for (int i = 1; i <= 20; i++) {
	    testValuesMap3.put(i, null);
	}
	testValuesMap3.put(26, null);
	testValuesMap3.put(27, null);
	testValuesMap3.put(28, null);
	testValuesMap3.put(30, 0);
	testValuesMap3.put(31, 0);

	map.put("myProjectKey:issue61/multilinecomment.go", testValuesMap3);

	Map<Integer, Integer> testValuesMap4 = new HashMap<>();
	for (int i = 6; i <= 48; i++) {
	    testValuesMap4.put(i, null);
	}
	map.put("myProjectKey:issue61/typestruct.go", testValuesMap4);
	map.forEach((key, mapValue) -> {
	    mapValue.forEach((line, value) -> {
		assertEquals("line " + line + " " + key, value, testerContext.lineHits(key, CoverageType.UNIT, line));
	    });
	});

    }

}

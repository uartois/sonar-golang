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
	    assertEquals(28, paths.count());

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Test
    public void testExecution() {
	final CoverageSensor sensor = new CoverageSensor();

	try {
	    BufferedReader reader = new BufferedReader(
		    new FileReader(new File(CoverageSensor.class.getResource("/coverage/util/util.go").getFile())));

	    String sCurrentLine;

	    StringBuilder sb = new StringBuilder();
	    while ((sCurrentLine = reader.readLine()) != null) {
		sb.append(sCurrentLine + "\n");
	    }

	    testerContext.fileSystem().add(new DefaultInputFile("myProjectKey", "util/util.go")
		    .setLanguage(GoLanguage.KEY).initMetadata(sb.toString()));

	    reader = new BufferedReader(new FileReader(
		    new File(CoverageSensor.class.getResource("/coverage/mathutil/mathutil.go").getFile())));

	    sb = new StringBuilder();
	    while ((sCurrentLine = reader.readLine()) != null) {
		sb.append(sCurrentLine + "\n");
	    }

	    testerContext.fileSystem().add(new DefaultInputFile("myProjectKey", "mathutil/mathutil.go")
		    .setLanguage(GoLanguage.KEY).initMetadata(sb.toString()));

	    reader = new BufferedReader(
		    new FileReader(new File(CoverageSensor.class.getResource("/coverage/pixel/pixel.go").getFile())));

	    sb = new StringBuilder();
	    while ((sCurrentLine = reader.readLine()) != null) {
		sb.append(sCurrentLine + "\n");
	    }

	    testerContext.fileSystem().add(new DefaultInputFile("myProjectKey", "pixel/pixel.go")
		    .setLanguage(GoLanguage.KEY).initMetadata(sb.toString()));

	    reader = new BufferedReader(new FileReader(
		    new File(CoverageSensor.class.getResource("/coverage/mathutil/filewithnocoverage.go").getFile())));

	    sb = new StringBuilder();
	    while ((sCurrentLine = reader.readLine()) != null) {
		sb.append(sCurrentLine + "\n");
	    }

	    testerContext.fileSystem().add(new DefaultInputFile("myProjectKey", "mathutil/filewithnocoverage.go")
		    .setLanguage(GoLanguage.KEY).initMetadata(sb.toString()));

	    reader = new BufferedReader(
		    new FileReader(new File(CoverageSensor.class.getResource("/coverage/mathutil/a.go").getFile())));

	    sb = new StringBuilder();
	    while ((sCurrentLine = reader.readLine()) != null) {
		sb.append(sCurrentLine + "\n");
	    }

	    testerContext.fileSystem().add(new DefaultInputFile("myProjectKey", "mathutil/a.go")
		    .setLanguage(GoLanguage.KEY).initMetadata(sb.toString()));

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
	    map.put("myProjectKey:mathutil/a.go", ImmutableMap.of(8, 0, 9, 0));

	    map.forEach((key, mapValue) -> {
		mapValue.forEach((line, value) -> {
		    assertEquals("line " + line + " " + key, value,
			    testerContext.lineHits(key, CoverageType.UNIT, line));
		});
	    });

	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	} catch (final IOException e) {
	    e.printStackTrace();
	}

    }

}

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
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

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
	    assertEquals(27, paths.count());

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
		    new File(CoverageSensor.class.getResource("/coverage/mathutil/filewithnotcoverage.go").getFile())));

	    sb = new StringBuilder();
	    while ((sCurrentLine = reader.readLine()) != null) {
		sb.append(sCurrentLine + "\n");
	    }

	    testerContext.fileSystem().add(new DefaultInputFile("myProjectKey", "mathutil/filewithnotcoverage.go")
		    .setLanguage(GoLanguage.KEY).initMetadata(sb.toString()));

	    sensor.execute(testerContext);

	    assertEquals(Integer.valueOf(1),
		    testerContext.lineHits("myProjectKey:mathutil/mathutil.go", CoverageType.UNIT, 7));

	    assertEquals(Integer.valueOf(0),
		    testerContext.lineHits("myProjectKey:pixel/pixel.go", CoverageType.UNIT, 28));

	    assertEquals(Integer.valueOf(0),
		    testerContext.lineHits("myProjectKey:pixel/pixel.go", CoverageType.UNIT, 37));

	    assertEquals(Integer.valueOf(0),
		    testerContext.lineHits("myProjectKey:mathutil/filewithnotcoverage.go", CoverageType.UNIT, 3));

	    LOGGER.info(testerContext.measures("myProjectKey").toString());

	} catch (final FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}

package fr.univartois.sonargo.coverage;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.Test;

import fr.univartois.sonargo.AbstractSonarTest;
import fr.univartois.sonargo.TestUtils;

public class CoverageSensorTest extends AbstractSonarTest {
	public CoverageSensorTest() {
		super(TestUtils.getCoverageBaseDir());
	}

	@Test
	public void testCreateStream() {
		CoverageSensor sensor = new CoverageSensor();
		try (Stream<Path> paths = sensor.createStream(testerContext)) {

			assertEquals(7, paths.count());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

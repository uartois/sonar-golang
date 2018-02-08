package fr.univartois.sonargo.core.metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

import fr.univartois.sonargo.AbstractSonarTest;
import fr.univartois.sonargo.TestUtils;

public class GoMetricSensorTest extends AbstractSonarTest {
    @Before
    @Override
    public void init() {
	init(TestUtils.getProjectDir());
    }

    @Test
    public void testExecute() {
	GoMetricSensor sensor = new GoMetricSensor();
	sensor.execute(testerContext);
	Map<Metric<Integer>, Integer> measures = sensor.getMeasures();
	assertNotNull(measures);
	assertEquals(new Integer(7), measures.get(CoreMetrics.NCLOC));
    }

}

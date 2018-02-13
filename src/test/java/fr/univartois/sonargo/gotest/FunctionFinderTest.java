/**
 * 
 */
package fr.univartois.sonargo.gotest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.AbstractSonarTest;
import fr.univartois.sonargo.TestUtils;

/**
 * @author thibault
 *
 */
public class FunctionFinderTest extends AbstractSonarTest {
    private static final Logger LOGGER = Loggers.get(FunctionFinderTest.class);

    @Before
    @Override
    public void init() {
	init(TestUtils.getTestBaseDir());
    }

    @Test
    public void testSearchInFile() {
	String path = new File(TestUtils.getTestBaseDir().baseDir(), "test_test.go").getAbsolutePath();
	LOGGER.info("testSearchInFile " + path);
	FunctionFinder f = new FunctionFinder(testerContext);
	f.searchFunctionInFile(Paths.get(path));
	Map<String, String> result = f.getResult();
	Set<String> expected = new java.util.HashSet<String>();
	expected.add("TestEasyDef");
	expected.add("TestSpaceDef");
	expected.add("TestNoSpaceDef");
	expected.add("TestTwoLines1");
	expected.add("TestTwoLines2");
	expected.add("TestNested");
	expected.add("TestSuite");
	assertEquals(expected, result.keySet());

    }

    @Test
    public void testSearchFunction() {
	LOGGER.info("testSearchFunction " + testerContext.fileSystem().baseDir());
	FunctionFinder f = new FunctionFinder(testerContext);
	Map<String, String> nameFunction = f.searchFunction();

	assertTrue(nameFunction.containsKey("TestAverage"));

    }

}

/**
 * 
 */
package fr.univartois.sonargo.gotest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fr.univartois.sonargo.AbstractSonarTest;
import fr.univartois.sonargo.TestUtils;

/**
 * @author thibault
 *
 */
public class FunctionFinderTest extends AbstractSonarTest {
	@Before
	@Override
	public void init() {
		init(TestUtils.getTestBaseDir());
	}

	@Test
	public void testSearchInFile() {
		try {
			String path = new File(testerContext.fileSystem().baseDir(), "test_test.go").getPath();

			FunctionFinder f = new FunctionFinder(testerContext);
			f.searchFunctionInFile(Paths.get(path));
			HashMap<String, String> result = f.getResult();
			Set<String> expected = new java.util.HashSet<String>();
			expected.add("TestEasyDef");
			expected.add("TestSpaceDef");
			expected.add("TestNoSpaceDef");
			expected.add("TestTwoLines1");
			expected.add("TestTwoLines2");
			expected.add("TestNested");
			expected.add("TestSuite");
			assertEquals(expected, result.keySet());

		} catch (IOException e) {
			fail("IOException thrown in test.");
		}
	}

	@Test
	public void testSearchFunction() {
		try {
			FunctionFinder f = new FunctionFinder(testerContext);
			HashMap<String, String> nameFunction = f.searchFunction();
			assertTrue(nameFunction.containsKey("TestAverage"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

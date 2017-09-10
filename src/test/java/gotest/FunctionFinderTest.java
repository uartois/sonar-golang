/**
 * 
 */
package gotest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import fr.univartois.sonargo.AbstractSonarTest;
import fr.univartois.sonargo.TestUtils;
import fr.univartois.sonargo.gotest.FunctionFinder;

/**
 * @author thibault
 *
 */
public class FunctionFinderTest extends AbstractSonarTest {
	@Before
	@Override
	public void init() {
		init(TestUtils.getDefaultFileSystem());
	}

	@Test
	public void testSearchInFile() {
		try {
			FunctionFinder f = new FunctionFinder(testerContext);
			assertEquals("TestAverage", f.searchInLine("func TestAverage(t *testing.T)"));
			assertEquals("TestGutterBalls", f.searchInLine("func TestGutterBalls(t *testing.T)"));
			assertEquals("TestOnePinOnEveryThrow", f.searchInLine("func TestOnePinOnEveryThrow(t *testing.T) {"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testSearchFunction() {
		try {
			FunctionFinder f = new FunctionFinder(testerContext);
			HashMap<String, String> nameFunction = f.searchFunction();

			assertTrue(nameFunction.containsKey("test1_test.go#TestAverage"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

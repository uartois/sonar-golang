/**
 * 
 */
package fr.univartois.sonargo;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

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

}

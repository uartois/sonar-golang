package fr.univartois.sonargo.coverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import fr.univartois.sonargo.AbstractSonarTest;
import fr.univartois.sonargo.TestUtils;

public class CoverageParserTest extends AbstractSonarTest {
    private CoverageParser coverage;

    @Override
    @Before
    public void init() {
	init(TestUtils.getCoverageBaseDir());
    }

    @Test
    public void testParse() {
	coverage = new CoverageParser();
	try {

	    assertNull(coverage.getFilepath());
	    assertTrue(coverage.getList().isEmpty());

	    coverage.parse(fileSystem.baseDir() + File.separator + "pkg1/coverage.xml");
	    assertEquals("/home/thibault/workspace/go/src/projectblackwhitego/util/util.go", coverage.getFilepath());

	    assertEquals(77, coverage.getList().size());

	} catch (ParserConfigurationException | SAXException | IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}

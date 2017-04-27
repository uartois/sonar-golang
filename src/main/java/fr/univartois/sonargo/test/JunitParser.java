package fr.univartois.sonargo.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.univartois.sonargo.Parser;

public class JunitParser implements Parser {
	private static final String TEST_SUITE_TAG = "testsuite";
	private static final String TEST_SKIPPED_TAG = "skipped";
	private static final String TEST_CASE_TAG = "testcase";
	private static final String NB_TOTAL_TEST_ATTR = "tests";
	private static final String NB_TEST_FAILURE_ATRR = "failures";
	private static final String NAME_TEST_ATTR = "name";
	private static final String TIME_TEST_ATTR = "time";

	private List<TestSuite> listTestSuite = new ArrayList<>();

	@Override
	public void parse(String reportPath) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File(reportPath));

		doc.getDocumentElement().normalize();

		NodeList testSuiteList = doc.getElementsByTagName(TEST_SUITE_TAG);
		for (int i = 0; i < testSuiteList.getLength(); i++) {
			Node nNode = testSuiteList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				listTestSuite.add(new TestSuite(Integer.parseInt(eElement.getAttribute(NB_TOTAL_TEST_ATTR)),
						Integer.parseInt(eElement.getAttribute(NB_TEST_FAILURE_ATRR)),
						eElement.getElementsByTagName(TEST_SKIPPED_TAG).getLength(),
						eElement.getAttribute(NAME_TEST_ATTR),
						Double.parseDouble(eElement.getAttribute(TIME_TEST_ATTR))));

			}
		}

	}

	public List<TestSuite> getListTestSuite() {
		return listTestSuite;
	}

}

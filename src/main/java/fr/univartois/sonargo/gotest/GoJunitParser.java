/*******************************************************************************
 * Copyright 2017 - Université d'Artois
 *
 * This file is part of SonarQube Golang plugin (sonar-golang).
 *
 * Sonar-golang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sonar-golang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar-golang.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *            Thibault Falque (thibault_falque@ens.univ-artois.fr)
 *******************************************************************************/
package fr.univartois.sonargo.gotest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.univartois.sonargo.core.Parser;

public class GoJunitParser implements Parser {
    private static final String TEST_SUITE_TAG = "testsuite";
    private static final String TEST_SKIPPED_TAG = "skipped";
    private static final String NB_TOTAL_TEST_ATTR = "tests";
    private static final String NB_TEST_FAILURE_ATRR = "failures";
    private static final String NAME_TEST_ATTR = "name";
    private static final String TIME_TEST_ATTR = "time";
    private static final String FAILURE_TAG = "failure";
    private static final String TEST_CASE_TAG = "testcase";

    private static final Logger LOGGER = Loggers.get(GoJunitParser.class);

    private final List<Map<String, GoTestFile>> listTestSuiteByPackage = new ArrayList<>();

    private Map<String, String> functionFileName;

    public GoJunitParser(Map<String, String> functionFileName) {
	this.functionFileName = functionFileName;
    }

    @Override
    public void parse(String reportPath) throws ParserConfigurationException, SAXException, IOException {
	listTestSuiteByPackage.clear();
	final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	final DocumentBuilder db = dbf.newDocumentBuilder();
	final Document doc = db.parse(new File(reportPath));

	doc.getDocumentElement().normalize();

	final NodeList testSuiteList = doc.getElementsByTagName(TEST_SUITE_TAG);
	for (int i = 0; i < testSuiteList.getLength(); i++) {
	    final Node nNode = testSuiteList.item(i);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		final Element eElement = (Element) nNode;

		listTestSuiteByPackage.add(groupTestCaseByFile(eElement));
	    }
	}

    }

    private HashMap<String, GoTestFile> groupTestCaseByFile(Element testSuite) {
	final NodeList testCaseList = testSuite.getElementsByTagName(TEST_CASE_TAG);
	HashMap<String, GoTestFile> mapResult = new HashMap<>();
	for (int i = 0; i < testCaseList.getLength(); i++) {
	    final Node nNode = testCaseList.item(i);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		final Element testCase = (Element) nNode;
		String functionName = testCase.getAttribute(NAME_TEST_ATTR);
		String fileName = functionFileName.get(functionName);
		GoTestFile goTest = null;
		if (mapResult.containsKey(fileName)) {
		    goTest = mapResult.get(fileName);
		} else if (fileName != null) {
		    goTest = new GoTestFile();
		    goTest.setFile(fileName);
		} else {
		    LOGGER.warn("The key is null");
		    continue;
		}

		goTest.addTestCase(new GoTestCase(testCase.getElementsByTagName(FAILURE_TAG).getLength() > 0,
			testCase.getElementsByTagName(TEST_SKIPPED_TAG).getLength() > 0,
			Double.parseDouble(testCase.getAttribute(TIME_TEST_ATTR)), functionName));
		mapResult.put(fileName, goTest);
	    }

	}
	return mapResult;

    }

    public List<Map<String, GoTestFile>> getListTestSuite() {
	return listTestSuiteByPackage;
    }

}

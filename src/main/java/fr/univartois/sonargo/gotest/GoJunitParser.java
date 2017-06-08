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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

    private final List<GoTestSuite> listTestSuite = new ArrayList<>();

    @Override
    public void parse(String reportPath) throws ParserConfigurationException, SAXException, IOException {
	listTestSuite.clear();
	final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	final DocumentBuilder db = dbf.newDocumentBuilder();
	final Document doc = db.parse(new File(reportPath));

	doc.getDocumentElement().normalize();

	final NodeList testSuiteList = doc.getElementsByTagName(TEST_SUITE_TAG);
	for (int i = 0; i < testSuiteList.getLength(); i++) {
	    final Node nNode = testSuiteList.item(i);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		final Element eElement = (Element) nNode;

		listTestSuite.add(new GoTestSuite(Integer.parseInt(eElement.getAttribute(NB_TOTAL_TEST_ATTR)),
			Integer.parseInt(eElement.getAttribute(NB_TEST_FAILURE_ATRR)),
			eElement.getElementsByTagName(TEST_SKIPPED_TAG).getLength(),
			eElement.getAttribute(NAME_TEST_ATTR),
			Double.parseDouble(eElement.getAttribute(TIME_TEST_ATTR))));

	    }
	}

    }

    public List<GoTestSuite> getListTestSuite() {
	return listTestSuite;
    }

}

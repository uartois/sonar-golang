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
package fr.univartois.sonargo.coverage;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.univartois.sonargo.core.Parser;
import fr.univartois.sonargo.core.settings.GoProperties;

public class CoverageParser implements Parser {

    private final Map<String, List<LineCoverage>> coverageByFile = new HashMap<>();
    private static final String FILE_NAME_ATTR = "filename";
    private static final String LINE_NUMBER_ATTR = "number";
    private static final String HITS_ATTR = "hits";
    private static final String METHOD_TAG = "method";
    private static final String CLASS_TAG = "class";
    private static final String LINE_TAG = "line";
    private static final Logger LOGGER = Loggers.get(CoverageParser.class);

    private final boolean checkDtd;

    public CoverageParser(SensorContext context) {
	checkDtd = context.settings().getBoolean(GoProperties.DTD_VERIFICATION_KEY);
    }

    private DocumentBuilder constructDocumentBuilder() throws ParserConfigurationException {
	final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	final DocumentBuilder db = dbf.newDocumentBuilder();

	if (!checkDtd) {
	    db.setEntityResolver((publicId, systemId) -> {
		if (systemId.contains("http://cobertura.sourceforge.net/xml/coverage-03.dtd")) {
		    return new InputSource(new StringReader(""));
		} else {
		    return null;
		}
	    });
	}

	return db;
    }

    /**
     * {@link http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work}
     * 
     * @param reportPath
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    @Override
    public void parse(String reportPath) throws ParserConfigurationException, SAXException, IOException {
	final DocumentBuilder db = constructDocumentBuilder();
	final Document doc = db.parse(new File(reportPath));

	doc.getDocumentElement().normalize();

	final NodeList classList = doc.getElementsByTagName(CLASS_TAG);

	for (int i = 0; i < classList.getLength(); i++) {
	    final Node nNode = classList.item(i);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

		final Element eElement = (Element) nNode;
		final String filepath = eElement.getAttribute(FILE_NAME_ATTR);

		parseMethodTag(eElement.getElementsByTagName(METHOD_TAG), getListForFile(filepath));
	    }
	}

    }

    public Map<String, List<LineCoverage>> getCoveragePerFile() {
	for (Map.Entry<String, List<LineCoverage>> entry : coverageByFile.entrySet()) {
	    String fileName = entry.getKey();
	    List<LineCoverage> list = entry.getValue();
	    LOGGER.debug(list.size() + "line coverage for file " + fileName);
	}
	return coverageByFile;
    }

    private List<LineCoverage> getListForFile(String filepath) {
	List<LineCoverage> list = coverageByFile.get(filepath);
	if (list == null) {
	    list = new ArrayList<>();
	    coverageByFile.put(filepath, list);
	}
	return list;
    }

    private void parseMethodTag(NodeList methodsList, List<LineCoverage> listOfCoverage) {
	for (int j = 0; j < methodsList.getLength(); j++) {
	    final Node nNode = methodsList.item(j);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		final Element eElement = (Element) nNode;
		parseLineTag(eElement.getElementsByTagName(LINE_TAG), listOfCoverage);
	    }
	}
    }

    private void parseLineTag(NodeList lineList, List<LineCoverage> listOfCoverage) {
	for (int j = 0; j < lineList.getLength(); j++) {
	    final Node nNode = lineList.item(j);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		final Element eElement = (Element) nNode;
		listOfCoverage.add(new LineCoverage(Integer.parseInt(eElement.getAttribute(LINE_NUMBER_ATTR)),
			Integer.parseInt(eElement.getAttribute(HITS_ATTR))));
	    }
	}
    }
}

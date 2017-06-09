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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.univartois.sonargo.core.Parser;
import fr.univartois.sonargo.core.language.GoLanguage;

public class CoverageParser implements Parser {

    private static final Logger LOGGER = Loggers.get(CoverageParser.class);
    private final SensorContext context;
    private final List<LineCoverage> listOfCoverage;
    private static final String FILE_NAME_ATTR = "filename";
    private static final String LINE_NUMBER_ATTR = "number";
    private static final String HITS_ATTR = "hits";
    private static final String METHOD_TAG = "method";
    private static final String CLASS_TAG = "class";
    private static final String LINE_TAG = "line";

    public CoverageParser(SensorContext context) {
	this.listOfCoverage = new ArrayList<>();
	this.context = context;
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
	final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setValidating(false);
	dbf.setNamespaceAware(true);
	dbf.setFeature("http://cobertura.sourceforge.net/xml/coverage-03.dtd", false);
	final DocumentBuilder db = dbf.newDocumentBuilder();

	final Document doc = db.parse(new File(reportPath));

	doc.getDocumentElement().normalize();

	final NodeList classList = doc.getElementsByTagName(CLASS_TAG);

	for (int i = 0; i < classList.getLength(); i++) {
	    final Node nNode = classList.item(i);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

		final Element eElement = (Element) nNode;
		final String filepath = eElement.getAttribute(FILE_NAME_ATTR);

		parseMethodTag(eElement.getElementsByTagName(METHOD_TAG));
                save(context, listOfCoverage,filepath);
		listOfCoverage.clear();
	    }
	}

    }

    private void parseMethodTag(NodeList methodsList) {
	for (int j = 0; j < methodsList.getLength(); j++) {
	    final Node nNode = methodsList.item(j);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		final Element eElement = (Element) nNode;
		parseLineTag(eElement.getElementsByTagName(LINE_TAG));
	    }
	}
    }

    private void parseLineTag(NodeList lineList) {
	for (int j = 0; j < lineList.getLength(); j++) {
	    final Node nNode = lineList.item(j);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		final Element eElement = (Element) nNode;
		listOfCoverage.add(new LineCoverage(Integer.parseInt(eElement.getAttribute(LINE_NUMBER_ATTR)),
			Integer.parseInt(eElement.getAttribute(HITS_ATTR))));
	    }
	}
    }

    public static void save(SensorContext context, List<LineCoverage> lines, String filePath) {
	final FileSystem fileSystem = context.fileSystem();
	final FilePredicates predicates = fileSystem.predicates();
	final String key = filePath.startsWith(File.separator) ? filePath : File.separator + filePath;
	final InputFile inputFile = fileSystem
		.inputFile(predicates.and(predicates.matchesPathPattern("file:**" + key.replace(File.separator, "/")),
			predicates.hasType(InputFile.Type.MAIN), predicates.hasLanguage(GoLanguage.KEY)));

	if (inputFile == null) {
	    LOGGER.warn("unable to create InputFile object: " + filePath);
	    return;
	}

	final NewCoverage coverage = context.newCoverage().onFile(inputFile);

	for (final LineCoverage line : lines) {
	    try {
		coverage.lineHits(line.getLineNumber(), line.getHits());
	    } catch (final Exception ex) {
		LOGGER.error(ex.getMessage() + line);
	    }
	}
	coverage.ofType(CoverageType.UNIT);
	coverage.save();
    }

}

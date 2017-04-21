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
package fr.univartois.sonargo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * GoLintIssueLoaderSensor This class can load a report file and create issue
 * 
 * @author thibault
 */
public class GoLintIssueLoaderSensor implements Sensor {
	private static final Logger LOGGER = Loggers.get(GoLintIssueLoaderSensor.class);

	protected final Settings settings;
	protected final FileSystem fileSystem;
	protected SensorContext context;

	/**
	 * Allow to create a new GoLintIssueLoaderSensor
	 * 
	 * @param se
	 * @see {@link Settings}
	 * @param fileSystem
	 * @see {@link FileSystem}
	 */
	public GoLintIssueLoaderSensor(final Settings se, final FileSystem fileSystem) {
		this.settings = se;
		this.fileSystem = fileSystem;
	}

	/**
	 * Create the description of the sensor
	 * 
	 * @param descriptor
	 *            A sensor descriptor @see {@link SensorDescriptor}
	 */
	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.onlyOnLanguage(GoLanguage.KEY).name("GoMetaLinter issues loader sensor");
	}

	private String getReportPath() {
		String reportPath = settings.getString(GoProperties.REPORT_PATH_KEY);
		if (!StringUtils.isEmpty(reportPath)) {
			return reportPath;
		}
		return null;
	}

	/**
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 * @param context
	 * @see {@link SensorContext}
	 */
	@Override
	public void execute(SensorContext context) {

		String reportPath = getReportPath();

		if (!StringUtils.isEmpty(reportPath)) {
			this.context = context;
			File analyse = new File(reportPath);
			try {
				LOGGER.info("Parse the file " + reportPath);
				parseAndSaveResults(analyse);
			} catch (XMLStreamException | ParserConfigurationException e) {
				LOGGER.error("Unable to parse the provided Golint file", e);
			}
		} else {
			LOGGER.warn("No report file ");

		}
	}

	protected void parseAndSaveResults(final File file) throws XMLStreamException, ParserConfigurationException {
		LOGGER.info("Parsing 'GoLint' Analysis Results");

		GoLintResultParser parser = new GoLintResultParser();

		List<GoError> listError = parser.parse(file);
		for (GoError e : listError) {
			getResourceAndSaveIssue(e);
		}

	}

	private void getResourceAndSaveIssue(final GoError error) {
		LOGGER.info(error.toString());

		InputFile inputFile = fileSystem
				.inputFile(fileSystem.predicates().and(fileSystem.predicates().hasRelativePath(error.getFilePath()),
						fileSystem.predicates().hasType(InputFile.Type.MAIN)));

		LOGGER.info("inputFile null ? " + (inputFile == null));
		GoKeyRule.init();
		if (inputFile != null) {
			saveIssue(inputFile, error.getLine(), GoKeyRule.getKeyFromError(error), error.getMessage());
		} else {
			LOGGER.error("Not able to find a InputFile with " + error.getFilePath());
		}
	}

	private static String getRepositoryKeyForLanguage(String languageKey) {
		return languageKey.toLowerCase() + "-" + GoLintRulesDefinition.KEY;
	}

	private void saveIssue(final InputFile inputFile, int line, final String externalRuleKey, final String message) {

		if (externalRuleKey == null) {
			LOGGER.warn("The key for the message " + message + " is null, issue not saved");
			return;
		}

		String language = inputFile.language();
		if (language == null)
			language = GoLanguage.KEY;

		RuleKey ruleKey = RuleKey.of(getRepositoryKeyForLanguage(language), externalRuleKey);

		NewIssue newIssue = context.newIssue().forRule(ruleKey);

		NewIssueLocation primaryLocation = newIssue.newLocation().on(inputFile).message(message);
		if (line > 0) {
			primaryLocation.at(inputFile.selectLine(line));
		}
		newIssue.at(primaryLocation);

		newIssue.save();
	}

	private static class GoLintResultParser {

		private static final String LINE_ATTRIBUTE = "line";
		private static final String MESS_ATTRIBUTE = "message";
		private static final String SEVER_ATTRIBUTE = "severity";

		/**
		 * GoLintResultParser allow parse a checkstyle report file
		 * 
		 * @param file
		 *            The path to checktyle report
		 * @return A list of error
		 * @throws XMLStreamException
		 * @throws ParserConfigurationException
		 *             throw if is not possible to parse the file
		 */
		public List<GoError> parse(File file) throws XMLStreamException, ParserConfigurationException {
			LOGGER.info("Parsing file {}", file.getAbsolutePath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builde;
			try {
				builde = dbFactory.newDocumentBuilder();
				Document doc = builde.parse(file);

				NodeList nList = doc.getElementsByTagName("file");

				List<GoError> listError = new ArrayList<>();
				for (int i = 0; i < nList.getLength(); i++) {

					Element n = (Element) nList.item(i);

					String filename = n.getAttribute("name");

					LOGGER.info("error for the file " + filename);

					NodeList children = n.getChildNodes();

					for (int j = 0; j < children.getLength(); j++) {

						if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {

							Element e = (Element) children.item(j);
							GoError err = new GoError(
									Integer.parseInt(e.getAttribute(GoLintResultParser.LINE_ATTRIBUTE)),
									e.getAttribute(GoLintResultParser.MESS_ATTRIBUTE),
									e.getAttribute(GoLintResultParser.SEVER_ATTRIBUTE), "./" + filename);
							listError.add(err);
						}

					}
				}

				return listError;

			} catch (SAXException e) {
				LOGGER.error("SAX Exception", e);
				throw new XMLStreamException(e);
			} catch (IOException e) {
				LOGGER.error("IOException", e);
				throw new XMLStreamException(e);
			}

		}
	}
}

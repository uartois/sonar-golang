package fr.univartois.sonargo.coverage;

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

public class CoverageParser implements Parser {

	private String filepath;
	private List<LineCoverage> listOfCoverage;
	private static final String FILE_NAME_ATTR = "filename";
	private static final String LINE_NUMBER_ATTR = "number";
	private static final String HITS_ATTR = "hits";
	private static final String METHOD_TAG = "method";
	private static final String CLASS_TAG = "class";
	private static final String LINE_TAG = "line";

	public CoverageParser() {
		listOfCoverage = new ArrayList<>();
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
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File(reportPath));

		doc.getDocumentElement().normalize();

		NodeList classList = doc.getElementsByTagName(CLASS_TAG);

		for (int i = 0; i < classList.getLength(); i++) {
			Node nNode = classList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				filepath = eElement.getAttribute(FILE_NAME_ATTR);

				parseMethodTag(eElement.getElementsByTagName(METHOD_TAG));

			}
		}

	}

	private void parseMethodTag(NodeList methodsList) {
		for (int j = 0; j < methodsList.getLength(); j++) {
			Node nNode = methodsList.item(j);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				parseLineTag(eElement.getElementsByTagName(LINE_TAG));
			}
		}
	}

	private void parseLineTag(NodeList lineList) {
		for (int j = 0; j < lineList.getLength(); j++) {
			Node nNode = lineList.item(j);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				listOfCoverage.add(new LineCoverage(Integer.parseInt(eElement.getAttribute(LINE_NUMBER_ATTR)),
						Integer.parseInt(eElement.getAttribute(HITS_ATTR))));
			}
		}
	}

	public String getFilepath() {
		return filepath;
	}

	public List<LineCoverage> getList() {
		return listOfCoverage;
	}

}

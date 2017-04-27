package fr.univartois.sonargo;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public interface Parser {
	public void parse(String reportPath) throws ParserConfigurationException, SAXException, IOException;
}

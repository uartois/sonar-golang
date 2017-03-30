package fr.univartois.sonargo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.ce.measure.Settings;
import org.sonar.api.internal.apachecommons.lang.StringUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GoLintIssueLoaderSensor implements Sensor {
	private static final Logger LOGGER=Loggers.get(GoLintIssueLoaderSensor.class);
	
	protected static final String REPORT_PATH_KEY="sonar.golint.reportPath";
	
	private Settings settings;
	
	
	public GoLintIssueLoaderSensor(Settings se){
		this.settings=se;
	}
	
	public void describe(SensorDescriptor descriptor) {
		descriptor.name("GoMetaLinter issues loader sensor");
	}
	
	
	private String getReportPath(){
		String reportPath=settings.getString(REPORT_PATH_KEY);
		if(!StringUtils.isEmpty(reportPath)){
			return reportPath;
		}
		return null;
	}
	
	public void execute(SensorContext context) {
		String reportPath=getReportPath();
		if(!StringUtils.isEmpty(reportPath)){
			File analyse=new File(reportPath);
			try{
				parseAndSaveResults(analyse);
			}catch(XMLStreamException e){
				throw new IllegalStateException("Unable to parse the provided Golint file",e);
			}
		}
	}
	protected void parseAndSaveResults(final File file) throws XMLStreamException{
		LOGGER.info("Parsing 'GoLint' Analysis Results");
		
		GoLintResultParser parser=new GoLintResultParser();
		
		List<GoFileError> listError=parser.parse(file);
		for(GoFileError e:listError){
			
		}
		
	}
	private class GoLintResultParser{
		
		private static final String COLUMN_ATTRIBUTE="column";
		private static final String LINE_ATTRIBUTE="line";
		private static final String MESS_ATTRIBUTE="message";
		private static final String SEVER_ATTRIBUTE="severity";
		
		public List<GoFileError> parse(File file) throws XMLStreamException{
			LOGGER.info("Parsing file {}",file.getAbsolutePath());
			DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builde;
			List<GoFileError> listFileError=new ArrayList<>();
			try {
				builde = dbFactory.newDocumentBuilder();
				Document doc=builde.parse(file);
				
				NodeList nList = doc.getElementsByTagName("file");
				
				for(int i = 0; i < nList.getLength(); i++){
					Node n=nList.item(i);
					List<GoError> listError=new ArrayList<>();
					String filePath=((Element)nList.item(i)).getAttribute("name");
					NodeList children=n.getChildNodes();
					for(int j=0;j<children.getLength();j++){
						Element e=(Element)children.item(j);
						GoError err=new GoError(e.getAttribute(GoLintResultParser.COLUMN_ATTRIBUTE),
								GoLintResultParser.LINE_ATTRIBUTE,
								GoLintResultParser.MESS_ATTRIBUTE,
								GoLintResultParser.SEVER_ATTRIBUTE);
						listError.add(err);
					}
					
					listFileError.add(new GoFileError(listError, filePath));
				}
				return listFileError;
				
			} catch (ParserConfigurationException e) {
				LOGGER.error("Parser configuration", e);
				throw new XMLStreamException(e);
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

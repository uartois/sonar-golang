package fr.univartois.sonargo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class GoKeyRule{
	public static final String PATH_FILE="config/key.properties";
	private static final Logger LOGGER=Loggers.get(GoKeyRule.class);
	private static Properties prop=new Properties();
	private static void init() {
		try {
			prop.load(new FileInputStream(new File(PATH_FILE)));
		} catch (FileNotFoundException e) {
			LOGGER.error("Unable to load the config file", e);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error("Unable to load the config file", e);
			e.printStackTrace();
		}
	}
	
	public static String getKeyFromDescription(String description) throws IllegalArgumentException{
	
			init();
			Pattern pattern;
			Matcher matcher;
			for(Entry<Object, Object> e : prop.entrySet()) {
	           pattern=Pattern.compile((String) e.getValue());
	           matcher=pattern.matcher(description);
	           if(!matcher.find()) continue;
	           return (String) e.getKey();
	        }
			
			throw new IllegalArgumentException("This description \""+description+"\" is not usable");

	}
}

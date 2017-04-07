package fr.univartois.sonargo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * This class load the file key.properties where there is a map that can
 * correspond lint a internal key with a Pattern for match with the message of
 * the report
 * 
 * @author thibault
 *
 */
public class GoKeyRule {
	public static final String PATH_FILE = "/key.properties";
	private static final Logger LOGGER = Loggers.get(GoKeyRule.class);
	private static Properties prop;

	private GoKeyRule() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Allow to load the key.properties file where there is a map that can
	 * correspond lint a internal key with a Pattern for match with the message
	 * of the report
	 */
	private synchronized static void init() {
		if (prop == null) {
			prop = new Properties();
			try {
				LOGGER.info("Load " + PATH_FILE);

				InputStream input = GoKeyRule.class.getResourceAsStream(PATH_FILE);

				if (input == null) {
					throw new FileNotFoundException(PATH_FILE);
				}

				prop.load(input);
			} catch (IOException e) {
				LOGGER.error("Unable to load the config file", e);
			}
		}
	}

	/**
	 * Allow to get the corresponding key of the message of the error
	 * 
	 * @param error
	 * @return
	 */
	public static String getKeyFromError(GoError error) {
		init();
		Pattern pattern;
		Matcher matcher;
		for (Entry<Object, Object> e : prop.entrySet()) {
			pattern = Pattern.compile((String) e.getValue());
			LOGGER.info((new StringBuilder()).append("Pattern: ").append(e.getValue())
					.append(", error message: ").append(error.getMessage()).toString());
			matcher = pattern.matcher(error.getMessage());
			if (!matcher.find())
				continue;
			return (String) e.getKey();
		}

		LOGGER.warn("This description \"" + error.getMessage() + "\" is not usable");
		return null;

	}
}

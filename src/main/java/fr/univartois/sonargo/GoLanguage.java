package fr.univartois.sonargo;

import org.sonar.api.internal.apachecommons.lang.StringUtils;
import org.sonar.api.resources.AbstractLanguage;

/**
 * This class defines the Golang language
 * @author thibault
 *
 */
public class GoLanguage extends AbstractLanguage{
	
	
	public static final String NAME="GO";
	public static final String KEY="go";
	public GoLanguage(String key, String name) {
		super(KEY, NAME);
	}

	private static final String[] DEFAULT_FILE_SUFFIXES={"go"};
	/**
	 * {@inheritDoc}}
	 */
	public String[] getFileSuffixes() {
		return DEFAULT_FILE_SUFFIXES;
	}
	/**
	 * Allows to know if the given file name has a valid suffix
	 * @param filename String representing the file name
	 * @return boolean <code>true</code> if the file name's suffix is known, <code>false</code> any other way
	 */
	public boolean hasValidSuffixes(String filename){
		for(String str:DEFAULT_FILE_SUFFIXES){
			if(StringUtils.lowerCase(filename).endsWith("."+str)){
				return true;
			}
		}
		return false;
	}
	
	

}

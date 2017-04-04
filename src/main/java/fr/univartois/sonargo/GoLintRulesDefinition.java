package fr.univartois.sonargo;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
/**
 * This class read the "/rules/golint-rules.xml" file and create all the rule than can detect by the Sensor
 * @author thibault
 *
 */
public class GoLintRulesDefinition implements RulesDefinition{
	private static final String PATH_TO_RULES_XML = "/rules/golint-rules.xml";
	private static final Logger LOGGER=Loggers.get(GoLintRulesDefinition.class);	
	  protected static final String KEY = "go";
	  protected static final String NAME = "Go";

	  public static final String REPO_KEY = GoLanguage.KEY + "-" + KEY;
	  protected static final String REPO_NAME = GoLanguage.KEY + "-" + NAME;

	  protected String rulesDefinitionFilePath() {
	    return PATH_TO_RULES_XML;
	  }

	  private void defineRulesForLanguage(Context context, String repositoryKey, String repositoryName, String languageKey) {
	    NewRepository repository = context.createRepository(repositoryKey, languageKey).setName(repositoryName);
	    
	    LOGGER.info("Repository "+repositoryName+" created with the key "+repositoryKey+" "+repository);
	    
	    InputStream rulesXml = this.getClass().getResourceAsStream(rulesDefinitionFilePath());
	    if (rulesXml != null) {
	      RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
	      rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
	    }else{
	    	LOGGER.warn("The file "+PATH_TO_RULES_XML+" is not loading ");
	    }

	    repository.done();
	    
	    LOGGER.info("Repository "+repositoryName+" done: "+repository.rules());
	    
	  }

	  @Override
	  public void define(Context context) {
	    defineRulesForLanguage(context, REPO_KEY, REPO_NAME, GoLanguage.KEY);
	}
}

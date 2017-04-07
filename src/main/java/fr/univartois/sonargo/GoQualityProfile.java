package fr.univartois.sonargo;

import static fr.univartois.sonargo.GoLintRulesDefinition.REPO_KEY;
import static fr.univartois.sonargo.GoLintRulesDefinition.REPO_NAME;

import org.sonar.api.internal.apachecommons.lang.StringUtils;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.graph.StringEdgeFactory;
/**
 * The class define all rules that will detect by the sensor, it's not the same of {@link GoLintRulesDefinition}
 * @author thibault
 *
 */
public final class GoQualityProfile extends ProfileDefinition {
  private static final Logger LOGGER=Loggers.get(GoQualityProfile.class);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
	
	  
	LOGGER.info("Golint Quality profile");  
	RulesProfile profile = RulesProfile.create("Golint Rules", GoLanguage.KEY);
	profile.setDefaultProfile(Boolean.TRUE);

    profile.activateRule(Rule.create(REPO_KEY, "ExportedType",REPO_NAME), null);
    profile.activateRule(Rule.create(REPO_KEY, "ExportedMethod",REPO_NAME), null);
    profile.activateRule(Rule.create(REPO_KEY, "SimplifiedTo",REPO_NAME), null);
    profile.activateRule(Rule.create(REPO_KEY, "UnusedStructField",REPO_NAME), null);
    
    LOGGER.info((new StringBuilder()).append("Profil generate: ").append(profile.getActiveRules()).toString());
    
    return profile;
  }
}
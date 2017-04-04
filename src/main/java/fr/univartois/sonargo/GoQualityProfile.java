package fr.univartois.sonargo;

import static fr.univartois.sonargo.GoLintRulesDefinition.REPO_KEY;
import static fr.univartois.sonargo.GoLintRulesDefinition.REPO_NAME;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
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
    
	RulesProfile profile = RulesProfile.create("Golint Rules", GoLanguage.KEY);
    Rule rule=Rule.create(REPO_KEY, "ExportedType",REPO_NAME);
    
    LOGGER.info("Active the rule ExportedType in "+REPO_KEY);
    LOGGER.info("rule "+rule);

    profile.activateRule(rule, null);
    return profile;
  }
}
package fr.univartois.sonargo;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;


import static org.sonar.api.rules.RulePriority.BLOCKER;
import static org.sonar.api.rules.RulePriority.CRITICAL;
import static org.sonar.api.rules.RulePriority.MAJOR;
import static fr.univartois.sonargo.GoLintRulesDefinition.REPO_KEY;

public final class GoQualityProfile extends ProfileDefinition {

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    RulesProfile profile = RulesProfile.create("Golint Rules", GoLanguage.KEY);
    profile.activateRule(Rule.create(REPO_KEY, "ExportedType"), BLOCKER);
    return profile;
  }
}
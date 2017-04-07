package fr.univartois.sonargo;

import static fr.univartois.sonargo.GoLintRulesDefinition.REPO_KEY;
import static fr.univartois.sonargo.GoLintRulesDefinition.REPO_NAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Map.Entry;

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
	private static final String PROFILE_PATH="/profile.properties";
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RulesProfile createProfile(ValidationMessages validation) {


		LOGGER.info("Golint Quality profile");  
		RulesProfile profile = RulesProfile.create("Golint Rules", GoLanguage.KEY);
		profile.setDefaultProfile(Boolean.TRUE);


		Properties prop=new Properties();
		try {
			prop.load(new FileInputStream(new File(PROFILE_PATH)));

			for (Entry<Object, Object> e : prop.entrySet()) {
				if(Boolean.TRUE.equals(Boolean.parseBoolean((String) e.getValue()))){
					profile.activateRule(Rule.create(REPO_KEY,(String) e.getKey(),REPO_NAME), null);
				}
			}

		}catch (IOException e) {
			LOGGER.error((new StringBuilder()).append("Unable to load ").append(PROFILE_PATH).toString(), e);
		}

		LOGGER.info((new StringBuilder()).append("Profil generate: ").append(profile.getActiveRules()).toString());

		return profile;
	}
}
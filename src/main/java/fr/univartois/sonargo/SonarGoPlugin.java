package fr.univartois.sonargo;

import org.sonar.api.Plugin;

/**
 * This class is the entry point of the plugin
 * @author thibault
 *
 */
public class SonarGoPlugin implements Plugin{

	@Override
	public void define(Context context) {
		
		context.addExtensions(GoLintRulesDefinition.class, GoLintIssueLoaderSensor.class);

		context.addExtensions(GoLanguage.class, GoQualityProfile.class);
		

	}

}

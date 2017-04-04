package fr.univartois.sonargo;

import org.sonar.api.Plugin;
public class SonarGoPlugin implements Plugin{

	@Override
	public void define(Context context) {
		
		context.addExtensions(GoLintRulesDefinition.class, GoLintIssueLoaderSensor.class);

		context.addExtensions(GoLanguage.class, GoQualityProfile.class);
		

	}

}

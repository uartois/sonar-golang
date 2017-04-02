package fr.univartois.sonargo;

import org.sonar.api.Plugin;
public class SonarGoPlugin implements Plugin{

	public void define(Context context) {
		/*context.addExtension(GoLanguage.class);
		context.addExtension(GoLintRulesDefinition.class);
		context.addExtension(GoLintIssueLoaderSensor.class);
		context.addExtension(GoKeyRule.class);
		context.addExtension(GoError.class);
		context.addExtension(GoQualityProfile.class);*/
		
		// tutorial on languages
		context.addExtensions(GoLanguage.class, GoQualityProfile.class);
		
		
		context.addExtensions(GoLintRulesDefinition.class, GoLintIssueLoaderSensor.class);

	}

}

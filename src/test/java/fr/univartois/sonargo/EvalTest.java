package fr.univartois.sonargo;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;

import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.runner.RunWith;

import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;

@RunWith(JUnitReportingRunner.class)
public class EvalTest extends JUnitStories {

	public EvalTest() {
		configuredEmbedder().embedderControls().doGenerateViewAfterStories(false).doIgnoreFailureInStories(false)
				.doIgnoreFailureInView(false).doVerboseFailures(true);
	}

	@Override
	public Configuration configuration() {
		return new MostUsefulConfiguration()
				// CONSOLE reporting
				.useStoryReporterBuilder(new StoryReporterBuilder().withDefaultFormats().withFormats(Format.CONSOLE));
	}

	// Here we specify the steps classes
	@Override
	public InjectableStepsFactory stepsFactory() {
		// varargs, can have more that one steps classes
		return new InstanceStepsFactory(configuration(), new ReportEvalStoryTest());
	}

	@Override
	protected List<String> storyPaths() {
		return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/*.story", "**/excluded*.story");
	}
}
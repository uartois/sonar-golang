package fr.univartois.sonargo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EvalTest.class, GoErrorTest.class, GoLanguageTest.class, GoQualityProfileTest.class,
		GoLintIssueLoaderSendorTest.class, GoLintRulesDefinitionTest.class, GoPluginTest.class,
		GoPropertiesTest.class, })
public class AllTests {
}

package fr.univartois.sonargo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.univartois.sonargo.language.GoLanguageTest;
import fr.univartois.sonargo.language.GoQualityProfileTest;
import fr.univartois.sonargo.rules.EvalTest;
import fr.univartois.sonargo.rules.GoErrorTest;
import fr.univartois.sonargo.rules.GoLintIssueLoaderSendorTest;
import fr.univartois.sonargo.rules.GoLintRulesDefinitionTest;
import fr.univartois.sonargo.settings.GoPropertiesTest;

@RunWith(Suite.class)
@SuiteClasses({ EvalTest.class, GoErrorTest.class, GoLanguageTest.class, GoQualityProfileTest.class,
		GoLintIssueLoaderSendorTest.class, GoLintRulesDefinitionTest.class, GoPluginTest.class,
		GoPropertiesTest.class, })
public class AllTests {
}

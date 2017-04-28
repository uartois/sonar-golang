package fr.univartois.sonargo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

public class GoPluginTest {
	private SonarGoPlugin javaPlugin = new SonarGoPlugin();
	private static final Version VERSION_6_0 = Version.create(6, 0);

	@Test
	public void test() {
		SonarRuntime runtime = SonarRuntimeImpl.forSonarLint(VERSION_6_0);
		Plugin.Context context = new Plugin.Context(runtime);
		javaPlugin.define(context);
		assertEquals(9, context.getExtensions().size());

	}

}

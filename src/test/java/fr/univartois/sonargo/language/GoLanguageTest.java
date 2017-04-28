package fr.univartois.sonargo.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import fr.univartois.sonargo.language.GoLanguage;

public class GoLanguageTest {

	@Test
	public void test() {
		GoLanguage go = new GoLanguage();
		assertTrue(go.hasValidSuffixes("test.go"));
		assertFalse(go.hasValidSuffixes("test.xml"));
		String[] def = { "go" };

		assertEquals(Arrays.asList(def), Arrays.asList(go.getFileSuffixes()));
	}

}

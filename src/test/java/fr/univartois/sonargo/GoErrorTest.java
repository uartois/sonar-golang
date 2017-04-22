package fr.univartois.sonargo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GoErrorTest {

	@Test
	public void testInitAndEqualityGoError() {
		GoError error = new GoError(10, "un message", "warning", "test.go");
		GoError error2 = new GoError(10, "un message", "warning", "test.go");
		GoError error3 = new GoError(10, "un message 2", "warning", "test.go");
		assertEquals(error.hashCode(), error2.hashCode());
		assertTrue(error.equals(error2));
		assertFalse(error.equals(error3));
		assertEquals(10, error.getLine());
		assertEquals("un message", error.getMessage());
		assertEquals("warning", error.getSeverity());
		assertEquals("test.go", error.getFilePath());

		assertEquals("GoError [line=10, message=un message, severity=warning, filePath=test.go]", error.toString());

	}

}

/*******************************************************************************
 * Copyright 2017 - Université d'Artois
 *
 * This file is part of SonarQube Golang plugin (sonar-golang).
 *
 * Sonar-golang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sonar-golang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar-golang.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *            Thibault Falque (thibault_falque@ens.univ-artois.fr)
 *******************************************************************************/
package fr.univartois.sonargo.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.univartois.sonargo.rules.GoError;

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

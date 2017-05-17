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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;

import fr.univartois.sonargo.core.rules.GoLintIssueLoaderSensor;

public class GoLintIssueLoaderSendorTest {
	private DefaultFileSystem fileSystem;
	private GoLintIssueLoaderSensor sensor;

	@Before
	public void setUp() {
		fileSystem = new DefaultFileSystem((File) null);
		sensor = new GoLintIssueLoaderSensor(new Settings(), fileSystem);
	}

	@Test
	public void describe() {
		SensorDescriptor sensorDescriptor = mock(SensorDescriptor.class);

		assertNotNull(sensorDescriptor);
		assertNotNull(sensor);

	}

}

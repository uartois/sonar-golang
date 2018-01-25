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
package fr.univartois.sonargo;

import java.io.File;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;

import fr.univartois.sonargo.core.language.GoLanguage;

public class TestUtils {
    private static final String TEST_RESSOURCES = "src/test/resources";
    private static final String COVERAGE_REPORT_PATH = "coverage";
    private static final String TEST_PATH = "gotest";
    private static final String HIGLIGHTER_CODE_PATH = "highlighter";
    private static final String PROJECT_CODE_PATH = "project";

    public static DefaultFileSystem getDefaultFileSystem() {
	return new DefaultFileSystem(getModuleBaseDir());
    }

    public static DefaultFileSystem getTestBaseDir() {
	DefaultFileSystem fs = new DefaultFileSystem(
		(new File(TEST_RESSOURCES + File.separator + TEST_PATH)).getAbsoluteFile());

	DefaultInputFile f1 = new DefaultInputFile("module", "test1_test.go");
	f1 = f1.setType(InputFile.Type.TEST);
	f1 = f1.setLanguage(GoLanguage.KEY);

	DefaultInputFile f2 = new DefaultInputFile("module", "test_test.go");
	f2 = f2.setType(InputFile.Type.TEST);
	f2 = f2.setLanguage(GoLanguage.KEY);

	fs = fs.add(f1);
	fs = fs.add(f2);

	return fs;
    }

    public static DefaultFileSystem getCoverageBaseDir() {
	return new DefaultFileSystem(new File(TEST_RESSOURCES + File.separator + COVERAGE_REPORT_PATH));
    }

    public static DefaultFileSystem getProjectDir() {
	DefaultFileSystem fs = new DefaultFileSystem(new File(TEST_RESSOURCES + File.separator + PROJECT_CODE_PATH));
	DefaultInputFile f = new DefaultInputFile("module", "package1.go");
	f = f.setType(InputFile.Type.MAIN);
	f = f.setLanguage(GoLanguage.KEY);

	DefaultInputFile f2 = new DefaultInputFile("module", "package1_test.go");
	f2 = f2.setType(InputFile.Type.TEST);
	f2 = f2.setLanguage(GoLanguage.KEY);

	DefaultInputFile f3 = new DefaultInputFile("module", "test.py");
	f3 = f3.setType(InputFile.Type.MAIN);
	f3 = f3.setLanguage("python");

	fs = fs.add(f);
	fs = fs.add(f2);
	fs = fs.add(f3);

	return fs;
    }

    public static DefaultFileSystem getColorizeDir() {
	return new DefaultFileSystem(new File(TEST_RESSOURCES + File.separator + HIGLIGHTER_CODE_PATH));
    }

    public static File getModuleBaseDir() {
	return new File(TEST_RESSOURCES);
    }

}

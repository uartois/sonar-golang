package fr.univartois.sonargo.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;

import fr.univartois.sonargo.AbstractSonarTest;
import fr.univartois.sonargo.TestUtils;
import fr.univartois.sonargo.core.language.GoLanguage;

public class ProjectExplorerTest extends AbstractSonarTest {

    @Before
    @Override
    public void init() {
	init(TestUtils.getProjectDir());
    }

    @Test
    public void testSearchByType() {

	List<InputFile> list = ProjectExplorer.searchByType(testerContext, InputFile.Type.MAIN);
	assertFalse(list.isEmpty());
	assertEquals(1, list.size());
    }

    @Test
    public void testSearchFileWithTypeMainOrTest() {
	List<InputFile> list = ProjectExplorer.searchFileWithTypeMainOrTest(testerContext);
	assertFalse(list.isEmpty());
	assertEquals(2, list.size());
    }

    @Test
    public void testGetByPath() {
	assertNotNull(ProjectExplorer.getByPath(testerContext, "package1.go"));
	assertNotNull(ProjectExplorer.getByPath(testerContext, "package1.go").language());
	assertEquals(GoLanguage.KEY, ProjectExplorer.getByPath(testerContext, "package1.go").language());
    }

}

package fr.univartois.sonargo.core;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

import fr.univartois.sonargo.core.language.GoLanguage;

public class ProjectExplorer {

    private ProjectExplorer() {
	// prevent the instantiation
    }

    public static List<InputFile> searchByType(final SensorContext context, final InputFile.Type type) {
	FileSystem system = context.fileSystem();
	FilePredicates predicate = system.predicates();
	Iterable<InputFile> iter = system
		.inputFiles(predicate.and(predicate.hasLanguage(GoLanguage.KEY), predicate.hasType(type)));

	final List<InputFile> listFiles = new ArrayList<>();
	iter.forEach(listFiles::add);
	return listFiles;
    }

    public static List<InputFile> searchFileWithTypeMainOrTest(final SensorContext context) {
	FilePredicates predicate = context.fileSystem().predicates();
	Iterable<InputFile> iter = context.fileSystem().inputFiles(predicate.and(predicate.hasLanguage(GoLanguage.KEY),
		predicate.or(predicate.hasType(InputFile.Type.MAIN), predicate.hasType(InputFile.Type.TEST))));

	final List<InputFile> listFiles = new ArrayList<>();
	iter.forEach(listFiles::add);
	return listFiles;
    }

    public static InputFile getByPath(final SensorContext context, final String filePath) {
	final FileSystem fileSystem = context.fileSystem();
	final FilePredicates predicates = fileSystem.predicates();
	return fileSystem.inputFile(predicates.hasPath(filePath));

    }

}

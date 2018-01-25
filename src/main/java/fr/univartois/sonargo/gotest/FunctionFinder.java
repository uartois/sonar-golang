package fr.univartois.sonargo.gotest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import fr.univartois.sonargo.core.ProjectExplorer;

public class FunctionFinder {
    private static final Logger LOGGER = Loggers.get(FunctionFinder.class);

    private HashMap<String, String> result = new HashMap<>();
    private String baseDir;
    private List<Path> paths;

    private static final Pattern MATCH_FUNC_NAME = Pattern.compile(
	    "func\\s+(\\(suite\\s+[a-zA-Z0-9\\*\\._-]*\\)\\s+)?(?<functionName>Test[^\\s]+)\\s*\\([a-zA-Z0-9\\*\\s\\.\\,_-]*\\)\\s*\\{");

    public FunctionFinder(SensorContext context) throws IOException {
	this.baseDir = context.fileSystem().baseDir().getPath();

	LOGGER.info("base dir " + baseDir);

	paths = new ArrayList<>();
	ProjectExplorer.searchByType(context, InputFile.Type.TEST).forEach(i -> paths.add(i.file().toPath()));

    }

    public HashMap<String, String> searchFunction() {

	paths.forEach((p) -> {
	    LOGGER.info("search test function in " + p.toFile().getAbsolutePath());
	    searchFunctionInFile(p);
	});
	LOGGER.debug(result.toString());
	return result;
    }

    private CharBuffer getFileAsBufferFromPath(Path p) {
	try (FileInputStream input = new FileInputStream(p.toString())) {
	    FileChannel channel = input.getChannel();
	    ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
	    return Charset.forName("utf8").newDecoder().decode(bbuf);
	} catch (FileNotFoundException e) {
	    LOGGER.warn("IO Exception caught -", e);
	} catch (IOException e) {
	    LOGGER.warn("IO Exception caught -", e);
	}
	return null;
    }

    public void searchFunctionInFile(Path p) {
	String absolutePath = p.toFile().getAbsolutePath();
	Matcher matcher = MATCH_FUNC_NAME.matcher(getFileAsBufferFromPath(p));
	while (matcher.find()) {
	    String func = matcher.group("functionName");
	    LOGGER.debug("Found function " + func + " at path " + absolutePath);
	    if (func != null && absolutePath != null) {
		result.put(func, absolutePath);
	    }
	}
    }

    public HashMap<String, String> getResult() {
	return result;
    }
}

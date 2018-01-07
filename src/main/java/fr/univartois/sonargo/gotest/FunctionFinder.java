package fr.univartois.sonargo.gotest;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class FunctionFinder {
	private static final Logger LOGGER = Loggers.get(FunctionFinder.class);

	private HashMap<String, String> result = new HashMap<>();
	private String baseDir;
	private Stream<Path> paths;

	private static final Pattern MATCH_FUNC_NAME = Pattern.compile(
			"func\\s+(\\([^\\s)]+\\s+[a-zA-Z0-9\\*\\._-]*\\)\\s+)?(?<functionName>Test[^\\s]+)\\s*\\([a-zA-Z0-9\\*\\s\\.\\,_-]*\\)\\s*\\{");

	public FunctionFinder(SensorContext context) throws IOException {
		this.baseDir = context.fileSystem().baseDir().getPath();

		LOGGER.info("base dir " + baseDir);

		paths = Files.walk(Paths.get(baseDir)).filter(p -> p.toFile().getName().endsWith("_test.go"));
	}

	public HashMap<String, String> searchFunction() {
		paths.forEach((p) -> {
			LOGGER.info("search test function in " + p.toFile().getAbsolutePath());
			searchFunctionInFile(p);
		});
		LOGGER.debug(result.toString());
		return result;
	}

	private CharBuffer getFileAsBufferFromPath(Path p) throws IOException {
		FileInputStream input = new FileInputStream(p.toString());
		FileChannel channel = input.getChannel();
		ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
		return Charset.forName("utf8").newDecoder().decode(bbuf);
	}

	public void searchFunctionInFile(Path p) {
		String absolutePath = p.toFile().getAbsolutePath();

		System.err.println("search function in file " + absolutePath);

		try {
			Matcher matcher = MATCH_FUNC_NAME.matcher(getFileAsBufferFromPath(p));
			while (matcher.find()) {
				String func = matcher.group("functionName");
				LOGGER.debug("Found function " + func + " at path " + absolutePath);
				if (func != null && absolutePath != null) {
					result.put(func, absolutePath);
				}
			}
		} catch (IOException e) {
			LOGGER.warn("IO Exception caught -", e);
		}
	}

	public HashMap<String, String> getResult() {
		return result;
	}
}

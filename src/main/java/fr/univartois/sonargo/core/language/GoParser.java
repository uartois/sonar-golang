package fr.univartois.sonargo.core.language;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;

public final class GoParser {

	private static final Parser<Grammar> P = GoParser.create();

	private GoParser() {
	}

	public static Parser<Grammar> create() {
		return Parser.builder(GoGrammar.create()).withLexer(GoLexer.create()).build();
	}

	public static AstNode parseFile(String filePath) {
		final File file = FileUtils.toFile(GoParser.class.getResource(filePath));
		if (file == null || !file.exists()) {
			throw new AssertionError("The file \"" + filePath + "\" does not exist.");
		}

		return P.parse(file);
	}

	public static AstNode parseString(String source) {
		return P.parse(source);
	}

}

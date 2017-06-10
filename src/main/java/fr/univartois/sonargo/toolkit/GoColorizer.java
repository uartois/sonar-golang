package fr.univartois.sonargo.toolkit;

import java.util.Arrays;
import java.util.List;

import org.sonar.colorizer.CDocTokenizer;
import org.sonar.colorizer.CppDocTokenizer;
import org.sonar.colorizer.KeywordsTokenizer;
import org.sonar.colorizer.Tokenizer;

import fr.univartois.sonargo.core.language.GoLexer;
import fr.univartois.sonargo.core.language.GoLexer.Keyword;

public final class GoColorizer {

	private GoColorizer() {
	}

	public static List<Tokenizer> getTokenizers() {
		return Arrays.asList(new CDocTokenizer("<span class=\"cd\">", "</span>"),
				new CppDocTokenizer("<span class=\"cppd\">", "</span>"),
				new KeywordsTokenizer("<span class=\"k\">", "</span>", GoLexer.Keyword.keywordValues()));
	}

}
package fr.univartois.sonargo.core.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.sonar.sslr.tests.ParserAssert;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Rule;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.Parser;
import com.sonar.sslr.impl.matcher.RuleDefinition;

import fr.univartois.sonargo.coverage.CoverageSensor;

public class GoGrammarTest {
	private Rule rule;
	private Parser<Grammar> parser;
	private ArrayList<String> goCodes;

	@Before
	public void init() {
		Lexer lexer = GoLexer.create();
		rule = new RuleDefinition("ruleName").is("foo");
		Grammar grammar = GoGrammar.create();
		parser = Parser.builder(grammar).withLexer(lexer).build();
		goCodes = new ArrayList<>();
		try {
			parseGoCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseGoCode() throws IOException {
		Files.list(new File(CoverageSensor.class.getResource("/grammar").getFile()).toPath()).forEach(path -> {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
				String sCurrentLine;
				StringBuilder sb = new StringBuilder();
				while ((sCurrentLine = reader.readLine()) != null) {
					sb.append(sCurrentLine + "\n");
				}
				goCodes.add(sb.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	@Test
	public void ok() {
		for (String s : goCodes) {
			System.out.println(s);
			new ParserAssert(parser).matches(s);
		}
	}

}

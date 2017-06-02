package fr.univartois.sonargo.core.language;

import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static com.sonar.sslr.test.lexer.LexerMatchers.hasToken;
import static fr.univartois.sonargo.core.language.GoLexer.Literals.FLOAT;
import static fr.univartois.sonargo.core.language.GoLexer.Literals.INTEGER;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.sonar.sslr.impl.Lexer;

public class GoLexerTest {
    Lexer lexer = GoLexer.create();
    private Pattern pattern;
    private Matcher matcher;

    @Test
    public void lexIdentifiers() {
	assertThat(lexer.lex("abc"), hasToken("abc", IDENTIFIER));
	assertThat(lexer.lex("abc0"), hasToken("abc0", IDENTIFIER));
	assertThat(lexer.lex("abc_0"), hasToken("abc_0", IDENTIFIER));
	assertThat(lexer.lex("i"), hasToken("i", IDENTIFIER));
    }

    @Test
    public void lexKeyword() {
	assertThat(lexer.lex("int"), hasToken("int", GoLexer.Keyword.INT));
	assertThat(lexer.lex("int32"), hasToken("int32", GoLexer.Keyword.INT32));
	assertThat(lexer.lex("int64"), hasToken("int64", GoLexer.Keyword.INT64));
    }

    @Test
    public void lexIntegers() {

	assertThat(lexer.lex("01"), hasToken("01", INTEGER));
	assertThat(lexer.lex("1"), hasToken("1", INTEGER));
	assertThat(lexer.lex("0x1F"), hasToken("0x1F", INTEGER));
	assertThat(lexer.lex("0x1e"), hasToken("0x1e", INTEGER));
    }

    @Test
    public void lexFloat() {

	final String[] array = { "0.", "72.40", "072.40", "2.71828", "1.e+0", "6.67428e-11", "1E6", ".25", ".12345E+5"

	};

	pattern = Pattern.compile(GoLexer.Literals.FLOAT.getValue());
	matcher = pattern.matcher("1.1");
	assertTrue(matcher.matches());
	matcher = pattern.matcher("1.0");
	assertTrue(matcher.matches());
	matcher = pattern.matcher("1.0e+10");
	assertTrue(matcher.matches());
	matcher = pattern.matcher("1.0e-10");
	assertTrue(matcher.matches());
	matcher = pattern.matcher("1.0E-10");
	assertTrue(matcher.matches());
	matcher = pattern.matcher("1.0E+10");
	assertTrue(matcher.matches());
	matcher = pattern.matcher("1e-10");
	assertTrue(matcher.matches());
	matcher = pattern.matcher("1e+10");
	assertTrue(matcher.matches());
	matcher = pattern.matcher(".1e+10");
	assertTrue(matcher.matches());
	matcher = pattern.matcher(".1e-10");
	assertTrue(matcher.matches());
	matcher = pattern.matcher(".1");
	assertTrue(matcher.matches());

	for (final String s : array) {
	    matcher = pattern.matcher(s);
	    assertTrue(matcher.matches());
	}

	assertThat(lexer.lex("1.0"), hasToken("1.0", FLOAT));

    }

}

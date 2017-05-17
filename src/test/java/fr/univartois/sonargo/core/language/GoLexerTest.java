package fr.univartois.sonargo.core.language;

import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static com.sonar.sslr.test.lexer.LexerMatchers.hasToken;
import static fr.univartois.sonargo.core.language.GoLexer.Literals.INTEGER;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.sonar.sslr.impl.Lexer;

public class GoLexerTest {
    Lexer lexer = GoLexer.create();

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

}

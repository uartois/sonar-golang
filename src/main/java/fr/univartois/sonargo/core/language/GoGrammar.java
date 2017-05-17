package fr.univartois.sonargo.core.language;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.BOOL;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.BYTE;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.COMPLEX128;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.COMPLEX64;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.FLOAT32;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.FLOAT64;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.INT;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.INT16;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.INT32;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.INT64;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.INT8;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.RUNE;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.STRING;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.UINT16;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.UINT32;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.UINT64;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.UINT8;
import static fr.univartois.sonargo.core.language.GoLexer.Keyword.UINTPTR;
import static fr.univartois.sonargo.core.language.GoLexer.Punctuators.COMMA;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

import com.sonar.sslr.api.Grammar;

public enum GoGrammar implements GrammarRuleKey {
    BIN_TYPE,
    TYPE,
    QUALIFIED_IDENT,
    PACKAGE_NAME,
    IMPORT_SIMPLE,
    IMPORT_COMPLEX,
    IMPORT_DEFINITION,
    PACKAGE_DEFINITION,
    VAR_DEFINITION,
    DEFINITION,
    IDENTIFIER_LIST,
    COMPILATION_UNIT;
    public static Grammar create() {

	final LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();

	b.rule(PACKAGE_NAME).is(IDENTIFIER);
	b.rule(QUALIFIED_IDENT).is(PACKAGE_NAME, GoLexer.Punctuators.DOT, IDENTIFIER);
	b.rule(BIN_TYPE).is(b.firstOf(INT, UINT16, UINT64, UINT8, UINT32, FLOAT32, FLOAT64, INT16, INT32, INT8, INT64,
		STRING, RUNE, BOOL, BYTE, UINTPTR, COMPLEX64, COMPLEX128));

	b.rule(TYPE).is(BIN_TYPE);

	b.rule(IDENTIFIER_LIST).is(IDENTIFIER, b.optional(b.oneOrMore(COMMA, IDENTIFIER)));

	b.rule(VAR_DEFINITION).is(GoLexer.Keyword.VAR, IDENTIFIER_LIST, TYPE);
	b.rule(PACKAGE_DEFINITION).is(GoLexer.Keyword.PACKAGE, PACKAGE_NAME);

	b.rule(IMPORT_SIMPLE).is(GoLexer.Keyword.IMPORT, GoLexer.Literals.STRING);
	b.rule(IMPORT_COMPLEX).is(GoLexer.Keyword.IMPORT, GoLexer.Punctuators.PAREN_L,
		b.oneOrMore(GoLexer.Literals.STRING), GoLexer.Punctuators.PAREN_R);
	b.rule(IMPORT_DEFINITION).is(b.firstOf(IMPORT_SIMPLE, IMPORT_COMPLEX));

	b.rule(DEFINITION).is(b.firstOf(PACKAGE_DEFINITION, IMPORT_DEFINITION, VAR_DEFINITION));

	b.rule(COMPILATION_UNIT).is(b.zeroOrMore(DEFINITION), EOF);

	b.setRootRule(COMPILATION_UNIT);
	return b.build();
    }
}

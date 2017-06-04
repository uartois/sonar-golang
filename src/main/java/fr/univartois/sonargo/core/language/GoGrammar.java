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
    TYPE_NAME,
    POINTER_TYPE,
    TYPE,
    QUALIFIED_IDENT,
    PACKAGE_NAME,
    IMPORT_SIMPLE,
    IMPORT_COMPLEX,
    IMPORT_DEFINITION,
    PACKAGE_DEFINITION,
    VAR_DEFINITION,
    FIELD_DEFINITION,
    FIELD_DEFINITION_NORMAL,
    FIELD_DEFINITION_ANOM,
    STRUCT_DEFINITION,
    FUNC_DEFINITION,
    SIGNATURE,
    RESULT,
    PARAMETERS,
    PARAMETERS_LIST,
    PARAMETERS_DECLARATION,
    DEFINITION,
    IDENTIFIER_LIST,
    COMPILATION_UNIT,
    TAG;
    public static Grammar create() {

	final LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();
	b.rule(TAG).is(GoLexer.Literals.STRING);

	b.rule(PACKAGE_NAME).is(IDENTIFIER);
	b.rule(QUALIFIED_IDENT).is(PACKAGE_NAME, GoLexer.Punctuators.DOT, IDENTIFIER);
	b.rule(BIN_TYPE).is(b.firstOf(INT, UINT16, UINT64, UINT8, UINT32, FLOAT32, FLOAT64, INT16, INT32, INT8, INT64,
		STRING, RUNE, BOOL, BYTE, UINTPTR, COMPLEX64, COMPLEX128));

	b.rule(TYPE_NAME).is(b.firstOf(IDENTIFIER, QUALIFIED_IDENT));
	b.rule(POINTER_TYPE).is(GoLexer.Punctuators.MUL, TYPE);
	b.rule(TYPE).is(b.firstOf(BIN_TYPE, POINTER_TYPE, TYPE_NAME));

	b.rule(IDENTIFIER_LIST).is(IDENTIFIER, b.optional(b.oneOrMore(COMMA, IDENTIFIER)));

	b.rule(FIELD_DEFINITION_NORMAL).is(IDENTIFIER_LIST, TYPE);
	b.rule(FIELD_DEFINITION_ANOM).is(b.optional(GoLexer.Punctuators.MUL), TYPE_NAME);
	b.rule(FIELD_DEFINITION).is(b.firstOf(FIELD_DEFINITION_NORMAL, FIELD_DEFINITION_ANOM), b.optional(TAG));
	b.rule(STRUCT_DEFINITION).is(GoLexer.Keyword.STRUCT, GoLexer.Punctuators.BRACE_L,
		b.zeroOrMore(FIELD_DEFINITION), GoLexer.Punctuators.BRACE_R);

	b.rule(PARAMETERS_DECLARATION).is(b.optional(IDENTIFIER_LIST), TYPE);
	b.rule(PARAMETERS_LIST).is(PARAMETERS_DECLARATION,
		b.optional(b.oneOrMore(GoLexer.Punctuators.COMMA, PARAMETERS_DECLARATION)));
	b.rule(PARAMETERS).is(GoLexer.Punctuators.PAREN_L,
		b.optional(PARAMETERS_LIST, b.optional(b.oneOrMore(GoLexer.Punctuators.COMMA, PARAMETERS_LIST))),
		GoLexer.Punctuators.PAREN_R);
	b.rule(RESULT).is(b.firstOf(PARAMETERS, TYPE));
	b.rule(SIGNATURE).is(PARAMETERS, b.optional(RESULT));
	b.rule(FUNC_DEFINITION).is(GoLexer.Keyword.FUNC, SIGNATURE);

	b.rule(VAR_DEFINITION).is(GoLexer.Keyword.VAR, IDENTIFIER_LIST, TYPE, b.optional(GoLexer.Punctuators.EQ,
		b.firstOf(GoLexer.Literals.STRING, GoLexer.Literals.FLOAT, GoLexer.Literals.INTEGER)));

	b.rule(PACKAGE_DEFINITION).is(GoLexer.Keyword.PACKAGE, PACKAGE_NAME);

	b.rule(IMPORT_SIMPLE).is(GoLexer.Keyword.IMPORT, GoLexer.Literals.STRING);
	b.rule(IMPORT_COMPLEX).is(GoLexer.Keyword.IMPORT, GoLexer.Punctuators.PAREN_L,
		b.oneOrMore(GoLexer.Literals.STRING), GoLexer.Punctuators.PAREN_R);
	b.rule(IMPORT_DEFINITION).is(b.firstOf(IMPORT_SIMPLE, IMPORT_COMPLEX));

	b.rule(DEFINITION).is(
		b.firstOf(PACKAGE_DEFINITION, IMPORT_DEFINITION, VAR_DEFINITION, STRUCT_DEFINITION, FUNC_DEFINITION));

	b.rule(COMPILATION_UNIT).is(b.zeroOrMore(DEFINITION), EOF);

	b.setRootRule(COMPILATION_UNIT);
	return b.build();
    }
}

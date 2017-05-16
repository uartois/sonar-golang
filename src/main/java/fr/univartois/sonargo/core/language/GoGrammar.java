package fr.univartois.sonargo.core.language;

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

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

import com.sonar.sslr.api.Grammar;

public enum GoGrammar implements GrammarRuleKey {
    BIN_TYPE;
    public static Grammar create() {

	final LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();
	b.rule(BIN_TYPE).is(INT, UINT16, UINT64, UINT8, UINT32, FLOAT32, FLOAT64, INT16, INT32, INT8, INT64, STRING,
		RUNE, BOOL, BYTE, UINTPTR, COMPLEX64, COMPLEX128);

	return b.build();
    }
}

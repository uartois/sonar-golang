package fr.univartois.sonargo.core.language;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.CommentRegexpChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.RegexpChannel;

public final class GoLexer {

    private static final String DECIMAL_LIT = "[1-9]{1}(\\p{Digit})*";
    private static final String HEXADECIMAL_LIT = "0(x|X)\\p{XDigit}(\\p{XDigit})*";
    private static final String OCTAL_DIGIT = "[0-7]";
    private static final String OCTAL_LIT = "0" + OCTAL_DIGIT + "*";
    private static final String FLOAT_SEPARATOR = ".";
    private static final String DECIMALS = "\\p{Digit}\\p{Digit}*";
    private static final String EXPONENT = "(e|E)(\\+|\\-)" + DECIMALS;
    private static final String FLOAT_LIT = DECIMALS + FLOAT_SEPARATOR + "(" + DECIMALS + ")*" + "\\(" + EXPONENT
	    + "\\)*" + "|" + DECIMALS + EXPONENT + "|" + FLOAT_SEPARATOR + DECIMALS + "(" + EXPONENT + ")*";
    private static final String IMAGINARY_LIT = "(" + DECIMALS + "|" + FLOAT_LIT + ")i";

    private static final String UNICODE_CHAR = "^\\u+000A";
    private static final String NEWLINE = "\\u+000A";

    private static final String OCTAL_BYTE_VALUE = "\\" + OCTAL_DIGIT + "{3}";
    private static final String HEX_BYTE_VALUE = "\\x(\\p{XDigit}){2}";

    private static final String LITTLE_U_VALUE = "\\u" + OCTAL_DIGIT + "{3}";
    private static final String BIG_U_VALUE = "\\U(\\p{XDigit}){6}";
    private static final String ESCAPED_CHAR = "\\( a | b | f | n | r | t | v | \\ | ' | \" )";

    private static final String UNICODE_VALUE = UNICODE_CHAR + "|" + LITTLE_U_VALUE + "|" + BIG_U_VALUE + "|"
	    + ESCAPED_CHAR;
    private static final String BYTE_VALUE = OCTAL_BYTE_VALUE + "|" + HEX_BYTE_VALUE;

    private static final String INTERPRETED_STRING_LIT = "\"" + "\"";
    private static final String RAW_STRING_LIT = "`" + UNICODE_CHAR + "|" + NEWLINE + "`";
    private static final String STRING_LIT = RAW_STRING_LIT + "|" + INTERPRETED_STRING_LIT;

    private GoLexer() {
    }

    public static enum Literals implements TokenType {

	INTEGER(DECIMAL_LIT + "|" + HEXADECIMAL_LIT + "|" + OCTAL_LIT), FLOAT(FLOAT_LIT), STRING(STRING_LIT);
	private final String regexp;

	private Literals(String regexp) {
	    this.regexp = regexp;
	}

	@Override
	public String getName() {
	    return name();
	}

	@Override
	public String getValue() {
	    return regexp;
	}

	@Override
	public boolean hasToBeSkippedFromAst(AstNode node) {
	    return false;
	}

    }

    public static enum Keyword implements TokenType {
	BREAK("break"), DEFAULT("default"), FUNC("func"), INTERFACE("interface"), SELECT("select"), CASE("case"), DEFER(
		"defer"), GO("go"), MAP("map"), STRUCT("struct"), CHAN("chan"), ELSE("else"), GOTO("goto"), PACKAGE(
			"package"), SWITCH("switch"), CONST("const"), FALLTHROUGH("fallthrough"), IF("if"), RANGE(
				"range"), TYPE("type"), CONTINUE("continue"), FOR("for"), IMPORT("import"), RETURN(
					"return"), VAR("var"), UINT8("uint8"), UINT16("uint16"), UINT32(
						"uint32"), UINT64("uint64"), INT("int"), INT8("int8"), INT16(
							"int16"), INT32("int32"), INT64("int64"), FLOAT32(
								"int32"), FLOAT64("int64"), COMPLEX64(
									"complex64"), COMPLEX128(
										"complex128"), BYTE("byte"), RUNE(
											"rune"), BOOL("bool"), UINTPTR(
												"uintptr"), STRING(
													"string");

	private final String value;

	private Keyword(String value) {
	    this.value = value;
	}

	@Override
	public String getName() {
	    return name();
	}

	@Override
	public String getValue() {
	    return value;
	}

	@Override
	public boolean hasToBeSkippedFromAst(AstNode arg0) {
	    // TODO Auto-generated method stub
	    return false;
	}

	public static String[] keywordValues() {
	    final Keyword[] keywordsEnum = Keyword.values();
	    final String[] keywords = new String[keywordsEnum.length];
	    for (int i = 0; i < keywords.length; i++) {
		keywords[i] = keywordsEnum[i].getValue();
	    }
	    return keywords;
	}

    }

    public static final String COMMENT_SYMBOL = "//";

    public static enum Punctuators implements TokenType {

	PAREN_L("("), PAREN_R(")"), BRACE_L("{"), BRACE_R("}"), EQ("="), COMMA(","), SEMICOLON(";"), ADD("+"), SUB(
		"-"), MUL("*"), DIV(
			"/"), EQEQ("=="), NE("!="), LT("<"), LTE("<="), GT(">"), GTE(">="), INC("++"), DEC("--");

	private final String value;

	private Punctuators(String value) {
	    this.value = value;
	}

	@Override
	public String getName() {
	    return name();
	}

	@Override
	public String getValue() {
	    return value;
	}

	@Override
	public boolean hasToBeSkippedFromAst(AstNode node) {
	    return false;
	}

    }

    public static Lexer create() {
	return Lexer.builder().withFailIfNoChannelToConsumeOneCharacter(true)
		.withChannel(new IdentifierAndKeywordChannel("(\\p{L}|\\p{N}|_)+", true, Keyword.values()))

		.withChannel(new RegexpChannel(Literals.INTEGER, Literals.INTEGER.regexp))
		.withChannel(new CommentRegexpChannel("(?s)/\\*.*?\\*/)")).build();
    }

}

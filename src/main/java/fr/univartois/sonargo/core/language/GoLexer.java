package fr.univartois.sonargo.core.language;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.CommentRegexpChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;

public final class GoLexer {

	private static final String DECIMAL_LIT = "[1-9]{1}(\\p{Digit})*";
	private static final String HEXADECIMAL_LIT = "0(x|X)\\p{XDigit}(\\p{XDigit})*";
	private static final String OCTAL_LIT = "0[0-7]*";
	private static final String FLOAT_SEPARATOR = ".";
	private static final String DECIMALS = "\\p{Digit}\\p{Digit}*";
	private static final String EXPONENT = "(e|E)(\\+|\\-)" + DECIMALS;

	private GoLexer() {
	}

	public static enum Literals implements TokenType {

		INTEGER(DECIMAL_LIT + "|" + HEXADECIMAL_LIT + "|" + OCTAL_LIT), FLOAT(
				DECIMALS + FLOAT_SEPARATOR + "(" + DECIMALS + ")*" + "\\(" + EXPONENT + "\\)*" + "|" + DECIMALS
						+ EXPONENT + "|" + FLOAT_SEPARATOR + DECIMALS + "(" + EXPONENT + ")*");
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
				.withChannel(
						new IdentifierAndKeywordChannel("[a-zA-Z]([a-zA-Z0-9_]*[a-zA-Z0-9])?+", true, Keyword.values()))
				.withChannel(new CommentRegexpChannel("(?s)/\\*.*?\\*/)")).build();
	}

}

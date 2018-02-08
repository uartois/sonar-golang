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
    GO_TYPE,
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
    STRUCT_TYPE,
    FUNC_TYPE,
    SIGNATURE,
    RESULT,
    PARAMETERS,
    PARAMETERS_LIST,
    PARAMETERS_DECLARATION,
    DEFINITION,
    IDENTIFIER_LIST,
    COMPILATION_UNIT,
    VAR_FAST_DEFINITION,
    TAG,
    VAR_DECLARATION,
    CONST_DECLARATION,
    DECLARATION,
    EXPR_LIST,
    EXPRESSION,
    UNARY_EXPR,
    BINARY_OP,
    PRIMARY_EXPR,
    REL_OP,
    ADD_OP,
    MUL_OP,
    UNARY_OP,
    OPERAND,
    SLICE,
    INDEX,
    CONVERSION,
    TYPE_ASSERTION,
    ARGUMENTS,
    SELECTOR,
    TYPE_LIT,
    INTERFACE_TYPE,
    ARRAY_TYPE,
    MAP_TYPE,
    CHANNEL_TYPE,
    SLICE_TYPE,
    RECEIVER_TYPE,
    COMPOSITE_LIT,
    LITERAL_TYPE,
    LITERAL_VALUE,
    ELEMENT_LIST,
    ELEMENT,
    FIELD_NAME,
    KEY,
    KEYED_ELEMENT,
    METHOD_EXPR,
    LITERAL,
    BASIC_LIT,
    FUNC_LIT,
    ELEMENT_TYPE;
    private static final LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();

    public static Grammar create() {

	b.rule(TAG).is(GoLexer.Literals.STRING);
	createCommonExpression();
	createPackageDefinition();
	createImport();
	createAllGoType();
	createOthersType();
	createStructDefinition();
	createFunctionType();
	createAllDeclaration();

	b.rule(DEFINITION).is(b.firstOf(PACKAGE_DEFINITION, IMPORT_DEFINITION, DECLARATION, STRUCT_TYPE, FUNC_TYPE));

	b.rule(COMPILATION_UNIT).is(b.zeroOrMore(DEFINITION), EOF);

	b.setRootRule(COMPILATION_UNIT);
	return b.build();
    }

    /**
     * @see https://golang.org/ref/spec#Import_declarations
     */
    private static void createImport() {
	b.rule(IMPORT_SIMPLE).is(GoLexer.Keyword.IMPORT, GoLexer.Literals.STRING);
	b.rule(IMPORT_COMPLEX).is(GoLexer.Keyword.IMPORT, GoLexer.Punctuators.PAREN_L,
		b.oneOrMore(GoLexer.Literals.STRING), GoLexer.Punctuators.PAREN_R);
	b.rule(IMPORT_DEFINITION).is(b.firstOf(IMPORT_SIMPLE, IMPORT_COMPLEX));
    }

    /**
     * https://golang.org/ref/spec#Package_clause
     */
    private static void createPackageDefinition() {
	b.rule(PACKAGE_NAME).is(IDENTIFIER);
	b.rule(PACKAGE_DEFINITION).is(GoLexer.Keyword.PACKAGE, PACKAGE_NAME);
    }

    /**
     * @see https://golang.org/ref/spec#Variables
     * @see https://golang.org/ref/spec#Short_variable_declarations
     */
    private static void createVarDeclaration() {

	Object varSpec = b.firstOf(IDENTIFIER_LIST,
		b.firstOf(b.sequence(TYPE, b.optional(GoLexer.Punctuators.EQ, EXPR_LIST)),
			b.sequence(GoLexer.Punctuators.EQ, EXPR_LIST)));

	b.rule(VAR_DEFINITION).is(GoLexer.Keyword.VAR, b.firstOf(varSpec,
		b.sequence(GoLexer.Punctuators.PAREN_R, b.zeroOrMore(varSpec), GoLexer.Punctuators.PAREN_L)));

	b.rule(VAR_FAST_DEFINITION).is(IDENTIFIER_LIST, GoLexer.Punctuators.EQ2, EXPR_LIST);
	b.rule(VAR_DECLARATION).is(b.firstOf(VAR_DEFINITION, VAR_FAST_DEFINITION));
    }

    /**
     * @see https://golang.org/ref/spec#Function_types
     */
    private static void createFunctionType() {
	b.rule(PARAMETERS_DECLARATION).is(b.optional(IDENTIFIER_LIST), TYPE);
	b.rule(PARAMETERS_LIST).is(PARAMETERS_DECLARATION,
		b.optional(b.oneOrMore(GoLexer.Punctuators.COMMA, PARAMETERS_DECLARATION)));
	b.rule(PARAMETERS).is(GoLexer.Punctuators.PAREN_L,
		b.optional(PARAMETERS_LIST, b.optional(b.oneOrMore(GoLexer.Punctuators.COMMA, PARAMETERS_LIST))),
		GoLexer.Punctuators.PAREN_R);
	b.rule(RESULT).is(b.firstOf(PARAMETERS, TYPE));
	b.rule(SIGNATURE).is(PARAMETERS, b.optional(RESULT));
	b.rule(FUNC_TYPE).is(GoLexer.Keyword.FUNC, SIGNATURE);
    }

    /**
     * @see https://golang.org/ref/spec#Function_declarations
     */
    private static void createFunctionDefinition() {
	// not implemented for the moment
    }

    private static void createIdentifierList() {
	b.rule(IDENTIFIER_LIST).is(IDENTIFIER, b.optional(b.oneOrMore(COMMA, IDENTIFIER)));
    }

    private static void creatOp() {

	b.rule(REL_OP).is(GoLexer.Punctuators.EQEQ, GoLexer.Punctuators.NE, GoLexer.Punctuators.GT,
		GoLexer.Punctuators.GTE, GoLexer.Punctuators.LT, GoLexer.Punctuators.LTE);

	b.rule(ADD_OP).is(b.firstOf(GoLexer.Punctuators.ADD, GoLexer.Punctuators.SUB, GoLexer.Punctuators.OR_BITBIT,
		GoLexer.Punctuators.OR_BITBIT));

	b.rule(MUL_OP)
		.is(b.firstOf(GoLexer.Punctuators.MUL, GoLexer.Punctuators.DIV, GoLexer.Punctuators.PERCENT,
			GoLexer.Punctuators.LEFT_SHIFT, GoLexer.Punctuators.RIGHT_SHIFT, GoLexer.Punctuators.AND_BITBIT,
			GoLexer.Punctuators.AND_NOT_BITBIT));

	b.rule(UNARY_OP).is(GoLexer.Punctuators.ADD, GoLexer.Punctuators.SUB, GoLexer.Punctuators.NOT,
		GoLexer.Punctuators.XOR, GoLexer.Punctuators.MUL, GoLexer.Punctuators.AND_BITBIT,
		GoLexer.Punctuators.LEFT);

	b.rule(BINARY_OP).is(b.firstOf(GoLexer.Punctuators.OR, GoLexer.Punctuators.AND, REL_OP, ADD_OP, MUL_OP));
    }

    /**
     * @see https://golang.org/ref/spec#Expression
     */
    private static void createExpr() {

	b.rule(SELECTOR).is(GoLexer.Punctuators.DOT, IDENTIFIER);
	b.rule(INDEX).is(GoLexer.Punctuators.SQUARE_BRACKET_RIGHT, EXPRESSION, GoLexer.Punctuators.SQUARE_BRACKET_LEFT);
	b.rule(SLICE)
		.is(b.firstOf(b.sequence(GoLexer.Punctuators.SQUARE_BRACKET_RIGHT, b.optional(EXPRESSION),
			GoLexer.Punctuators.COLON, b.optional(EXPRESSION), GoLexer.Punctuators.SQUARE_BRACKET_LEFT),
			b.sequence(b.optional(EXPRESSION), GoLexer.Punctuators.COLON, EXPRESSION,
				GoLexer.Punctuators.COLON, EXPRESSION)));
	b.rule(TYPE_ASSERTION).is(GoLexer.Punctuators.DOT, GoLexer.Punctuators.PAREN_R, TYPE,
		GoLexer.Punctuators.PAREN_L);

	b.rule(ARGUMENTS).is(GoLexer.Punctuators.PAREN_R,
		b.optional(b.sequence(
			b.firstOf(EXPR_LIST, b.sequence(TYPE, b.optional(GoLexer.Punctuators.COMMA, EXPR_LIST))),
			b.optional(GoLexer.Punctuators.DOT3), b.optional(GoLexer.Punctuators.COMMA))),
		GoLexer.Punctuators.PAREN_L);

	b.rule(CONVERSION).is(TYPE, GoLexer.Punctuators.PAREN_R, EXPRESSION, b.optional(GoLexer.Punctuators.COMMA),
		GoLexer.Punctuators.PAREN_L);

	b.rule(BASIC_LIT).is(b.firstOf(GoLexer.Literals.FLOAT, GoLexer.Literals.INTEGER, GoLexer.Literals.STRING));

	Object operandName = b.firstOf(IDENTIFIER, QUALIFIED_IDENT);

	createComposeLit();
	createMethodExpr();
	createFuncLit();

	b.rule(LITERAL).is(b.firstOf(BASIC_LIT, COMPOSITE_LIT, FUNC_LIT));

	b.rule(OPERAND).is(b.firstOf(LITERAL, operandName, METHOD_EXPR,
		b.sequence(GoLexer.Punctuators.PAREN_R, EXPRESSION, GoLexer.Punctuators.PAREN_R)));

	b.rule(PRIMARY_EXPR)
		.is(b.firstOf(OPERAND, CONVERSION, b.sequence(PRIMARY_EXPR, SELECTOR), b.sequence(PRIMARY_EXPR, INDEX),
			b.sequence(PRIMARY_EXPR, SLICE), b.sequence(PRIMARY_EXPR, TYPE_ASSERTION),
			b.sequence(PRIMARY_EXPR, ARGUMENTS)));

	b.rule(UNARY_EXPR).is(b.firstOf(PRIMARY_EXPR, b.sequence(UNARY_OP, UNARY_EXPR)));
	b.rule(EXPRESSION).is(b.firstOf(UNARY_EXPR, b.sequence(EXPRESSION, BINARY_OP, EXPRESSION)));
    }

    /**
     * @see https://golang.org/ref/spec#Method_expressions
     *      MethodName==Identifier
     */
    private static void createMethodExpr() {
	b.rule(RECEIVER_TYPE)
		.is(b.firstOf(TYPE_NAME,
			b.sequence(GoLexer.Punctuators.PAREN_R, TYPE_NAME, GoLexer.Punctuators.PAREN_L),
			b.sequence(GoLexer.Punctuators.PAREN_R, RECEIVER_TYPE, GoLexer.Punctuators.PAREN_L)));
	b.rule(METHOD_EXPR).is(b.sequence(RECEIVER_TYPE, GoLexer.Punctuators.DOT, IDENTIFIER));
    }

    /**
     * @see https://golang.org/ref/spec#CompositeLit
     */
    private static void createComposeLit() {
	b.rule(FIELD_NAME).is(IDENTIFIER);
	b.rule(KEY).is(b.firstOf(FIELD_NAME, EXPRESSION, LITERAL_VALUE));
	b.rule(ELEMENT).is(b.firstOf(EXPRESSION, LITERAL_VALUE));
	b.rule(KEYED_ELEMENT).is(b.sequence(b.optional(b.sequence(KEY, GoLexer.Punctuators.COLON)), ELEMENT));
	b.rule(ELEMENT_LIST).is(b.sequence(KEYED_ELEMENT, b.zeroOrMore(GoLexer.Punctuators.COMMA, KEYED_ELEMENT)));
	b.rule(LITERAL_VALUE).is(GoLexer.Punctuators.BRACE_R, b.optional(
		b.sequence(ELEMENT_LIST, b.optional(GoLexer.Punctuators.COMMA)), GoLexer.Punctuators.BRACE_L));

	b.rule(ELEMENT_TYPE).is(TYPE);
	b.rule(LITERAL_TYPE)
		.is(b.firstOf(STRUCT_TYPE, ARRAY_TYPE, SLICE_TYPE, MAP_TYPE, TYPE_NAME,
			b.sequence(GoLexer.Punctuators.SQUARE_BRACKET_RIGHT, GoLexer.Punctuators.DOT3,
				GoLexer.Punctuators.SQUARE_BRACKET_LEFT, ELEMENT_TYPE)));

	b.rule(COMPOSITE_LIT).is(LITERAL_TYPE, LITERAL_VALUE);
    }

    /**
     * @see https://golang.org/ref/spec#FunctionLit
     */
    private static Object createFuncLit() {
	return null;
    }

    private static void createExprList() {

	b.rule(EXPR_LIST).is(EXPRESSION, b.optional(COMMA), EXPRESSION);
    }

    /**
     * @see https://golang.org/ref/spec#Expression
     * @see https://golang.org/ref/spec#ExpressionList
     */
    private static void createCommonExpression() {

	createIdentifierList();
	creatOp();
	createExpr();
	createExprList();

    }

    /**
     * @see https://golang.org/ref/spec#Declarations_and_scope
     */
    private static void createAllDeclaration() {
	constDeclaration();
	createVarDeclaration();
	b.rule(DECLARATION).is(VAR_DECLARATION);
    }

    /**
     * https://golang.org/ref/spec#Constant_declarations
     */
    private static void constDeclaration() {

	Object constSpec = b.sequence(IDENTIFIER_LIST,
		b.optional(b.sequence(b.optional(TYPE), GoLexer.Punctuators.EQ), EXPR_LIST));

	Object manyConstSpec = b.sequence(GoLexer.Punctuators.PAREN_R, b.zeroOrMore(constSpec),
		GoLexer.Punctuators.PAREN_L);

	b.rule(CONST_DECLARATION).is(GoLexer.Keyword.CONST, b.firstOf(constSpec, manyConstSpec));
    }

    private static void createOthersType() {
	b.rule(QUALIFIED_IDENT).is(PACKAGE_NAME, GoLexer.Punctuators.DOT, IDENTIFIER);
	b.rule(TYPE_NAME).is(b.firstOf(IDENTIFIER, QUALIFIED_IDENT));
	b.rule(POINTER_TYPE).is(GoLexer.Punctuators.MUL, TYPE);
	b.rule(TYPE_LIT).is(b.firstOf(ARRAY_TYPE, STRUCT_TYPE, POINTER_TYPE, FUNC_TYPE, INTERFACE_TYPE, SLICE_TYPE,
		MAP_TYPE, CHANNEL_TYPE));
	b.rule(TYPE).is(b.firstOf(GO_TYPE, TYPE_LIT, TYPE_NAME,
		b.sequence(GoLexer.Punctuators.PAREN_R, TYPE, GoLexer.Punctuators.PAREN_L)));
    }

    /**
     * @see https://golang.org/ref/spec#Struct_types
     */
    private static void createStructDefinition() {

	b.rule(FIELD_DEFINITION_NORMAL).is(IDENTIFIER_LIST, TYPE);
	b.rule(FIELD_DEFINITION_ANOM).is(b.optional(GoLexer.Punctuators.MUL), TYPE_NAME);
	b.rule(FIELD_DEFINITION).is(b.firstOf(FIELD_DEFINITION_NORMAL, FIELD_DEFINITION_ANOM), b.optional(TAG));
	b.rule(STRUCT_TYPE).is(GoLexer.Keyword.STRUCT, GoLexer.Punctuators.BRACE_L, b.zeroOrMore(FIELD_DEFINITION),
		GoLexer.Punctuators.BRACE_R);

    }

    private static void createAllGoType() {
	b.rule(GO_TYPE).is(b.firstOf(INT, UINT16, UINT64, UINT8, UINT32, FLOAT32, FLOAT64, INT16, INT32, INT8, INT64,
		STRING, RUNE, BOOL, BYTE, UINTPTR, COMPLEX64, COMPLEX128));
    }

}

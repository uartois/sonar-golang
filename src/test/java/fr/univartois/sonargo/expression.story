Some expression violation

Narrative:
Our plugin should be able to 
find a key for message. 

Scenario: find key of message

Given Given a Key finder

When the expression entered is <expression>
Then the result should be <result>

Examples:

|expression  |result|
|exported type Foo should have comment or be unexported|ExportedType|
|comment on exported type Foo should be of the form "Foo ..."|FormComment|
|comment on exported type Foo should be of the form "Foo..."|FormComment|
|comment on exported method Foo.Bar should be of the form "Foo.Bar ..."|FormComment|
|don't use ALL_CAPS in Go names; use CamelCase|AllCaps|
|don't use an underscore in package name|UnderscoreInPackageName|
|don't use leading k in Go names; var kFoo should be Foo|LeadingK|
|don't use underscores in Go names; var foo_bar should be fooBar|UnderscoreInGoName|
|error should be the last type when returning multiple items|ErrorLastReturn|
|error strings should not be capitalized or end with punctuation or a newline|ErrorEndString|
|error var err should have name of the form errFo|ErrorVarName|
|exported method FooMethod returns unexported type unexportedType, which can be annoying to use|AnnoyingUseExportedType|
|if block ends with a return statement, so drop this else and outdent its block|IfBlockReturn|
|package comment should be of the form "Package..."|PackageCommentForm|
|package comment should not have leading space|PackageCommentSpace|
|receiver name a should be consistent with previous receiver name b for bar|ReceiverNameConsistent|
|receiver name should be a reflection of its identity; don't use generic names such as "this" or "self"|ReceiverNameReflection|
|receiver name should not be an underscore|ReceiverNameNotUnderscore|
|should drop = 0 from declaration of var myZeroInt; it is the zero value|DropAssignment|
|should drop = nil from declaration of var myPtr; it is the zero value|DropAssignment|
|should have a package comment, unless it's in another file for this package|HavePackageComment|
|should not use basic type int8 as key in context.WithValue|NotUseBasicType|
|should not use basic type int16 as key in context.WithValue|NotUseBasicType|
|should not use basic type int32 as key in context.WithValue|NotUseBasicType|
|should not use basic type uint8 as key in context.WithValue|NotUseBasicType|
|should not use basic type uintptr as key in context.WithValue|NotUseBasicType|
|should not use dot imports|NotUseDotImport|
|should omit 2nd value from range; this loop is equivalent to `for x := range ...`|Omit2ndValueFromRange|
|should omit type int from declaration of var myZeroInt; it will be inferred from the right-hand side|OmitType|
|should replace x=x+1 with x++|ReplaceLintIncDec|
|should replace t.Errorf(fmt.Sprintf(...)) with t.Errorf(...)|ReplaceSprintf|
|var Serverhttp should be ServerHTTP|Initialisms|
|func name will be used as foo.FooMulti by other packages, and that stutters; consider calling this Multi|PackageNames|
|a blank import should be only in a main or test package, or have a comment justifying it|BlankImport|
|comment on exported type Foo should be of the form "Foo ..." (with optional leading article)|CommentExportedType|
|context.Context should be the first parameter of a function|ContextContextFirstArg|
|exported method FooMethod should have comment or be unexported|ExportedHaveComment|
|exported const FooType should have comment (or a comment on this block) or be unexported|ExportedHaveComment2|
|exported const Hello4 should have its own declaration|ExportedDeclaration|//more info https://github.com/go-lang-plugin-org/go-lang-idea-plugin/issues/2049
|var timeoutSecs is of type time.Second; don't use unit-specific suffix Secs|SpecificSuffix|
|a message error not found|null|
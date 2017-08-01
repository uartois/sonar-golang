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
|exported type Foo should have comment or be unexported|golint:ExportedHaveComment|
|comment on exported type Foo should be of the form "Foo ..."|golint:FormComment|
|comment on exported type Foo should be of the form "Foo..."|golint:FormComment|
|comment on exported method Foo.Bar should be of the form "Foo.Bar ..."|golint:FormComment|
|don't use ALL_CAPS in Go names; use CamelCase|golint:AllCaps|
|don't use an underscore in package name|golint:UnderscoreInPackageName|
|don't use leading k in Go names; var kFoo should be Foo|golint:LeadingK|
|don't use underscores in Go names; var foo_bar should be fooBar|golint:UnderscoreInGoName|
|error should be the last type when returning multiple items|golint:ErrorLastReturn|
|error strings should not be capitalized or end with punctuation or a newline|golint:ErrorEndString|
|error var err should have name of the form errFo|golint:ErrorVarName|
|exported method FooMethod returns unexported type unexportedType, which can be annoying to use|golint:AnnoyingUseExportedType|
|if block ends with a return statement, so drop this else and outdent its block|golint:IfBlockReturn|
|package comment should be of the form "Package..."|golint:PackageCommentForm|
|package comment should not have leading space|golint:PackageCommentSpace|
|receiver name a should be consistent with previous receiver name b for bar|golint:ReceiverNameConsistent|
|receiver name should be a reflection of its identity; don't use generic names such as "this" or "self"|golint:ReceiverNameReflection|
|receiver name should not be an underscore|golint:ReceiverNameNotUnderscore|
|should drop = 0 from declaration of var myZeroInt; it is the zero value|golint:DropAssignment|
|should drop = nil from declaration of var myPtr; it is the zero value|golint:DropAssignment|
|should have a package comment, unless it's in another file for this package|golint:HavePackageComment|
|should not use basic type int8 as key in context.WithValue|golint:NotUseBasicType|
|should not use basic type int16 as key in context.WithValue|golint:NotUseBasicType|
|should not use basic type int32 as key in context.WithValue|golint:NotUseBasicType|
|should not use basic type uint8 as key in context.WithValue|golint:NotUseBasicType|
|should not use basic type uintptr as key in context.WithValue|golint:NotUseBasicType|
|should not use dot imports|golint:NotUseDotImport|
|should omit 2nd value from range; this loop is equivalent to `for x := range ...`|golint:Omit2ndValueFromRange|
|should omit type int from declaration of var myZeroInt; it will be inferred from the right-hand side|golint:OmitType|
|should replace x=x+1 with x++|golint:ReplaceLintIncDec|
|should replace t.Errorf(fmt.Sprintf(...)) with t.Errorf(...)|golint:ReplaceSprintf|
|var Serverhttp should be ServerHTTP|golint:Initialisms|
|func name will be used as foo.FooMulti by other packages, and that stutters; consider calling this Multi|golint:PackageNames|
|a blank import should be only in a main or test package, or have a comment justifying it|golint:BlankImport|
|comment on exported type Foo should be of the form "Foo ..." (with optional leading article)|golint:CommentExportedType|
|context.Context should be the first parameter of a function|golint:ContextContextFirstArg|
|exported method FooMethod should have comment or be unexported|golint:ExportedHaveComment|
|exported const FooType should have comment (or a comment on this block) or be unexported|golint:ExportedHaveComment2|
|exported const Hello4 should have its own declaration|golint:ExportedDeclaration|//more info https://github.com/go-lang-plugin-org/go-lang-idea-plugin/issues/2049
|var timeoutSecs is of type time.Second; don't use unit-specific suffix Secs|golint:SpecificSuffix|
|cyclomatic complexity 11 of function main() is high (> 10)|gocyclo:CyclomaticComplexity|
|error return value not checked (os.MkdirAll("./tmp/build", os.FileMode(0755)))|errcheck:ValueNotChecked|
|error return value not checked (util.Save(jpegimage.(*image.NRGBA), "png_"+nameWithoutExtension+".png"))|errcheck:ValueNotChecked|
|error return value not checked (route(r))|errcheck:ValueNotChecked|
|error return value not checked (core.NewConfiguration(ConfigName, ConfigPath).ReadInConfig())|errcheck:ValueNotChecked|
|unnecessary conversion|unconvert:UnnecessaryConversion|
|unused struct field projectblackwhitego.ImageService.req|structcheck:UnusedStructField|
|a message error not found|null|
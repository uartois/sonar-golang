var test int = 5
var test2 float32 = 8.6
var test3 string = "a string"

var test4 int
var test5 float64
var test6 string


// https://golang.org/ref/spec#Variables

var x interface{}  // x is nil and has static type interface{}
var v *T           // v has value nil, static type *T
x = 42             // x has value 42 and dynamic type int
x = v              // x has value (*T)(nil) and dynamic type *T

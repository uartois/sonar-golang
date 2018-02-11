/*
Exemple from: https://golang.org/doc/effective_go.html#commentary
Package regexp implements a simple library for regular expressions.

The syntax of the regular expressions accepted is:

    regexp:
        concatenation { '|' concatenation }
    concatenation:
        { closure }
    closure:
        term [ '*' | '+' | '?' ]
    term:
        '^'
        '$'
        '.'
        character
        '[' [ '^' ] character-ranges ']'
        '(' regexp ')'
*/
package regexp

import (	
	"fmt"
)
/* Compile parses a regular expression and returns, if successful,
 a Regexp that can be used to match against text.
*/
func Compile(str string) (*Regexp, error) {func main() {
	foo()
	fmt.Println("vim-go")
}


package test

import (
	"github.com/stretchr/testify/assert"
)

func IAmNotATest(t *testing.T) {
	// do something
}

func TestEasyDef(t *testing.T) {
	assert.True(t, true, "Should match")
}

func TestSpaceDef (t *testing.T) {
	assert.True(t, true, "Should match")
}

func TestNoSpaceDef(t *testing.T){
	assert.True(t, true, "Should match")
}

func TestTwoLines1 (t *testing.T) 
{
	assert.True(t, true, "Should match")
}

func 
TestTwoLines2 (t *testing.T) {
	assert.True(t, true, "Should match")
}

func TestNested(t *testing.T) {
	t.Parallel()
	t.Run("match", func(t *testing.T) {
		assert.True(t, true, "Should match")
	})
	t.Run("nomatch", func(t *testing.T) {
		assert.True(t, true, "Should match")
	})
}

func (suite *ExampleTestSuite) TestSuite() {
    suite.Equal(5, suite.VariableThatShouldStartAtFive)
}

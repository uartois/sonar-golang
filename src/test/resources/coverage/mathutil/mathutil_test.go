package mathutil

import (
	"math"
	"testing"
)

func TestSqrtUint32(t *testing.T) {
	result1 := SqrtUint32(4)
	result2 := math.Sqrt(4.0)

	if uint32(result2) != result1 {
		t.Error(
			"For 4 and 4.0",
			"expected ", 2,
			"got", result1,
		)
	}
}

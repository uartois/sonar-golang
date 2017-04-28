package util

import "testing"

func TestSkipped2(t *testing.T) {
	t.SkipNow()
}

func TestFailure(t *testing.T) {
	t.FailNow()
}

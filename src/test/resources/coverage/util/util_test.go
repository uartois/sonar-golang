package util

import (
	"log"
	"testing"
)

func TestSkipped(t *testing.T) {
	t.SkipNow()
}

func TestFalseSquare(t *testing.T) {
	out := falseSquare(5)

	if out != 25 {
		t.Error(
			"For 5",
			"expected", "25",
			"got", out,
		)
	}

}

func TestMin(t *testing.T) {
	out := Min(5, 9)

	if out != 5 {
		t.Error(
			"For 5,9",
			"expected", "5",
			"got", out,
		)
	}

}

func TestMax(t *testing.T) {
	out := Max(5, 9)

	if out != 9 {
		t.Error(
			"For 5,9",
			"expected", "9",
			"got", out,
		)
	}

}

func TestCompare(t *testing.T) {
	img1, err := OpenPng("../imagetest/Image47.png")
	if err != nil {
		log.Fatal("erreur test compare")
		return
	}

	count := CompareImage(img1, img1)

	if count != 0 {
		t.Error(
			"For Image47.png",
			"expected 0",
			"got", count,
		)
	}
}

func TestCompare2(t *testing.T) {
	img1, err := OpenPng("../imagetest/Image47.png")
	img2, err2 := OpenPng("../imagetest/Image47.png")

	if err != nil || err2 != nil {
		log.Fatal(err, " ", err2)
		return
	}

	count := CompareImage(img1, img2)

	if count != 0 {
		t.Error(
			"For Image47.png",
			"expected 0",
			"got", count,
		)
	}
}

package main

import "math"

//SqrtUint32 Permet de faire une racine carré sur un uint32
func SqrtUint32(x uint32) uint32 {
	return uint32(math.Ceil(math.Sqrt(float64(x))))
}

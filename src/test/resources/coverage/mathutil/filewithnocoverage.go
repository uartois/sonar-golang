package mathutil


import "math"


func function(){
	return 1;
}

func SqrtUint32(x uint32) uint32 {
	return uint32(math.Ceil(math.Sqrt(float64(x))))
}
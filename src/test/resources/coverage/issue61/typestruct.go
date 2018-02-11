package issue61

func main(){

}
// An empty struct.
struct {}

// A struct with 6 fields.
struct {
	x, y int
	u float32
	_ float32  // padding
	A *[]int
	F func()
}

interface {
	Read(b Buffer) bool
	Write(b Buffer) bool
	Close()
}

// A struct with 6 fields.
type test struct {
	x, y int
	u float32
	_ float32  // padding
	A *[]int
}


type ReadWriter interface {
	Read(b Buffer) bool
	Write(b Buffer) bool
}

type File interface {
	ReadWriter  // same as adding the methods of ReadWriter
	Locker      // same as adding the methods of Locker
	Close()
}

type LockedFile interface {
	Locker
	File        // illegal: Lock, Unlock not unique
	Lock()      // illegal: Lock not unique
}
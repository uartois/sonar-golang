package util

import (
	"errors"
	"image"
	"image/gif"
	"image/jpeg"
	"image/png"
	"io"
	"os"
	"path/filepath"
	"strings"

	"golang.org/x/image/bmp"
	"golang.org/x/image/tiff"
)

// Format is an image file format.
type Format int

// Image file formats.
const (
	JPEG Format = iota
	PNG
	GIF
	TIFF
	BMP
)

func (f Format) String() string {
	switch f {
	case JPEG:
		return "JPEG"
	case PNG:
		return "PNG"
	case GIF:
		return "GIF"
	case TIFF:
		return "TIFF"
	case BMP:
		return "BMP"
	default:
		return "Unsupported"
	}
}

var (
	// ErrUnsupportedFormat means the given image format (or file extension) is unsupported.
	ErrUnsupportedFormat = errors.New("imaging: unsupported image format")
)

func falseSquare(a int) int {
	return a
}

//Min retourne le min
func Min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

//Max retourne le max
func Max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func Uint32toUint8(val uint32) uint8 {
	return uint8(val >> 8)
}

//CompareImage calcule le nombre de pixel de différence entre deux images
func CompareImage(img1 *image.Image, img2 *image.Image) int {

	if img1 == nil || img2 == nil {
		return -1
	}

	w := (*img1).Bounds().Dx()
	h := (*img1).Bounds().Dy()
	var count int
	for y := 0; y < h; y++ {
		for x := 0; x < w; x++ {
			r, g, b, _ := (*img1).At(x, y).RGBA()
			r2, g2, b2, _ := (*img2).At(x, y).RGBA()
			if r != r2 || g != g2 || b != b2 {
				count++
			}
		}
	}
	return count
}

//OpenPng permet d'ouvrir un PNG
func OpenPng(name string) (*image.Image, error) {
	f, err := os.Open(name)
	if err != nil {
		return nil, err
	}
	in, err2 := png.Decode(f)
	if err2 != nil {
		return nil, err2
	}

	return &in, nil
}

func IsInBoundaries(img *image.NRGBA, x, y int) bool {
	return x >= 0 && x < (*img).Bounds().Dx() && y >= 0 && y < (*img).Bounds().Dy()
}

// //WritePng permet d'écrire un PNG
// func WritePng(img *image.NRGBA, name string) {
// 	fout, err := os.OpenFile(name, os.O_WRONLY|os.O_CREATE, 0600)
// 	if err != nil {
// 		log.Fatal(err)
// 	}
// 	defer fout.Close()
//
// 	if err := png.Encode(fout, img); err != nil {
// 		fout.Close()
// 		log.Fatal(err)
// 	}
//
// 	if err := fout.Close(); err != nil {
// 		log.Fatal(err)
// 	}
// }

//WritePng permet d'écrire un PNG
func WriteImage(w io.Writer, img image.Image, format Format) error {
	var err error
	switch format {
	case JPEG:
		var rgba *image.RGBA
		if nrgba, ok := img.(*image.NRGBA); ok {
			if nrgba.Opaque() {
				rgba = &image.RGBA{
					Pix:    nrgba.Pix,
					Stride: nrgba.Stride,
					Rect:   nrgba.Rect,
				}
			}
		}
		if rgba != nil {
			err = jpeg.Encode(w, rgba, &jpeg.Options{Quality: 95})
		} else {
			err = jpeg.Encode(w, img, &jpeg.Options{Quality: 95})
		}

	case PNG:
		err = png.Encode(w, img)
	case GIF:
		err = gif.Encode(w, img, &gif.Options{NumColors: 256})
	case TIFF:
		err = tiff.Encode(w, img, &tiff.Options{Compression: tiff.Deflate, Predictor: true})
	case BMP:
		err = bmp.Encode(w, img)
	default:
		err = ErrUnsupportedFormat
	}
	return err
}

// Save saves the image to file with the specified filename.
// The format is determined from the filename extension: "jpg" (or "jpeg"), "png", "gif", "tif" (or "tiff") and "bmp" are supported.
func Save(img image.Image, filename string) (err error) {
	formats := map[string]Format{
		".jpg":  JPEG,
		".jpeg": JPEG,
		".png":  PNG,
		".tif":  TIFF,
		".tiff": TIFF,
		".bmp":  BMP,
		".gif":  GIF,
	}

	ext := strings.ToLower(filepath.Ext(filename))
	f, ok := formats[ext]
	if !ok {
		return ErrUnsupportedFormat
	}

	file, err := os.Create(filename)
	if err != nil {
		return err
	}
	defer file.Close()

	return WriteImage(file, img, f)
}

func Exists(path string) (bool, error) {
	_, err := os.Stat(path)
	if err == nil {
		return true, nil
	}
	if os.IsNotExist(err) {
		return false, nil
	}
	return true, err
}

func RemoveContents(dir string) error {
	d, err := os.Open(dir)
	if err != nil {
		return err
	}
	defer d.Close()
	names, err := d.Readdirnames(-1)
	if err != nil {
		return err
	}
	for _, name := range names {
		err = os.RemoveAll(filepath.Join(dir, name))
		if err != nil {
			return err
		}
	}
	return nil
}

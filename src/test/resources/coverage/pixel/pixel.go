package pixel

import (
	"image"
	"image/color"
	"projectblackwhitego/util"
)

type Pixel struct {
	R uint32
	G uint32
	B uint32
	A uint32
}

type PixelImage struct {
	imageRGBA *image.Image
}

func NewPixel(r, g, b, a uint32) *Pixel {
	return &Pixel{r, g, b, a}
}
func NewPixelImage(bounds image.Rectangle) *PixelImage {
	img := image.Image(image.NewNRGBA(bounds))
	return &PixelImage{&img}
}
func (pi *PixelImage) Set(x, y int, pix *Pixel) {
	(*pi.imageRGBA).(*image.NRGBA).Set(x, y, color.NRGBA{
		util.Uint32toUint8(pix.R),
		util.Uint32toUint8(pix.G),
		util.Uint32toUint8(pix.B),
		util.Uint32toUint8(pix.A),
	})
}

func (pi *PixelImage) GetImage() *image.Image {
	return pi.imageRGBA
}

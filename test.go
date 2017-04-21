package effect

import (
	"fmt"
	"image"
	"image/color"
	"math"
	"projectblackwhitego/convolution"
	"projectblackwhitego/mathutil"
	"projectblackwhitego/pixel"
	"projectblackwhitego/util"
	"runtime"
	"sort"
	"sync"
	"time"

	"github.com/disintegration/gift"
)

type SobelFilter struct {
	Hfilter *convolution.Filter
	Vfilter *convolution.Filter
}

type BilateralFilter struct {
	kernel             [][]float64
	kernelRadius       float64
	gaussPreCalculated []float64
	sigmaD             float64
	sigmaR             float64
	twoSigmaRSquared   float64
}

var sobelFilter = SobelFilter{
	Hfilter: &convolution.Filter{
		Masque: [][]int{
			{-1, 0, +1},
			{-2, 0, +2},
			{-1, 0, +1},
		},
		SizeX: 3,
		SizeY: 3,
	},
	Vfilter: &convolution.Filter{
		Masque: [][]int{
			{+1, +2, +1},
			{0, 0, 0},
			{-1, -2, -1},
		},
		SizeX: 3,
		SizeY: 3,
	},
}

//Sobel Përmet d'appliquer un filter de sobel
func Sobel(img *image.Image) *image.Image {
	imgh := convolution.Convolution(img, sobelFilter.Hfilter)
	imgv := convolution.Convolution(img, sobelFilter.Vfilter)
	imageRGBA := pixel.NewPixelImage((*img).Bounds())
	for y := 0; y < (*img).Bounds().Dy(); y++ {
		for x := 0; x < (*img).Bounds().Dx(); x++ {
			pixelh := pixel.NewPixel(imgh.At(x, y).RGBA())
			pixelv := pixel.NewPixel(imgv.At(x, y).RGBA())

			// sumV := (pixelv.R + pixelv.G + pixelv.B + pixelv.A) / 4
			// sumH := (pixelh.R + pixelh.G + pixelh.B + pixelh.A) / 4
			r := mathutil.SqrtUint32(pixelh.R*pixelh.R + pixelv.R*pixelv.R)
			g := mathutil.SqrtUint32(pixelh.G*pixelh.G + pixelv.G*pixelv.G)
			b := mathutil.SqrtUint32(pixelh.B*pixelh.B + pixelv.B*pixelv.B)

			// res := mathutil.SqrtUint32(sumV*sumV + sumH*sumH)
			_, _, _, a := imgh.At(x, y).RGBA()
			imageRGBA.Set(x, y, pixel.NewPixel(r, g, b, a))
		}
	}

	//util.WritePng(imgv, "testv.png")
	//util.WritePng(imgh, "testh.png")
	return imageRGBA.GetImage()
}

func Median(img image.Image) *image.Image {
	imageRGBA := pixel.NewPixelImage(img.Bounds())
	for y := 0; y < img.Bounds().Dy(); y++ {
		for x := 0; x < img.Bounds().Dx(); x++ {
			var tabRed []int
			var tabGreen []int
			var tabBlue []int

			for i := -3; i <= 3; i++ {
				for j := -3; j <= 3; j++ {
					r, g, b, _ := img.At(x+i, y+j).RGBA()

					tabRed = append(tabRed, int(r))
					tabGreen = append(tabGreen, int(g))
					tabBlue = append(tabBlue, int(b))
				}
			}

			sort.Sort(sort.IntSlice(tabRed))
			sort.Sort(sort.IntSlice(tabGreen))
			sort.Sort(sort.IntSlice(tabBlue))

			medianRed := uint32(tabRed[(len(tabRed)+1)/2])
			medianGreen := uint32(tabGreen[(len(tabGreen)+1)/2])
			medianBlue := uint32(tabBlue[(len(tabBlue)+1)/2])

			_, _, _, a := img.At(x, y).RGBA()
			imageRGBA.Set(x, y, pixel.NewPixel(medianRed, medianGreen, medianBlue, a))
		}

	}
	return imageRGBA.GetImage()
}

func gauss(sigma float64, x int, y int) float64 {

	return math.Exp(float64(float64(-(x*x + y*y)) / float64(2*sigma*sigma)))
}

func fillParallel1(tab []float64, twosigma float64) {
	routines := runtime.NumCPU()
	workers := (routines / 2)
	step := len(tab) / workers
	var wg sync.WaitGroup
	for i := 0; i < workers; i++ {
		sli := tab[i*step : (i*step)+step]
		wg.Add(1)
		go fillWg(sli, float64(i*step), float64((i*step)+step), twosigma, &wg)
	}
	wg.Wait()
}

func fillWg(tab []float64, imin float64, imax float64, twosigma float64, wg *sync.WaitGroup) {
	start := imin
	for i := range tab {
		tab[i] = math.Exp(-(float64(start*start) / twosigma))
		start++
	}
	wg.Done()
}

func NewBilateralFilter(sigmaD float64, sigmaR float64) *BilateralFilter {
	sigmaMax := math.Max(sigmaD, sigmaR)
	kernelRadius := int(math.Ceil(2 * sigmaMax))
	kernelSize := kernelRadius*2 + 1
	center := (kernelSize - 1) / 2

	kernel := make([][]float64, int(kernelSize))

	for x := -center; x < -center+kernelSize; x++ {
		kernel[int(x+center)] = make([]float64, int(kernelSize))
		for y := -center; y < -center+kernelSize; y++ {
			kernel[int(x+center)][int(y+center)] = gauss(sigmaD, x, y)
		}
	}

	twoSigmaRSquared := 2 * sigmaR * sigmaR

	gaussSimilarity := make([]float64, 256)
	fillParallel1(gaussSimilarity, twoSigmaRSquared)

	//for i := 0; i < 256; i++ {
	//gaussSimilarity[i] = math.Exp(-(float64(i*i) / twoSigmaRSquared))
	//}

	return &BilateralFilter{
		kernel:             kernel,
		kernelRadius:       float64(kernelRadius),
		gaussPreCalculated: gaussSimilarity,
		sigmaD:             sigmaD,
		sigmaR:             sigmaR,
		twoSigmaRSquared:   twoSigmaRSquared,
	}
}

func (b *BilateralFilter) similarity(p int, s int) float64 {
	return b.gaussPreCalculated[int(math.Abs(float64(p-s)))]
}
func (b *BilateralFilter) getKernelRadius() float64 {
	return b.kernelRadius
}
func (b *BilateralFilter) getKernel() [][]float64 {
	return b.kernel
}

func applyBilateralFilter(b *BilateralFilter, i int, j int, colorPixel *color.Color, img *image.NRGBA) (uint32, uint32, uint32) {

	sumR := 0.0
	sumG := 0.0
	sumB := 0.0

	totalWeightR := 0.0
	totalWeightG := 0.0
	totalWeightB := 0.0

	mMax := i + int(b.kernelRadius)
	nMax := j + int(b.kernelRadius)
	var weightR float64
	var weightG float64
	var weightB float64

	kernel := b.getKernel()
	rc, gc, bc, _ := (*colorPixel).RGBA()

	for m := int(i - int(b.kernelRadius)); m <= mMax; m++ {
		for n := int(j - int(b.kernelRadius)); n <= nMax; n++ {

			if util.IsInBoundaries(img, m, n) {
				rk, gk, bk, _ := (*img).At(m, n).RGBA()

				kernelValue := kernel[i-m+int(b.kernelRadius)][j-n+int(b.kernelRadius)]

				weightR = kernelValue * b.similarity(int(rk>>8), int(rc>>8))
				totalWeightR += weightR
				sumR += (weightR * float64(rk))

				weightG = kernelValue * b.similarity(int(gk>>8), int(gc>>8))
				totalWeightG += weightG
				sumG += (weightG * float64(gk))

				weightB = kernelValue * b.similarity(int(bk>>8), int(bc>>8))
				totalWeightB += weightB
				sumB += (weightB * float64(bk))
			}
		}
	}
	return uint32(math.Floor(sumR / totalWeightR)), uint32(math.Floor(sumG / totalWeightG)), uint32(math.Floor(sumB / totalWeightB))
}

func BilateralParralel(img *image.NRGBA, startx, starty, width, height int, pixelImage *pixel.PixelImage, bilateral *BilateralFilter, first bool, wg *sync.WaitGroup) {
	var color color.Color

	for y := starty; y < height; y++ {
		for x := startx; x < width; x++ {
			if first {
				color = (*img).At(x, y)
			} else {
				i := pixelImage.GetImage()
				color = (*i).At(x, y)
			}
			_, _, _, a := (*img).At(x, y).RGBA()
			resR, resG, resB := applyBilateralFilter(bilateral, x, y, &color, img)

			pixelImage.Set(x, y, pixel.NewPixel(resR, resG, resB, a))
		}
	}

	wg.Done()
}

func Bilateral(img *image.Image) image.Image {

	gift1 := gift.New(gift.Resize((*img).Bounds().Dx()/4, (*img).Bounds().Dy()/4, gift.LanczosResampling))

	rectResize := image.Rectangle{image.Point{0, 0}, image.Point{(*img).Bounds().Dx() / 4, (*img).Bounds().Dy() / 4}}

	imgageResize := image.NewNRGBA(rectResize)

	gift1.Draw(imgageResize, *img)
	imageRGBA := pixel.NewPixelImage((*imgageResize).Bounds())

	routines := runtime.NumCPU()
	workers := (routines / 2)
	var wg sync.WaitGroup

	d := time.Now()
	bilateral := NewBilateralFilter(7.0, 7.0)
	fmt.Println(time.Since(d))
	stepWidth := (*imgageResize).Bounds().Dx() / workers
	stepHeight := (*imgageResize).Bounds().Dy() / workers

	var first bool
	for i := 0; i < 14; i++ {

		if i == 0 {
			first = true
		} else {
			first = false
		}

		for wh := 0; wh < workers; wh++ {
			for ww := 0; ww < workers; ww++ {
				wg.Add(1)
				go BilateralParralel(imgageResize, ww*stepWidth, wh*stepHeight, (ww*stepWidth)+stepWidth, (wh*stepHeight)+stepHeight, imageRGBA, bilateral, first, &wg)
			}
		}
		wg.Wait()
	}

	gift2 := gift.New(gift.ResizeToFill((*img).Bounds().Dx(), (*img).Bounds().Dy(), gift.LanczosResampling, gift.CenterAnchor))

	rectOriginalSize := image.Rectangle{image.Point{0, 0}, image.Point{(*img).Bounds().Dx(), (*img).Bounds().Dy()}}

	imgageRes := image.NewNRGBA(rectOriginalSize)
	source := imageRGBA.GetImage()
	gift2.Draw(imgageRes, *source)

	return imgageRes
}

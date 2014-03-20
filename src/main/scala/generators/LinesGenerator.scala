package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator.{ DimensionToImage, Spot, Grid, canvasFactory}
import scala.language.implicitConversions

class LinesGenerator(wdt: Int, hgt: Int, lnWdt: Int) {
  if(hgt < 1 | wdt < 1 | lnWdt < 1) 
    throw new IllegalArgumentException("Values for height, width and line width must be positive.")
  
  def height = hgt
  def width = wdt
  def lineWidth = lnWdt
  
  // TODO: Pretty good. Could still improve spacing plans.
  def spacing: Randomize[Int] = Randomize.equalProb((3 to (width / lineWidth) / 2).toList map (_ * lnWdt))
  
  protected def makeVerticalLines(): List[Spot] = {
    def linePlacer(place: Int = spacing.select, spots: List[Spot] = Nil): List[Spot] = {
      if(width < 3 * lnWdt + place) spots
      else linePlacer(place + spacing.select, ((place, 0), lnWdt, hgt) :: spots)
    }
    linePlacer().reverse
  }
  
  def makeHorizontalLines(): List[Spot] = {
    def linePlacer(place: Int = spacing.select, spots: List[Spot] = Nil): List[Spot] = {
      if(height < 3 * lnWdt + place) spots
      else linePlacer(place + spacing.select, ((0, place), wdt, lnWdt) :: spots)
    }
    linePlacer().reverse
  }
  
  def drawLines(spots: List[Spot], canvas: Image = canvasFactory(width, height)): Image = {
    def makeLine(spot: Spot) = RectangleFilled(Color.Black, spot._2, spot._3)
    def drawSpotOnCanvas(canvas: Image, spot: Spot): Image = 
      canvas.placeImage(makeLine(spot), spot._1._1, spot._1._2, XAlign.Left, YAlign.Top)
    (canvas /: spots)(drawSpotOnCanvas)
  }
  
  def asImage: Image = {
    val verts = makeVerticalLines
    val hors = makeHorizontalLines
    drawLines(verts ++ hors)
  }
  
  def makeGrid(vertLines: List[Spot], horLines: List[Spot]): Grid = {
    Nil
  }
  
  def grid: Grid = {
    Nil
  }
}

object LinesGenerator {
  def apply(hgt: Int, wdt: Int, lnWdt: Int): LinesGenerator = 
    new LinesGenerator(hgt, wdt, lnWdt)
}
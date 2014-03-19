package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator.{ DimensionToImage, Spot , canvasFactory}
import scala.language.implicitConversions

class LinesGenerator(hgt: Int, wdt: Int, lnWdt: Int) {
  if(hgt < 1 | wdt < 1 | lnWdt < 1) 
    throw new IllegalArgumentException("Values for height, width and line width must be positive.")
  
  def height = hgt
  def width = wdt
  def lineWidth = lnWdt
  
  def spacing: Randomize[Int] = Randomize.equalProb((3 to 30).toList map (_ * lnWdt))
  
  def makeVerticalLines(): List[Spot] = {
    def linePlacer(place: Int = spacing.select, spots: List[Spot] = Nil): List[Spot] = {
      if(width < 3 * lnWdt + place) spots
      else linePlacer(place + spacing.select, ((place, 0), lnWdt, hgt) :: spots)
    }
    linePlacer()
  }
  
  def drawLines(spots: List[Spot]): Image = {
    val canvas = canvasFactory(height, width)
    def makeLine(spot: Spot) = RectangleFilled(Color.Black, spot._2, spot._3)
    def drawSpotOnCanvas(canvas: Image, spot: Spot): Image = 
      canvas.placeImage(makeLine(spot), spot._1._1, spot._1._2)
    (canvas /: spots)(drawSpotOnCanvas)
  }
  
  def demo = drawLines(makeVerticalLines)
}

object LinesGenerator {
  def apply(hgt: Int, wdt: Int, lnWdt: Int): LinesGenerator = 
    new LinesGenerator(hgt, wdt, lnWdt)
}
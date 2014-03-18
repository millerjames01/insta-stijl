package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator.{ DimensionToImage, Spot , canvasFactory}
import scala.language.implicitConversions

abstract class LinesGenerator(hgt: Int, wdt: Int, lnWdt: Int) {
  def height = hgt
  def width = wdt
  def lineWidth = lnWdt
  
  def spacing: Randomize[Int] = Randomize.equalProb((3 to 30).toList map (_ * lnWdt))
  
  def makeVerticalLines(): List[Spot] = {
    def linePlacer(widthLeft: Int = spacing.select, spots: List[Spot] = Nil): List[Spot] = {
      if(widthLeft < 3 * lnWdt) spots
      else linePlacer(widthLeft + spacing.select, ((widthLeft, 0), lnWdt, hgt) :: spots)
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
}
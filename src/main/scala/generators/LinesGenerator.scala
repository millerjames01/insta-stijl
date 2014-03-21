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
  def canvas = canvasFactory(width, height)
  protected def initialSpot = ((0, 0), width, height)
  
  
  // TODO: Pretty good. Could still improve spacing plans.
  protected def spacing: Randomize[Int] = Randomize.equalProb((3 to (width / lineWidth) / 2).toList map (_ * lnWdt))
  
  protected def makeVerticalLines(): List[Spot] = {
    def linePlacer(place: Int = spacing.select, spots: List[Spot] = Nil): List[Spot] = {
      if(width < 3 * lnWdt + place) spots
      else linePlacer(place + spacing.select, ((place, 0), lnWdt, hgt) :: spots)
    }
    linePlacer().reverse
  }
  
  protected def makeHorizontalLines(): List[Spot] = {
    def linePlacer(place: Int = spacing.select, spots: List[Spot] = Nil): List[Spot] = {
      if(height < 3 * lnWdt + place) spots
      else linePlacer(place + spacing.select, ((0, place), wdt, lnWdt) :: spots)
    }
    linePlacer().reverse
  }
  
  protected def drawLines(spots: List[Spot], canvas: Image = canvasFactory(width, height)): Image = {
    def makeLine(spot: Spot) = RectangleFilled(Color.Black, spot._2, spot._3)
    def drawSpotOnCanvas(canvas: Image, spot: Spot): Image = 
      canvas.placeImage(makeLine(spot), spot._1._1, spot._1._2, XAlign.Left, YAlign.Top)
    (canvas /: spots)(drawSpotOnCanvas)
  }
  
  def makeGridOfVerticals(vertLines: List[Spot], toSplit: Spot = initialSpot, currGrid: Grid = Nil): Grid = vertLines match {
    case Nil => toSplit :: currGrid
    case line :: rest => {
      val definiteSpot = (toSplit._1, line._1._1 - toSplit._1._1, toSplit._3)
      val nextToSplit = (((line._1._1 + line._2), 0), width - (line._1._1 + line._2), toSplit._3)
      makeGridOfVerticals(rest, nextToSplit, definiteSpot :: currGrid)
    }
  } 
  
  // TODO: This is a stub
  def makeGrid(vertLines: List[Spot], horLines: List[Spot], toSplit: Spot = initialSpot): Grid = {
    Nil
  }
  
  def generate: (Image, Grid) = {
    val verts = makeVerticalLines
    val hors = makeHorizontalLines
    (drawLines(verts ++ hors), makeGrid(verts, hors))
  }
}

object LinesGenerator {
  def apply(hgt: Int, wdt: Int, lnWdt: Int): LinesGenerator = 
    new LinesGenerator(hgt, wdt, lnWdt)
}
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
  
  
  sealed abstract class Dimension
  case object Horizontal extends Dimension
  case object Vertical extends Dimension
  // TODO: Pretty good. Could still improve spacing plans.
  protected def spacing(dim: Dimension): Randomize[Int] = {
    val bound = dim match {
      case Vertical => width
      case Horizontal => height
    }
    val lineOptions = (3 to (bound / lineWidth) / 3).toList map (_ * lnWdt)
    if(lineOptions == Nil) Randomize.equalProb(width + 1)
    else Randomize.equalProb(lineOptions)
  }
  
  protected def makeVerticalLines(): List[Spot] = {
    def linePlacer(place: Int = spacing(Vertical).select, spots: List[Spot] = Nil): List[Spot] = {
      if(width < 3 * lnWdt + place) spots
      else linePlacer(place + spacing(Vertical).select, ((place, 0), lnWdt, hgt) :: spots)
    }
    linePlacer().reverse
  }
  
  protected def makeHorizontalLines(): List[Spot] = {
    def linePlacer(place: Int = spacing(Horizontal).select, spots: List[Spot] = Nil): List[Spot] = {
      if(height < 3 * lnWdt + place) spots
      else linePlacer(place + spacing(Horizontal).select, ((0, place), wdt, lnWdt) :: spots)
    }
    linePlacer().reverse
  }
  
  protected def drawLines(spots: List[Spot], canvas: Image = canvasFactory(width, height)): Image = {
    def makeLine(spot: Spot) = RectangleFilled(Color.Black, spot._2, spot._3)
    def drawSpotOnCanvas(canvas: Image, spot: Spot): Image = 
      canvas.placeImage(makeLine(spot), spot._1._1, spot._1._2, XAlign.Left, YAlign.Top)
    (canvas /: spots)(drawSpotOnCanvas)
  }
  
  protected def makeGridOfVerticals(vertLines: List[Spot], toSplit: Spot = initialSpot, currGrid: Grid = Nil): Grid = vertLines match {
    case Nil => (toSplit :: currGrid).reverse
    case line :: rest => {
      val definiteSpot = (toSplit._1, line._1._1 - toSplit._1._1, toSplit._3)
      val nextToSplit = (((line._1._1 + line._2), 0), width - (line._1._1 + line._2), toSplit._3)
      makeGridOfVerticals(rest, nextToSplit, definiteSpot :: currGrid)
    }
  } 
 
  // TODO: Test this more
  protected def overlayHorizontals(horLines: List[Spot], gridToSplit: Grid, newGrid: Grid = Nil): Grid = horLines match {
    case Nil => (newGrid ++ gridToSplit)
    case line :: rest => {
      val definiteSpots = gridToSplit map (spot => (spot._1, spot._2, line._1._2 - spot._1._2))
      val nextGridToSplit = gridToSplit map (spot => 
        ((spot._1._1, line._1._2 + line._3), spot._2, height - (line._1._2 + line._3)))
      overlayHorizontals(rest, nextGridToSplit, newGrid ++ definiteSpots)
    }
  }
  
  // TODO: This is a stub
  def makeGrid(vertLines: List[Spot], horLines: List[Spot]): Grid = {
    val vertGrid = makeGridOfVerticals(vertLines)
    overlayHorizontals(horLines, vertGrid)
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
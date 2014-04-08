package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator.{ DimensionToImage, Spot, Grid, canvasFactory, rectangleFactory}
import scala.language.implicitConversions
import generators.{ SpacedCompositionGenerator => SCG }
import scala.{ Some => Colored, None => Blank}

class SpacedCompositionGenerator(width: Int, height: Int, lineWidth: Int, colors: Randomize[Color]) 
	extends CompositionGenerator(width, height, lineWidth) {
  override def fillIn(img: Image, grd: Grid) = {
    def fillAndSwitch(image: Image = img, grid: Grid = grd, randomColor: Randomize[Color]= colors, blankIter: Int = 1): Image = grid match {
      case Nil => image
      case spot :: rest => {
        val (coord, hgt, wid) = spot
        val (x, y) = coord
        val blankOrColor = SCG.space(randomColor, 30 - blankIter, blankIter) 
        blankOrColor match {
          case Blank => fillAndSwitch(image, rest, randomColor, blankIter + 1)
          case Colored(color) => {
            val squareToAdd = rectangleFactory(color)(hgt, wid)
            val newImage = image.placeImage(squareToAdd, x, y, XAlign.Left, YAlign.Top)
            val newColors: Randomize[Color] = try { randomColor - color } catch { case ex: Exception => colors }
            fillAndSwitch(newImage, rest, newColors, 0)
          }
        }
      }
    }
    fillAndSwitch()
  }
}

object SpacedCompositionGenerator {
  def space(colors: Randomize[Color], blankOdds: Int, colorOdds: Int): Option[Color] = {
    def makeColored = Colored(colors.select)
    Randomize((Blank, blankOdds), (makeColored, colorOdds)).select
  }
  
  def basicColors: Randomize[Color] = {
    val red = Color(185,31,3)
    val blue = Color(52, 109, 162)
    val yellow = Color(223,183,0)
    Randomize.equalProb(red, blue, yellow)
  }
}
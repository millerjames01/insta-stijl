package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator.{ DimensionToImage, Spot, Grid, canvasFactory, rectangleFactory}
import scala.language.implicitConversions
import generators.{ SpacedCompositionGenerator => SCG }
import scala.{ Some => Filled, None => Blank}

class SpacedCompositionGenerator[T](width: Int, height: Int, lineWidth: Int, fill: Randomize[T], 
    roller: T => DimensionToImage, oddsOfBlank: Int = 30)
	extends CompositionGenerator(width, height, lineWidth) {
  override def fillIn(img: Image, grd: Grid) = {
    def fillAndSwitch(image: Image = img, grid: Grid = grd, randomFill: Randomize[T]= fill, blankIter: Int = 1): Image = grid match {
      case Nil => image
      case spot :: rest => {
        val (coord, hgt, wid) = spot
        val (x, y) = coord
        val blankOrColor = SCG.space(randomFill, oddsOfBlank - blankIter, blankIter) 
        blankOrColor match {
          case Blank => fillAndSwitch(image, rest, randomFill, blankIter + 1)
          case Filled(fi) => {
            val squareToAdd = roller(fi)(hgt, wid)
            val newImage = image.placeImage(squareToAdd, x, y, XAlign.Left, YAlign.Top)
            val newProps: Randomize[T] = try { randomFill - fi } catch { case ex: Exception => randomFill }
            fillAndSwitch(newImage, rest, newProps, 0)
          }
        }
      }
    }
    fillAndSwitch()
  }
}

object SpacedCompositionGenerator {
  val red = Color(185,31,3)
  val blue = Color(52, 109, 162)
  val yellow = Color(223,183,0)
  
  def space[T](fill: Randomize[T], blankOdds: Int, colorOdds: Int): Option[T] = {
    def makeColored = Filled(fill.select)
    Randomize((Blank, blankOdds), (makeColored, colorOdds)).select
  }
  
  def basicColors: Randomize[Color] = {
    Randomize.equalProb(red, blue, yellow)
  }
  
  def spacedColorCompositionGenerator(width: Int, height: Int, lineWidth: Int) = 
    new SpacedCompositionGenerator[Color](width, height, lineWidth, basicColors, rectangleFactory(_))
    
  def spacedRecursiveCompositionGenerator(width: Int, height: Int, lineWidth: Int) = {
    val sccg = spacedColorCompositionGenerator(400, 300, 3)
    val samples = ((1 to 20).toList) map (i => sccg.generate)
    def randomSample = Randomize.equalProb(samples) + (rectangleFactory(red)(100,100), 10) + 
    				   (rectangleFactory(blue)(100,100), 10) + (rectangleFactory(yellow)(100, 100), 10)
    def squish(img: Image): DimensionToImage = (width, height) => { 
      val iWidth: Double = img.width
      val iHght: Double = img.height
      img.scale(width / iWidth, height / iHght) 
    }
    new SpacedCompositionGenerator[Image](width, height, lineWidth, randomSample, squish, 20)
  }
}
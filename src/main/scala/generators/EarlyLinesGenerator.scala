package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator.DimensionToImage

class EarlyLinesGenerator(hgt: Int, wdt: Int, lnWdt: Int) extends LinesGenerator {
  def height: Int = hgt
  def width: Int = wdt
  def lineWidth: Int = lnWdt
  
  def verticalCount: Randomize[Int] = 
  def horizontalCount: Randomize[Int]
  def spacing: Randomize[Int]
  
  def generate(): Image
}
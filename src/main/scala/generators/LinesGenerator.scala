package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator.DimensionToImage

trait LinesGenerator {
  def height: Int
  def width: Int
  def lineWidth: Int
  
  def verticalCount: Randomize[Int]
  def horizontalCount: Randomize[Int]
  def spacing: Randomize[Int]
  
  def generate(): Image
}
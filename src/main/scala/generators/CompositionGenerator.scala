package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator.DimensionToImage

class CompositionGenerator(height: Int, width: Int, 
    randomImage: Randomize[DimensionToImage]) {
  
  protected def generateLines = 0

}

object CompositionGenerator {
  type DimensionToImage = (Int, Int) => Image
  type Spot = ((Int, Int), Int, Int)
  type Grid = List[Spot]
  
  def rectangleFactory(color: Color): DimensionToImage =
    RectangleFilled(color, _, _)
  def canvasFactory: DimensionToImage =
    rectangleFactory(Color.White)
    
  val defaultWidth = 400
  val defaultHeight = 500
  val lineWidth = 10
  val lineSpacing = (Random.nextInt(98) + 2) * 5
  val numLines = Randomize((1, 2), (2, 4), (3, 2))
  
  def tooSmall(spot: Spot): Boolean = 
    spot._2 >= 50 & spot._3 >= 50 & spot._2 * spot._3 > 5625
}
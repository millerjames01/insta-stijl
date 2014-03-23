package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator.DimensionToImage

class CompositionGenerator(width: Int, height: Int, lineWidth: Int, randomImage: Randomize[DimensionToImage]) {
  protected val lineGenerator = LinesGenerator(width, height, lineWidth)

  def generate: Image = lineGenerator.generate._1
}

object CompositionGenerator {
  type DimensionToImage = (Int, Int) => Image
  type Spot = ((Int, Int), Int, Int)
  type Grid = List[Spot]
  
  def rectangleFactory(color: Color): DimensionToImage =
    RectangleFilled(color, _, _)
  def canvasFactory: DimensionToImage =
    rectangleFactory(Color.White)
}
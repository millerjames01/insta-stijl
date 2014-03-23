package generators

import org.dupontmanual.image._
import util.Randomize
import scala.util.Random
import CompositionGenerator._

class CompositionGenerator(width: Int, height: Int, lineWidth: Int, randomImage: Randomize[DimensionToImage] = basicShapes) {
  protected val lineGenerator = LinesGenerator(width, height, lineWidth)

  def generate: Image = {
    val (img, grd) = lineGenerator.generate
    
    (img /: grd)((currImage, spot) => {
      val (x, y) = spot._1
      val selectedImage = randomImage.select()(spot._2, spot._3)
      currImage.placeImage(selectedImage, x, y, XAlign.Left, YAlign.Top)
    })
  }
}

object CompositionGenerator {
  type DimensionToImage = (Int, Int) => Image
  type Spot = ((Int, Int), Int, Int)
  type Grid = List[Spot]
  
  def apply(width: Int, height: Int, lineWidth: Int, randomImage: Randomize[DimensionToImage]) = 
    new CompositionGenerator(width, height, lineWidth, randomImage)
  
  def rectangleFactory(color: Color): DimensionToImage =
    RectangleFilled(color, _, _)
  def canvasFactory: DimensionToImage =
    rectangleFactory(Color.White)
  
  def basicShapes: Randomize[DimensionToImage] = {
    val redRects = rectangleFactory(Color(185,31,3))
    val blueRects = rectangleFactory(Color(52, 109, 162))
    val yellowRects = rectangleFactory(Color(223,183,0))
    Randomize((canvasFactory, 20), (redRects, 2), (blueRects, 2), (yellowRects, 1))
  }
  
  def alternateShapes: Randomize[DimensionToImage] = {
    val green = rectangleFactory(Color(157, 232, 135))
    val pink = rectangleFactory(Color(240, 144, 149))
    val brown = rectangleFactory(Color(146, 98, 57))
    Randomize((canvasFactory, 20), (green, 2), (pink, 2), (brown, 1))
  }
  
  def recursiveShapes(lineWidth: Int): Randomize[DimensionToImage] = {
    val redRects = rectangleFactory(Color(185,31,3))
    val blueRects = rectangleFactory(Color(52, 109, 162))
    val yellowRects = rectangleFactory(Color(223,183,0))
    val recursive: DimensionToImage = CompositionGenerator(_, _, lineWidth, basicShapes).generate
    Randomize((canvasFactory, 70), (recursive, 10), (redRects, 4), (blueRects, 3), (yellowRects, 3))
  }
}
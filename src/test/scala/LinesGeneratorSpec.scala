package test

import org.scalatest.{FunSuite, GivenWhenThen, PrivateMethodTester}
import org.dupontmanual.image._
import generators.LinesGenerator
import generators.CompositionGenerator.{ Spot, Grid }
import scala.language.implicitConversions

class LinesGeneratorSpec extends FunSuite with GivenWhenThen with PrivateMethodTester {
  test("Constructor should initiate values correctly.") {
    val lg = LinesGenerator(400, 600, 20)
    assert(lg.width == 400)
    assert(lg.height == 600)
    assert(lg.lineWidth == 20)
  }
  
  test("Constructor should catch negative arguments as an error.") {
    intercept[IllegalArgumentException] {
      val lg = LinesGenerator(-20, 200, 30)
    }
    intercept[IllegalArgumentException] {
      val lg = LinesGenerator(20, -30, 50) 
    }
    intercept[IllegalArgumentException] {
      val lg = LinesGenerator(20, 3000, -20)
    }
  }
  
  test("Generated lines requirements") {
    val width = 800
    val height = 600
    val lineWidth = 15
    val lg = LinesGenerator(width, height, lineWidth)
    
    val makeVerticalLines = PrivateMethod[Lines]('makeVerticalLines)
    val makeHorizontalLines = PrivateMethod[Lines]('makeHorizontalLines)
    
    sealed abstract class Axis {
      def getFromLine(line: Spot): Int
      def linePlacementCheck(line: Spot): Boolean
    }
    case object X extends Axis {
      def getFromLine(line: Spot) = line._1._1
      def linePlacementCheck(line: Spot): Boolean = {
        val ((x, y), w, h) = line
        lineWidth <= x & x <= width - lineWidth & 0 == y & h == height
      }
    }
    case object Y extends Axis {
      def getFromLine(line: Spot) = line._1._2
      def linePlacementCheck(line: Spot) = {
        val ((x, y), w, h) = line
        lineWidth <= y & y <= height - lineWidth & 0 == x & w == width
      }
    }
    type Lines = List[Spot]
    val linesToTestX = for(i <- (1 to 10).toList) yield (lg invokePrivate makeVerticalLines())
    val offCanvasLines: Lines = List(((width, 0), lineWidth, height))
    val linesToTestY = for(i <- (1 to 10).toList) yield (lg invokePrivate makeHorizontalLines())
    
    def sortedByPos(lines: Lines, axis: Axis): Boolean = {
      def lessThanNext(lastPos: Int = lineWidth - 1, linesToTest: Lines = lines): Boolean = linesToTest match {
        case Nil => true
        case line :: rest => {
          val currentPos = axis.getFromLine(line)
          if(lastPos + lineWidth >= currentPos) false 
          else lessThanNext(currentPos, rest)
        }
      }
      lessThanNext()
    }
    
    def onCanvas(lines: Lines, axis: Axis): Boolean = {
      def isOnCanvas(line: Spot) = {
        val ((x, y), w, h) = line
        axis.linePlacementCheck(line)
      }
      lines forall (isOnCanvas(_))
    }
    
    val testResultsX = linesToTestX map (ls => (sortedByPos(ls, X), onCanvas(ls, X)))
    val allSortedX = (true /: testResultsX)(_ & _._1)
    val allOnX = (true /: testResultsX)(_ & _._2)
    val smallSizeX = linesToTestX.head.size == 0 | linesToTestX.head.size == 1
    info("Vertical lines should be")
    info("sorted by ascending x values")
    assert(allSortedX)
    assert(smallSizeX | !sortedByPos(linesToTestX.head.reverse, X))
    info("all striping the canvas vertically")
    assert(allOnX)
    assert(!onCanvas(offCanvasLines, X))
    
    
    val testResultsY = linesToTestY map (ls => (sortedByPos(ls, Y), onCanvas(ls, Y)))
    val allSortedY = (true /: testResultsY) (_ & _._1)
    val allOnY = (true /: testResultsY) (_ & _._2)
    val smallSizeY = linesToTestY.head.size == 0 | linesToTestY.head.size == 1
    info("Horizontal lines should be")
    info("sorted by ascending y values")
    assert(allSortedY)
    assert(smallSizeY | !sortedByPos(linesToTestY.head.reverse, Y))
    info("all striping the canvas horizontally")
    assert(allOnY)
    assert(!onCanvas(offCanvasLines, Y))
  }
  
  test("Lines should be drawn according to position correctly") {
    val drawLines = PrivateMethod[Image]('drawLines)
    
    val lg = LinesGenerator(200, 200, 10)
    val someLines = List(((130,0),10,200), ((170,0),10,200)) ++ List(((0,30),200,10))
    val correctImage = Bitmap("./src/test/resources/lines-test-1.png")
    val testImage = lg invokePrivate drawLines(someLines, lg.canvas)
    assert(testImage sameBitmapAs correctImage)
  }
  
  test("Grids should be built appropriately") {
    info("Vertical lines should make a grid") 
    val makeGridOfVerticals = PrivateMethod[Grid]('makeGridOfVerticals)
    
    val lg = LinesGenerator(300, 300, 15)
    val initialSpot = ((0, 0), 300, 300)
    val noLines = lg invokePrivate makeGridOfVerticals(Nil, initialSpot, Nil)
    val oneLine = lg invokePrivate makeGridOfVerticals(List(((150, 0), 15, 300)), initialSpot, Nil)
    assert(noLines == List(initialSpot)) 
    assert(oneLine == List(((0, 0), 150, 300), ((165, 0), 135, 300)))
    
    info("Horizontal lines should overlay those lines")
    val overlayHorizontals = PrivateMethod[Grid]('overlayHorizontals)
    val addToOneLine = lg invokePrivate overlayHorizontals(List(((0, 100), 300, 15)), oneLine, Nil)
    assert(addToOneLine == List(((0, 0), 150, 100), ((165, 0), 135, 100),
    						    ((0, 115), 150, 185), ((165, 115), 135, 185)))
  }
}
package test

import org.scalatest.{FunSuite, GivenWhenThen}
import org.dupontmanual.image._
import generators.LinesGenerator
import generators.CompositionGenerator.Spot
import scala.language.implicitConversions

class LinesGeneratorSpec extends FunSuite with GivenWhenThen {
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
  
  test("Generated vertical lines spots should be") {
    val width = 800
    val height = 600
    val lineWidth = 15
    val lg = LinesGenerator(width, height, lineWidth)
    
    type Lines = List[Spot]
    val linesToTest: List[Lines] = for(i <- (1 to 10).toList) yield lg.makeVerticalLines
    
    def sortedByX(lines: Lines): Boolean = {
      def lessThanNext(lastX: Int = lineWidth - 1, linesToTest: Lines = lines): Boolean = linesToTest match {
        case Nil => true
        case line :: rest => {
          val currentX = line._1._1
          if(lastX + lineWidth >= currentX) false
          else lessThanNext(currentX, rest)
        }
      }
      lessThanNext()
    }
    
    def onCanvas(lines: Lines): Boolean = {
      def isOnCanvas(line: Spot) = {
        val ((x, y), w, h) = line
        0 <= x & x <= width - lineWidth & 0 == y & h == height
      }
      lines forall (isOnCanvas(_))
    }
    
    val testResults = linesToTest map (ls => (sortedByX(ls), onCanvas(ls)))
    val allSorted = (true /: testResults)(_ | _._1)
    val allOn = (true /: testResults)(_ | _._2)
    info("sorted by ascending x values")
    assert(allSorted)
    info("all striping the canvas")
    assert(allOn)
  }
  
  test("Lines should be drawn according to position correctly") {
    
  }
}
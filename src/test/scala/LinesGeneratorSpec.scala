package test

import org.scalatest.FunSuite
import org.dupontmanual.image._
import generators.LinesGenerator

class LinesGeneratorSpec extends FunSuite {
  test("Constructor should initiate values correctly.") {
    val lg = LinesGenerator(400, 600, 20)
    assert(lg.height == 400)
    assert(lg.width == 600)
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
  
  test("Vertical lines generated should") {}
}
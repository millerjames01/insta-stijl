package test

import org.scalatest.FunSuite
import org.dupontmanual.image._
import util.Randomize

class RandomizeSuite extends FunSuite {
  test("Randomize shouldn't allow negative prevalences.") {
    intercept[IllegalArgumentException] {
      val spinner = Randomize((1, 5), (2, 5), (3, -5))
    }
  }
  
  test("Randomize must have one prevalent choice.") {
    intercept[IllegalArgumentException] {
      val spinner = Randomize((1, 0), (2, 0), (3, 0))
    }
  }
  
  test("Randomize always selects a single option.") {
    val singleSpinner = Randomize((1, 40))
    val spins = (1 to 100) map ((x) => singleSpinner.select())
    val allSpins1 = (false /: spins)(_ | _ == 1)
    assert(allSpins1)
  }
  
  test("Prevalences give accurate approximations.") {
    sealed abstract class GameSpin
    case object Jackpot extends GameSpin
    case object TryAgain extends GameSpin
    
    val attempts = 10000
    val jackpotPrev = 10
    val tryagainPrev = 90
    val jackpotProb = jackpotPrev / (jackpotPrev + tryagainPrev * 1.0)
    val tryagainProb = tryagainPrev / (jackpotPrev + tryagainPrev * 1.0)
    val expectedJackpots = jackpotProb * attempts
    val expectedTryagains = tryagainProb * attempts
    val allowance = attempts / 100
    
    val jackpotSpinner = Randomize[GameSpin]((Jackpot, jackpotPrev), (TryAgain, tryagainPrev))
    val spins = (1 to attempts) map ((x) => jackpotSpinner.select())
    val jackpots = spins.count(_ == Jackpot)
    val jackpotsAreClose = 
      expectedJackpots - allowance < jackpots && jackpots < expectedJackpots + allowance
    assert(jackpotsAreClose)
    
  }
}
package util

import org.dupontmanual.image._
import scala.util.Random
import scala.language.implicitConversions

class Randomize[T](choiceAndPrev: List[(T, Int)]) {
  protected val containsNegPrev = (false /: choiceAndPrev)(_ | _._2 <= 0)
  protected val totalPrev = (0 /: choiceAndPrev)(_ + _._2)
  
  if(containsNegPrev) 
    throw new IllegalArgumentException("Prevalence for any choice cannot be an integer less than 0.")
  if(totalPrev == 0) 
    throw new IllegalArgumentException("One choice must have a prevalence greater than 0.")
  
  def select(): T = {
    def pickT(choices: List[(T, Int)], skip: Int = Random.nextInt(totalPrev)): T = {
      val (pick, prev) = choices.head
      if(skip - prev < 0) pick
      else pickT(choices.tail, skip - prev)
    }
    pickT(choiceAndPrev)
  }
  
  // TODO: Test below this line
  def remove(): (T, Randomize[T]) = {
    val pick = select()
    (pick, this - pick)
  }
  
  def +(newChoice: (T, Int)): Randomize[T] =
    new Randomize(newChoice :: choiceAndPrev.filter(_._2 == newChoice._1))
  
  def -(choice: T): Randomize[T] = {
    new Randomize(choiceAndPrev.filter(_._1 == choice))
  }
}

object Randomize {
  def apply[T](choiceAndPrev: (T, Int)*): Randomize[T] = 
    new Randomize(choiceAndPrev.toList)
  
  def apply[T](choiceAndPrev: List[(T, Int)]): Randomize[T] = 
    new Randomize(choiceAndPrev)
  
  // TODO: Test these
  def equalProb[T](choices: List[T]): Randomize[T] =
    new Randomize(choices map ((t: T) => (t, 1)))
  
  def equalProb[T](choices: T*): Randomize[T] = equalProb(choices.toList)  
}
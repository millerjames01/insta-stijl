import generators._
import SpacedCompositionGenerator._
import util.Randomize

object Demo extends App {
  override def main(args: Array[String]) {
    val scg = spacedColorCompositionGenerator(800, 600, 5)
    val randomQuote = Randomize.equalProb(
      "All painting – the painting of the past as well as of the present – shows us that its essential plastic means we are only line and color.",
      "In past times when one lived in contact with nature, abstraction was easy; it was done unconsciously. Now in our denaturalized age abstraction becomes an effort.",
      "Art is not made for anybody and is, at the same time, for everybody.",
      "The emotion of beauty is always obscured by the appearance of the object. Therefore the object must be eliminated from the picture.",
      "The colored planes, as much by position and dimension as by the greater value given to color, plastically express only relationships and not forms.",
      "Curves are so emotional.",
      "Everything is expressed through relationships.",
      "The position of the artist is humble. He is essentially a channel.",
      "In art the search for a content which is collectively understandable is false; the content will always be individual.",
      "Every true artist has been inspired more by the beauty of lines and color and the relationships between them than by the concrete subject of the picture.",
      "Intellect confuses intuition.",
      "Trees! How ghastly!"
    )
    
    def getQuotes(count: Int = 4, randomize: Randomize[String] = randomQuote, list: List[String] = Nil): Seq[String] = {
      if(count == 0) list.toSeq
      else {
        val (quote, removedRandom) = randomize.remove
        getQuotes(count - 1, removedRandom, quote :: list)
      }
    }
    
    val quotes = getQuotes()
    
    for(i <- 0 to 3) {
      for(j <- 1 to 15) {
        scg.generate.saveAsDisplayed("samples/sample" + (i * 15 + j))
      }
      println("\"" + quotes(i) + "\" (" + {(i + 1) * 25} + "%)")
    }
  }
}
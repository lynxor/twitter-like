package ag.parse

import java.io.FileNotFoundException

import org.scalatest.FunSuite

import scala.io.Source

class TweetParserSuite extends FunSuite {
  test("lineParse should work, simple case") {
    val result = TweetsParser.parseLine("Pietie> the message")
    assert(result === ("Pietie" -> "the message"))
  }

  test("lineParse should work with extra whitespace") {
    val result = TweetsParser.parseLine(" Pietie>  the message ")
    assert(result === ("Pietie" -> "the message"))
  }

  test("lineParse should work, even with a > in the message") {
    val result = TweetsParser.parseLine("Pietie> the mess>age")
    assert(result === ("Pietie" -> "the mess>age"))
  }

  test("message too long should throw IAE") {
    intercept[IllegalArgumentException] {
      TweetsParser.parseLine("Pietie> " + (0 to 141).map(_ => "a").mkString(""))
    }
  }

  test("invalid format - no space") {
    intercept[IllegalArgumentException] {
      TweetsParser.parseLine("Pietie>themessage")
    }
  }

  test("Multiple lines") {
    TweetsParser.parseFile(List("Pietie> themessage", "Jannie> other message").toIterator)
  }

  test("From file") {
    val results = TweetsParser.parseFile(Source.fromResource("given-tweets.txt"))
    assert(results === List(
      "Alan" -> "If you have a procedure with 10 parameters, you probably missed some.",
      "Ward" -> "There are only two hard things in Computer Science: cache invalidation, naming things and off-by-1 errors.",
      "Alan" -> "Random numbers should not be generated with a method chosen at random."
    ))
  }

  //Slightly redundant
  test("From non-extant file") {
    intercept[FileNotFoundException] {
      TweetsParser.parseFile("/tmp/this-file-does-not-exist.txt")
    }
  }
}

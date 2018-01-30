package ag.parse

import org.scalatest.FunSuite

import scala.io.Source

class TweetParserSuite extends FunSuite {
  test("lineParse should work, simple case") {
    val result = TweetsParser.parseLine("Pietie> the message")
    assert(result === Tweet("Pietie", "the message"))
  }

  test("lineParse should work with extra whitespace") {
    val result = TweetsParser.parseLine(" Pietie>  the message ")
    assert(result === Tweet("Pietie", "the message"))
  }

  test("lineParse should work, even with a > in the message") {
    val result = TweetsParser.parseLine("Pietie> the mess>age")
    assert(result === Tweet("Pietie", "the mess>age"))
  }
  test("lineParse should throw IAE on empty message"){
    intercept[IllegalArgumentException] {
      TweetsParser.parseLine("Pietie>")
    }
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
      Tweet("Alan","If you have a procedure with 10 parameters, you probably missed some."),
        Tweet("Ward","There are only two hard things in Computer Science: cache invalidation, naming things and off-by-1 errors."),
          Tweet("Alan","Random numbers should not be generated with a method chosen at random.")
    ))
  }

  test("Tweet format") {
    assert(Tweet("user1", "message 1").format === "\t@user1: message 1")
  }
}

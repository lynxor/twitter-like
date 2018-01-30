package ag

import ag.parse.{Tweet, User}
import org.scalatest.FunSuite

import scala.io.Source

class TweeterSuite extends FunSuite {
  test("populate tweets should work correctly in simple case, ordered") {
    val allTweets = List(Tweet("a", "msga"), Tweet("b", "msgb"))
    val userWithTweets = Tweeter.combine(
      List(User("a", "b"), User("c"), User("b")),
      allTweets)

    assert(userWithTweets === List(
      UserWithTweets("a", Set("b"), allTweets),
      UserWithTweets("b", Set(), List(Tweet("b", "msgb"))),
      UserWithTweets("c", Set(), Nil)))
  }

  test("populate tweets should ignore tweets if not in users") {
    val userWithTweets = Tweeter.combine(
      List(User("a")),
      List(Tweet("a", "msga"), Tweet("k", "ignored msg")))

    assert(userWithTweets === List(
      UserWithTweets("a", Set(), List(Tweet("a", "msga")))))
  }

  test("No users passed in, should return empty list") {
    assert(Tweeter.combine(Nil, Nil) === Nil)
  }

  test("format a user with tweets") {
    val result = UserWithTweets("a", Set("b"), List(Tweet("a", "msga"), Tweet("b", "msgb"))).format
    assert(result === "a\n\t@a: msga\n\t@b: msgb")
  }

  test("The given example") {
    val result = Tweeter.combine(
      Source.fromResource("given-users.txt"),
      Source.fromResource("given-tweets.txt")
    )
    val expected = List(
      UserWithTweets("Alan", Set("Martin"), List(
        Tweet("Alan", "If you have a procedure with 10 parameters, you probably missed some."),
        Tweet("Alan", "Random numbers should not be generated with a method chosen at random."))),
      UserWithTweets("Martin", Set(), List()),
      UserWithTweets("Ward", Set("Alan", "Martin"), List(
        Tweet("Alan", "If you have a procedure with 10 parameters, you probably missed some."),
        Tweet("Ward", "There are only two hard things in Computer Science: cache invalidation, naming things and off-by-1 errors."),
        Tweet("Alan", "Random numbers should not be generated with a method chosen at random."))))

    assert(result === expected)
  }
}

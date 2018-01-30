package ag

import ag.parse.{Tweet, User}
import org.scalatest.FunSuite

class TweeterSuite extends FunSuite {
  test("populate tweets should work correctly in simple case, ordered") {
    val allTweets = List(Tweet("a", "msga"), Tweet("b", "msgb"))
    val userWithTweets = Tweeter.populateTweets(
      List(User("a", "b"), User("c"), User("b")),
      allTweets)

    assert(userWithTweets === List(
      UserWithTweets("a", Set("b"), allTweets),
      UserWithTweets("b", Set(), List(Tweet("b", "msgb"))),
      UserWithTweets("c", Set(), Nil)))
  }

  test("populate tweets should ignore tweets if not in users") {
    val userWithTweets = Tweeter.populateTweets(
      List(User("a")),
      List(Tweet("a", "msga"), Tweet("k", "ignored msg")))

    assert(userWithTweets === List(
      UserWithTweets("a", Set(), List(Tweet("a", "msga")))))
  }

  test("No users passed in, should return empty list") {
    assert(Tweeter.populateTweets(Nil, Nil) === Nil)
  }

  test("format a user with tweets") {
    val result = UserWithTweets("a", Set("b"), List(Tweet("a", "msga"), Tweet("b", "msgb"))).format
    assert(result === "a\n\t@a: msga\n\t@b: msgb\n")
  }

}

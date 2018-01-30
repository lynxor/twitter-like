package ag.parse

import org.scalatest.FunSuite

import scala.io.Source

class UsersParserSuite extends FunSuite {
  val expectedResult2Users = List(
    "theuser" -> Set("otheruser", "otheruser2"),
    "otheruser" -> Set(),
    "otheruser2" -> Set())

  test("that the username parses from a line simple users line") {
    val result = UsersParser.parseLine("theuser follows otheruser")
    assert(result === List("theuser" -> Set("otheruser"), "otheruser" -> Set()))
  }

  test("that the username parses from a line in with more than one follows") {
    val result = UsersParser.parseLine("theuser follows otheruser, otheruser2")
    assert(result === expectedResult2Users)
  }

  test("that the username parses from a line in with no follows") {
    val result = UsersParser.parseLine("theuser follows ")
    assert(result === List("theuser" -> Set()))
  }

  test("that the username parses from a line with some extra spaces and tabs") {
    val result = UsersParser.parseLine(" theuser  follows  otheruser  ,   otheruser2 ")
    assert(result === expectedResult2Users)
  }

  test("that the username parses from a line with some extra commas") {
    val result = UsersParser.parseLine("theuser follows otheruser,,,otheruser2,,, ")
    assert(result === expectedResult2Users)
  }


  test("parsing lines works in happy case") {
    val result = UsersParser.parseLines(List("theuser follows otheruser", "otheruser follows theuser").toIterator)
    assert(result === List(User("theuser", "otheruser"), User("otheruser", "theuser")))
  }

  test("parsing lines following more than one other user") {
    val result = UsersParser.parseLines(List("theuser follows otheruser, otheruser2",
      "otheruser follows theuser").toIterator)

    assert(result === List(
      User("theuser", "otheruser", "otheruser2"),
      User("otheruser", "theuser"),
      User("otheruser2"),
    ))
  }

  test("parsing lines, no follows for one user") {
    val result = UsersParser.parseLines(List("theuser follows otheruser").toIterator)
    assert(result === List(User("theuser", "otheruser"), User("otheruser")))
  }

  test("parsing lines, user listed more than once. Combined follows correctly") {
    val result = UsersParser.parseLines(List("a follows b", "a follows c", "a follows d").toIterator)
    assert(result === List(User("a", "b", "c", "d"), User("b"), User("c"), User("d")))
  }

  test("parsing from file works, and has the correct order") {
    val source = Source.fromResource("given-users.txt")
    val users = UsersParser.parseFile(source)
    val expected = List(
      User("Ward", Set("Alan", "Martin")),
      User("Alan", Set("Martin")),
      User("Martin")
    )
    assert(users === expected)
  }
}

package ag.parse

import scala.io.Source

case class User(name: String, follows: Set[String] = Set())

object User {
  //convenience for test cases
  def apply(name: String, follows: String*) = new User(name, follows.toSet)
}


object UsersParser {
  type ParseResult = (String, Set[String])

  def split(str: String, separator: String): List[String] = str.split(separator).toList.collect {
    case s: String if s.trim.length > 0 => s.trim
  }

  def parseFile(source: Source): List[User] = parseLines(source.getLines)

  def parseLines(lines: Iterator[String]): List[User] = {
    val initialValue: Map[String, Set[String]] = Map[String, Set[String]]().withDefault(_ => Set())

    val unioned = lines.flatMap(parseLine).foldLeft(initialValue)((acc, value) => {
      val (user, follows) = value
      acc + (user -> (acc(user) ++ follows))
    })

    unioned.map(usr => {
      val (name, follows) = usr
      User(name, follows)
    }).toList
  }

  //Return ALL users mentioned in line. Username can contain follows but not as a word
  def parseLine(line: String): List[ParseResult] = {
    if (line.isEmpty) throw new IllegalArgumentException("Empty user line detected")
    split(line, """\bfollows\b""") match {
      case name :: Nil => List(name -> Set())
      case name :: followsString :: Nil =>
        val follows = parseFollows(followsString)
        (name -> follows) :: follows.map(f => f -> Set[String]()).toList
      case _ => throw new IllegalArgumentException("Invalid user line passed in")
    }
  }

  private def parseFollows(followsString: String): Set[String] = split(followsString, ",").toSet match {
    case usernames: Set[String] => usernames
    case _ => Set()
  }
}

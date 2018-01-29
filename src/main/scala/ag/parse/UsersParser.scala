package ag.parse

import scala.io.Source
import scala.util.parsing.combinator._

case class User(name: String, follows: Set[User] = Set())


object UsersParser {
  type ParseResult = (String, Set[String])

  private def split(str: String, separator: String): List[String] = str.split(separator).toList.collect {
    case s: String if s.trim.length > 0 => s.trim
  }
  def parseFile(filePath: String) = parseLines(Source.fromFile(filePath).getLines)

  def parseLines(lines: Iterator[String]) = {
     val initialValue = Map[String, Set[String]]().withDefault(_ => Set())

     lines.flatMap(parseLine).foldLeft(initialValue)( (acc, value) => {
        val (user, follows) = value
        acc + (user -> (acc(user) ++ follows))
     })
  }

  //Return ALL users mentioned in line
  def parseLine(line: String): List[ParseResult] = split(line, "follows") match {
    case name :: Nil => List(name -> Set())
    case name :: followsString :: Nil =>
      val follows = parseFollows(followsString)
      (name -> follows) :: follows.map(f => f -> Set[String]()).toList
    case _ => Nil
  }

  private def parseFollows(followsString: String): Set[String] = split(followsString, ",").toSet match {
    case usernames: Set[String] => usernames
    case _ => Set()
  }
}

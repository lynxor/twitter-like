package ag.parse

import scala.io.Source
import ParseUtils._

object UsersParser {
  type ParseResult = (String, Set[String])
  type ResultType = Map[String, Set[String]]

  def parseFile(filePath: String): ResultType = parseFile(Source.fromFile(filePath))
  def parseFile(source: Source): ResultType = parseLines(source.getLines)

  def parseLines(lines: Iterator[String]) : ResultType = {
     val initialValue : ResultType = Map[String, Set[String]]().withDefault(_ => Set())

     lines.flatMap(parseLine).foldLeft(initialValue)( (acc, value) => {
        val (user, follows) = value
        acc + (user -> (acc(user) ++ follows))
     })
  }

  //Return ALL users mentioned in line. Username can contain follows but not as a word
  def parseLine(line: String): List[ParseResult] = split(line, """\bfollows\b""") match {
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

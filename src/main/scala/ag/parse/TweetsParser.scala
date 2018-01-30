package ag.parse

import java.io.FileNotFoundException

import scala.io.Source

object TweetsParser {
  type ParseResult = (String, String)

  //Using regex so you can use > in the message
  private val userRegex = """^([a-zA-Z\s-]+)\>\s+(.*)$""".r
  def parseLine(line: String) : ParseResult = line match {
    case userRegex(user, msg) if msg.trim.length <= 140 => user.trim -> msg.trim
    case userRegex(_, msg) if msg.trim.length > 140 => throw new IllegalArgumentException("Not a valid tweet - message is too long: \n" + line)
    case _ => throw new IllegalArgumentException("Not a valid tweet. Follow this format - user> message. \n" + line)
  }

  def parseFile(lines: Iterator[String]) : List[ParseResult] = lines.map(parseLine).toList
  def parseFile(source: Source) : List[ParseResult] = {
    if(source.isEmpty) {
      throw new IllegalArgumentException("Empty source passed to tweets parser")
    }
    parseFile(source.getLines())
  }
  def parseFile(filePath: String) : List[ParseResult] = parseFile(Source.fromFile(filePath))
}
package ag.parse

import scala.io.Source

case class Tweet(user: String, msg: String) {
  def appliesToUser(aUser: User): Boolean = aUser.name == user || aUser.follows.contains(user)

  def format: String = s"""\t@$user: $msg"""
}

object TweetsParser {
  type ParseResult = Tweet

  //Using regex so you can use > in the message
  private val userRegex = """^([a-zA-Z\s-]+)\>\s+(.*)$""".r

  def parseLine(line: String): ParseResult = line match {
    case userRegex(user, msg) if msg.trim.length <= 140 && !msg.trim.isEmpty => Tweet(user.trim, msg.trim)
    case userRegex(_, msg) if msg.trim.length > 140 || msg.trim.isEmpty =>
      throw new IllegalArgumentException(s"Not a valid tweet - message is of invalid length(${msg.length}). Offending line: \n\t$line")
    case _ => throw new IllegalArgumentException(s"Not a valid tweet. Follow this format - user> message. The offending line: \n\t$line")
  }

  def parseFile(lines: Iterator[String]): List[ParseResult] = lines.collect {
    case line: String if !line.trim.isEmpty => parseLine(line)
  }.toList

  def parseFile(source: Source): List[ParseResult] = parseFile(source.getLines())
}

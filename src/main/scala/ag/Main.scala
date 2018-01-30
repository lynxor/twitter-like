package ag

import java.io.File

import scala.io.Source
import scala.util.{Failure, Success, Try}

object Main {
  def main(args: Array[String]) {
    args.toList match {
      case usersFile :: tweetsFile :: Nil =>
        run(usersFile, tweetsFile)
      case _ => println("""User file and tweets file(full path) expected as args""")
    }
  }

  private def run(usersFile: String, tweetsFile: String) {
    getSources(usersFile, tweetsFile) match {
      case Success((userSource, tweetSource)) =>
        val result = Tweeter.combine(userSource, tweetSource)
        println(result.map(_.format).mkString("\n"))
      case Failure(_) => println("Invalid file(s) passed. Pass in full path to your users and tweets file as args.")
    }
  }

  def getSources(usersFile: String, tweetsFile: String) : Try[(Source, Source)] =
      Try(Source.fromFile(usersFile) -> Source.fromFile(tweetsFile))
}

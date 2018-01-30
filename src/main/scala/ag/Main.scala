package ag

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.{Failure, Success, Try}

object Main {
  val logger = Logger(LoggerFactory.getLogger("name"))

  def main(args: Array[String]) {
    args.toList match {
      case usersFile :: tweetsFile :: Nil => Try(Source.fromFile(usersFile) -> Source.fromFile(tweetsFile)) match {
        case Success((userSource, tweetSource)) =>
          logger.info("Got input files. Running...")
          run(userSource, tweetSource)
        case Failure(e) => exitWithError("Invalid file(s) passed. Pass in full path to your users and tweets file as args.", e)
      }
      case _ => exitWithError(
        s"""User file and tweets file(full path) expected as args, passed args: [${args.mkString(",")}]""",
        new IllegalArgumentException())
    }
  }

  private def run(userSource: Source, tweetSource: Source) {
    Try(Tweeter.combine(userSource, tweetSource)) match {
      case Success(result) => println(result.map(_.format).mkString("\n"))
      case Failure(e) => exitWithError(e.getMessage, e)
    }
  }

  private def exitWithError(message: String, e: Throwable): Unit = {
    println(message) //simple message for user
    logger.error(message, e) //message plus stack for log
//    System.exit(1) //error status code for program . sbt doesn't like it
  }
}

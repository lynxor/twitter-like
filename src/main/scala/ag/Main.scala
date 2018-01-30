package ag

import scala.io.Source

object Main {
  def main(args: Array[String]) {
    args.toList match {
      case usersFile :: tweetsFile :: Nil =>
        val result = Tweeter.combine(Source.fromFile(usersFile), Source.fromFile(tweetsFile))
        println(result.map(_.format).mkString("\n"))
      case _ => println("""User file and tweets file expected as args""")
    }
  }
}

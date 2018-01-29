package ag

object Main {
  def main(args: Array[String]) {
    args.toList match {
      case usersFile :: tweetsFile :: Nil =>  println(usersFile + tweetsFile)
      case _ => println("""User file and tweets file expected as args""")
    }
  }
}

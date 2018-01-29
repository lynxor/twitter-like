//case class User(name: String, follows: Set[User] = Set())
//
////object User {
////  //  Create from line in users file
////  def apply(line: String): User = line.split("follows").toList match {
////    case name :: followsString :: Nil => new User(name, splitFollows(followsString))
////    case _ =>
////
////  }
////}
////
////private def splitFollows(followsString: String): Set[User] =
////  followsString.split(",").map(_.trim).toSet match {
////    case usernames: Set[String] => usernames.map( u => new User(u))
////  }
////
////}

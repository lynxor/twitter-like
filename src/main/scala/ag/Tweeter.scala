package ag

import ag.parse.{Tweet, User}

//Separate case class so you can't mistakenly assume the users from the parser already have tweets
case class UserWithTweets(name: String, follows: Set[String], tweets: List[Tweet])
  extends Ordered[UserWithTweets] {
  def compare(that: UserWithTweets): Int = this.name compare that.name

  def format: String = s"""$name\n${tweets.map(_.format).mkString("\n")}\n"""
}

object UserWithTweets {
  def apply(user: User, tweets: List[Tweet]) = new UserWithTweets(user.name, user.follows, tweets)
}

object Tweeter {
  def populateTweets(users: List[User], tweets: List[Tweet]): List[UserWithTweets] = users.map(user =>
    UserWithTweets(user, tweets.filter(tweet => tweet.appliesToUser(user)))).sorted
}

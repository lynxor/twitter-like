package ag

import ag.parse.{Tweet, TweetsParser, User, UsersParser}

import scala.io.Source

//Separate case class so you can't mistakenly assume the users from the parser already have tweets
case class UserWithTweets(name: String, follows: Set[String], tweets: List[Tweet])
  extends Ordered[UserWithTweets] {
  def compare(that: UserWithTweets): Int = this.name compare that.name

  //Not using toString - easier to debug with default case class toString
  def format: String = s"""$name\n${tweets.map(_.format).mkString("\n")}"""
}

object UserWithTweets {
  def apply(user: User, tweets: List[Tweet]) = new UserWithTweets(user.name, user.follows, tweets)
}

object Tweeter {

  def combine(userSource: Source, tweetSource: Source): List[UserWithTweets] = {
     val users = UsersParser.parseFile(userSource)
     val tweets = TweetsParser.parseFile(tweetSource)

     combine(users, tweets)
  }

  def combine(users: List[User], tweets: List[Tweet]): List[UserWithTweets] = users.map(user =>
    UserWithTweets(user, tweets.filter(tweet => tweet.appliesToUser(user)))).sorted
}

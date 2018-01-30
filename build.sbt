scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6",
  "junit" % "junit" % "4.12" % "test",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3")
showSuccess := false

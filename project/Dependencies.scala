import sbt._

object Dependencies {
  lazy val catsEffect = "org.typelevel" %% "cats-effect"  % "1.3.1"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.1"
}

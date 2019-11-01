import Dependencies._

lazy val root =
  (project in file("."))
    .enablePlugins(DynVerPlugin)
    .settings(
      name := "exploding",
      libraryDependencies ++= Seq(
        catsEffect,
        scalaTest  % Test,
        scalaCheck % Test
      )
    )
    .settings(settings)

lazy val settings = commonSettings ++ coverageSettings

lazy val coverageSettings = Seq(
  coverageFailOnMinimum := false,
  coverageExcludedFiles := ".*/target/.*",
  coverageHighlighting := true
)

lazy val commonSettings =
  Seq(
    scalaVersion := "2.12.8",
    organization := "com.example",
    organizationName := "example",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding",
      "UTF-8",
      "-Xfuture",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-unused",
      "-Xfatal-warnings",
      "-Xlint",
      "-Ypartial-unification"
    ),
    javacOptions ++= Seq(
      "-source",
      "1.8",
      "-target",
      "1.8"
    ),
    scalafmtOnCompile.in(ThisBuild) := true,
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")
  )

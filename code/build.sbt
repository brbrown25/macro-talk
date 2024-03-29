lazy val scVersion = "2.12.8"
lazy val paradiseVersion = "2.1.1"
lazy val common = List(
  scalaVersion := scVersion,
  organization := "com.bbrownsound",
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % scVersion,
    "org.slf4j" % "slf4j-api" % "1.7.25" % Provided,
    "org.slf4j" % "slf4j-simple" % "1.7.25" % Provided
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Ywarn-unused-import",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:experimental.macros" //,
    // "-Ystatistics",
    // "-print"
  ),
  addCompilerPlugin(("org.scalamacros" % "paradise" % paradiseVersion).cross(CrossVersion.full))
)

javaOptions ++= Seq(
  "-Dapplication.home=.",
  "-Dlogback.configurationFile=conf/logback.xml"
)

lazy val macros = (project in file("macros")).settings(inThisBuild(common), name := "macros")

lazy val examples = (project in file("examples"))
  .settings(
    inThisBuild(common),
    name := "examples",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    )
  )
  .aggregate(macros)
  .dependsOn(macros)

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

lazy val root = (project in file("."))
  .settings(
    inThisBuild(common),
    name := "okcfp-macro-talk"
  )
  .aggregate(macros, examples)

crossScalaVersions := Seq("2.10.7", "2.11.12", "2.12.8")

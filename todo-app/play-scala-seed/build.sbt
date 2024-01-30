name := """play-scala-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies ++= Seq(
	"org.playframework" %% "play-json" % "3.0.0",
	"org.playframework" %% "play-slick" % "6.0.0",
	"org.playframework" %% "play-slick-evolutions" % "6.0.0",

	"org.postgresql" % "postgresql" % "42.5.4",

	"io.sentry" % "sentry" % "7.2.0",

	"org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
	"com.h2database" % "h2" % "2.2.224" % Test
)

Test / testOptions += Tests.Argument("-Dconfig.resource=", "application-test.conf")

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

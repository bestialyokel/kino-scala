name := """play-scala-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .configure(ProjectSettings.commonProfile)
  .enablePlugins(PlayScala)

scalaVersion := "2.13.12"

ThisBuild / resolvers += "Artima Maven Repository".at("https://repo.artima.com/releases")

libraryDependencies += guice

libraryDependencies ++=
  Seq(
    "org.playframework"      %% "play-json"             % "3.0.0",
    "org.playframework"      %% "play-slick"            % "6.0.0",
    "com.github.tminglei"    %% "slick-pg"              % "0.21.1",
    "org.playframework"      %% "play-slick-evolutions" % "6.0.0",
    "com.beachape"           %% "enumeratum"            % "1.7.2",
    "com.beachape"           %% "enumeratum-slick"      % "1.7.4",
    "com.beachape"           %% "enumeratum-play-json"  % "1.7.3",
    "com.beachape"           %% "enumeratum-play"       % "1.8.0",
    "org.postgresql"          % "postgresql"            % "42.7.1",
    "io.sentry"               % "sentry"                % "7.2.0",
    "io.sentry"               % "sentry-logback"        % "6.19.0",
    "org.typelevel"          %% "cats-core"             % "2.9.0",
    "io.scalaland"           %% "chimney"               % "0.8.5",
    "org.scalatestplus.play" %% "scalatestplus-play"    % "7.0.0"   % Test,
    "com.h2database"          % "h2"                    % "2.2.224" % Test
  )

Test / testOptions += Tests.Argument("-Dconfig.resource=", "application-test.conf")

import play.sbt.routes.RoutesKeys
RoutesKeys.routesImport := Seq.empty
RoutesKeys.routesImport += "enums.TaskStatus"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

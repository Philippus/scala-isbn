name         := "scala-isbn"
organization := "nl.gn0s1s"
startYear    := Some(2021)
homepage     := Some(url("https://github.com/philippus/scala-isbn"))
licenses += ("MPL-2.0", url("https://www.mozilla.org/MPL/2.0/"))

developers := List(
  Developer(
    id = "philippus",
    name = "Philippus Baalman",
    email = "",
    url = url("https://github.com/philippus")
  )
)

scalaVersion := "2.13.16"
crossScalaVersions += "3.3.6"

scalacOptions += "-Xsource:3"

ThisBuild / versionScheme          := Some("semver-spec")
ThisBuild / versionPolicyIntention := Compatibility.BinaryCompatible

Compile / packageBin / packageOptions += Package.ManifestAttributes(
  "Automatic-Module-Name" -> "nl.gn0s1s.isbn"
)

scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml"        % "2.4.0",
  "org.scalameta"          %% "munit"            % "1.2.0" % Test,
  "org.scalameta"          %% "munit-scalacheck" % "1.2.0" % Test
)

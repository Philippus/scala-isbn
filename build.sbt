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

crossScalaVersions := List("2.13.15")
scalaVersion       := crossScalaVersions.value.last

ThisBuild / versionScheme          := Some("semver-spec")
ThisBuild / versionPolicyIntention := Compatibility.BinaryCompatible

Compile / packageBin / packageOptions += Package.ManifestAttributes(
  "Automatic-Module-Name" -> "nl.gn0s1s.isbn"
)

scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml"        % "2.3.0",
  "org.scalameta"          %% "munit"            % "1.0.2" % Test,
  "org.scalameta"          %% "munit-scalacheck" % "1.0.0" % Test
)

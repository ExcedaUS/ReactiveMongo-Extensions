import scalariform.formatter.preferences._
import aether.AetherKeys._

name := "reactivemongo-extensions"

lazy val commonSettings = Seq(
  organization := "org.reactivemongo",
  version := "0.12.1",
  scalaVersion  := "2.11.8",
  crossScalaVersions  := Seq("2.11.8"),
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-encoding", "utf8",
    "-feature",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:existentials",
    "-target:jvm-1.8"),
    resolvers ++= Seq(
    "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"),
  javaOptions in Test ++= Seq("-Xmx512m", "-XX:MaxPermSize=512m"),
  testOptions in Test += Tests.Argument("-oDS"),
  parallelExecution in Test := true,
  shellPrompt in ThisBuild := Common.prompt,
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignParameters, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
  .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true))

lazy val settings = (
  commonSettings
  ++ scalariformSettings
  ++ org.scalastyle.sbt.ScalastylePlugin.Settings)

lazy val publishSettings = Seq(
  credentials += Credentials(realm = "packagecloud", host = "packagecloud.io", userName = "", passwd = "f016816d0b60b45d8b0b77952f98b51a680c7c309015c8a7"),
  //publishMavenStyle := true,
  //publishArtifact in Test := false,
  aetherWagons := Seq(aether.WagonWrapper("packagecloud+https", "io.packagecloud.maven.wagon.PackagecloudWagon")),
  publishTo := Some("packagecloud+https" at "packagecloud+https://packagecloud.io/exceda-eng/maven")
)

lazy val root = project.in(file("."))
  .aggregate(bson, json, core, samples)
  .settings(settings: _*)
  .settings(publishSettings: _*)
  .settings(publishArtifact := false)
  .settings(unidocSettings: _*)

lazy val core = project.in(file("core"))
  .settings(settings: _*)
  .settings(publishSettings: _*)

lazy val bson = project.in(file("bson"))
  .settings(settings: _*)
  .settings(publishSettings: _*)
  .dependsOn(core % "test->test;compile->compile")

lazy val json = project.in(file("json"))
  .settings(settings: _*)
  .settings(publishSettings: _*)
  .dependsOn(core % "test->test;compile->compile")

lazy val samples = project.in(file("samples"))
  .settings(settings: _*)
  .settings(publishSettings: _*)
  .settings(publishArtifact := false)
  .dependsOn(core % "test->test;compile->compile", bson % "compile->compile")

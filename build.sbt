import sbt.Keys._
import io.mehitabel.fts.project._
import io.mehitabel.fts.project.ProjectSettings._

lazy val rootSettings = Seq(
  organization := "io.mehitabel",
  scalaVersion := "2.12.3",
  version := "0.1.0",
  scalacOptions := commonScalacOptions
)

lazy val fts = project.in(file("."))
  .settings(rootSettings: _*)
  .dependsOn(
    cli
  )
  .aggregate(core, cli)

lazy val core = project
  .settings (rootSettings: _*)
  .settings(libraryDependencies ++= Dependencies.ftsCore)

lazy val cli = project.dependsOn(core)
  .settings (rootSettings: _*)
  .settings(libraryDependencies ++= Dependencies.ftsCli)


mainClass in assembly := Some("io.mehitabel.fts.MainApp")

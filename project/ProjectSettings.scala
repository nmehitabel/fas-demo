package io.mehitabel.fts.project

import sbt._
import sbt.Keys._

object ProjectSettings {

  lazy val commonScalacOptions = Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard",
    "-language:higherKinds"
  )

  lazy val warnUnusedImport = Seq(
    scalacOptions += "-Ywarn-unused-import",
    scalacOptions in (Compile, console) ~= (_.filterNot("-Ywarn-unused-import" == _)),
    scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
  )
}

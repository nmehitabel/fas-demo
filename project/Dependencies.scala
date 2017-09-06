package io.mehitabel.fts.project

import sbt._

object Dependencies {
  import Grouping._
  object Version {
    val cats = "1.0.0-MF"
    val scalatest = "3.0.1"
    val scalacheck = "1.13.4"
    val betterFiles = "3.0.0"
  }

  lazy val ftsCore = {cats ++ test}
  lazy val ftsCli = {cats ++ files ++ test}

  object Grouping {
    val cats = Seq(
      "org.typelevel" %% "cats-core"   % Version.cats,
      "org.typelevel" %% "cats-effect" % "0.4"
    )
    val files = Seq (
      "com.github.pathikrit" %% "better-files" % Version.betterFiles
    )
    val test = Seq(
      "org.scalatest"  %% "scalatest"             % Version.scalatest,
      "org.scalacheck" %% "scalacheck"            % Version.scalacheck
    ).map(_ % Test)
  }
}

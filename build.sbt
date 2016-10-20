import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin.autoImport._
import com.typesafe.sbt.pgp.PgpKeys

val Org = "org.scoverage"
val MockitoVersion = "1.10.19"
//val ScalatestVersion = "3.0.0"

val appSettings = Seq(
    organization := Org,
    scalaVersion := "2.11.8",
    crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0-RC1"),
    fork in Test := false,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    parallelExecution in Test := false,
    scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8"),
    concurrentRestrictions in Global += Tags.limit(Tags.Test, 1),
    publishTo <<= version {
      (v: String) =>
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("-SNAPSHOT"))
          Some(Resolver.sonatypeRepo("snapshots"))
        else
          Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := {
      <url>https://github.com/scoverage/scalac-scoverage-runtime-sjs</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:scoverage/scalac-scoverage-runtime-sjs.git</url>
          <connection>scm:git@github.com:scoverage/scalac-scoverage-runtime-sjs.git</connection>
        </scm>
        <developers>
          <developer>
            <id>sksamuel</id>
            <name>Stephen Samuel</name>
            <url>http://github.com/sksamuel</url>
          </developer>
        </developers>
    },
    pomIncludeRepository := {
      _ => false
    }
  ) ++ Seq(
    releaseCrossBuild := true,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value
  )

lazy val root = Project("scalac-scoverage-runtime-sjs06", file("."))
    .enablePlugins(ScalaJSPlugin)
    .settings(name := "scalac-scoverage-runtime")
    .settings(appSettings: _*)
    .settings(
      scalaJSStage := FastOptStage/*, GS - remove this (rewrite tests for JUnit)
      libraryDependencies += "org.scalatest" %% "scalatest_sjs0.6" % ScalatestVersion % "test"*/
    )

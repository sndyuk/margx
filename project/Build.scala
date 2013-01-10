import sbt._
import Keys._
import scala.Console

object ApplicationBuild extends Build {

  println(Console.RED + """
                          |    _______   ______  ___    ____  ______
                          |   / ____/ | / / __ \/   |  / __ \/_  __/
                          |  / __/ /  |/ / /_/ / /| | / /_/ / / /
                          | / /___/ /|  / _, _/ ___ |/ ____/ / /
                          |/_____/_/ |_/_/ |_/_/  |_/_/     /_/
                        """.stripMargin + Console.RESET)

  val appScalaVersion = "2.10.0"

  // --- Setting keys
  val appResolvers = Seq(
    "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases",
    "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
    "Java.net" at "http://download.java.net/maven/2")

  val _appScalaOptions = Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    //"-optimize",
    //"-Xprint:jvm",
    "-Ymacro-debug-lite",
    "-encoding", "utf-8")

  val appScalaOptions = Seq("-encoding", "UTF-8", "-target:jvm-1.6", "-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Ywarn-adapted-args")
  val javacOptions = Seq("-source", "1.6", "-target", "1.6", "-Xlint:unchecked", "-Xlint:deprecation")


  val appDependencies = Seq(
    "com.typesafe" %% "scalalogging-slf4j" % "1.0.0",
    "org.slf4j" % "slf4j-api" % "1.7.1",
    "ch.qos.logback" % "logback-classic" % "1.0.7",
    "com.typesafe.akka" %% "akka-actor" % "2.1.0",
    "com.typesafe.akka" %% "akka-remote" % "2.1.0",
    "com.typesafe" % "config" % "1.0.0",
    "org.scala-lang" % "scala-reflect" % "2.10.0")

  lazy val root = Project(id = "margx",
    base = file(".")).settings(
    scalaVersion := appScalaVersion
  ).aggregate(macros, board, player, akka_sample_base, akka_sample_remote)

  lazy val macros = Project(
    id = "margx-macros",
    base = file("modules/macro")
  ).settings(
    scalaVersion := appScalaVersion,
    scalacOptions ++= appScalaOptions,

    libraryDependencies ++= appDependencies,
    resolvers ++= appResolvers
  )

  lazy val board = Project(
    id = "margx-board",
    base = file("modules/board")).settings(

    // --- Scala
    scalaVersion := appScalaVersion,
    scalacOptions ++= appScalaOptions,

    libraryDependencies ++= appDependencies,
    resolvers ++= appResolvers
  ).dependsOn(macros)

  lazy val player = Project(
    id = "margx-player",
    base = file("modules/player")).settings(

    // --- Scala
    scalaVersion := appScalaVersion,
    scalacOptions ++= appScalaOptions,

    libraryDependencies ++= appDependencies,
    resolvers ++= appResolvers
  ).dependsOn(macros, board)


  lazy val akka_sample_base = Project(
    id = "akka_sample_base",
    base = file("modules/akka_sample_base")).settings(

    // --- Scala
    scalaVersion := appScalaVersion,
    scalacOptions ++= appScalaOptions,

    libraryDependencies ++= appDependencies,
    resolvers ++= appResolvers
  )

  lazy val akka_sample_remote = Project(
    id = "akka_sample_remote",
    base = file("modules/akka_sample_remote")).settings(

    // --- Scala
    scalaVersion := appScalaVersion,
    scalacOptions ++= appScalaOptions,

    libraryDependencies ++= appDependencies,
    resolvers ++= appResolvers
  )
}


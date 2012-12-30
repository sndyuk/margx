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
     "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases",
     "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
     "Java.net"               at "http://download.java.net/maven/2")

  val appScalaOptions = Seq(
      "-deprecation",
      "-unchecked",
      "-feature",
      //"-optimize",
      //"-Xprint:jvm",
      "-Ymacro-debug-lite",
      "-encoding", "utf-8")
 
  val appDependencies = Seq(
      "com.typesafe" %% "scalalogging-slf4j" % "1.0.0",
      "org.slf4j" % "slf4j-api" % "1.7.1",
      "ch.qos.logback" % "logback-classic" % "1.0.7",
      "com.typesafe.akka" %% "akka-actor" % "2.1.0",
      "org.scala-lang" % "scala-reflect" % "2.10.0")

  lazy val root = Project(id = "margx",
    base = file(".")).settings(
    scalaVersion := appScalaVersion
  ).aggregate(macro, board, player)

  lazy val macro = Project(
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
  ).dependsOn(macro)

  lazy val player = Project(
    id = "margx-player", 
    base = file("modules/player")).settings(

    // --- Scala
    scalaVersion := appScalaVersion,
    scalacOptions ++= appScalaOptions,
    
    libraryDependencies ++= appDependencies,
    resolvers ++= appResolvers
  ).dependsOn(macro, board)
}


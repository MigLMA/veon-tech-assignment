name := "veon-tech-assignment"

version := "1.0"

scalaVersion := "2.12.2"

organization := "com.evans"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

val akkaVersion = "2.4.17"
val akkaHttpVersion = "10.0.6"
val scalaTestV = "3.0.1"

libraryDependencies ++= {
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",

    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "org.scalatest" %% "scalatest" % scalaTestV % Test,
    "org.scalacheck" %% "scalacheck" % "1.13.4" % Test
  )
}


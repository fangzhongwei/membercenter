name := """membercenter"""

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.lawsofnature.member" % "memberclient_2.11" % "1.0-SNAPSHOT",
  "com.lawsofnature.common" % "common-ice_2.11" % "1.0",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0-M1",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.slick" %% "slick" % "3.2.0-M1",
  "net.codingwell" %% "scala-guice" % "4.0.1",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "com.typesafe.slick" % "slick-codegen_2.11" % "3.2.0-M1" % "test"
)
// https://mvnrepository.com/artifact/org.reactivestreams/reactive-streams
libraryDependencies += "org.reactivestreams" % "reactive-streams" % "1.0.0"
libraryDependencies += "com.lawsofnature.common" % "common-error_2.11" % "1.0"

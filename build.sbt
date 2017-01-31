lazy val commonSettings = Seq(
  javacOptions ++= Seq("-encoding", "UTF-8"),
  organization := "com.jxjxgo.member",
  version := "1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "com.jxjxgo.common" % "common-finagle-thrift_2.11" % "1.0",
    "com.lawsofnature.common" % "common-edecrypt_2.11" % "1.0"
  )
)

lazy val membercommonlib = (project in file("membercommonlib")).settings(commonSettings: _*).settings(
  name := """membercommonlib""",
  libraryDependencies ++= Seq(
  )
)

lazy val memberserver = (project in file("memberserver")).settings(commonSettings: _*).settings(
  name := """memberserver""",
  libraryDependencies ++= Seq(
    "com.jxjxgo.member" % "membercommonlib_2.11" % "1.0",
    "mysql" % "mysql-connector-java" % "5.1.36",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0-M2",
    "net.codingwell" %% "scala-guice" % "4.1.0",
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "com.jxjxgo.account" % "accountcommonlib_2.11" % "1.0",
    "org.reactivestreams" % "reactive-streams" % "1.0.0",
    "com.lawsofnature.common" % "common-error_2.11" % "1.0",
    "com.lawsofnature.common" % "common-mysql_2.11" % "1.0",
    "com.jxjxgo.edcenter" % "edclient_2.11" % "1.0",
    "com.jxjxgo.common" % "common-utils_2.11" % "1.0",
    "org.springframework.security" % "spring-security-crypto" % "4.2.0.RELEASE",
    "com.typesafe.slick" % "slick-codegen_2.11" % "3.2.0-M2" % "test"
  )
)

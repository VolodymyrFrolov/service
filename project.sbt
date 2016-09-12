organization in Global := "kafka.console"

scalaVersion in Global := "2.11.8"

assembly := null

lazy val root    = project.in(file(".")).aggregate(core, service)

lazy val core    = project

lazy val service = project.dependsOn(core % "test->test;compile->compile")

settings.common

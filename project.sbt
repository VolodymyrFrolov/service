organization in Global := "kafka.console"

scalaVersion in Global := "2.11.8"

lazy val root    = project.in(file(".")).aggregate(core, service)

lazy val core    = project

lazy val service = project.dependsOn(core % "test->test;compile->compile")

settings.nonService

settings.common

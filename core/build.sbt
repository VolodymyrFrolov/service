import sbt._, Keys._
import dependencies._

libraryDependencies ++= Seq(
  scalaz.core, scalaz.streams,
  logback,
  journal,
  tconfig,
  _test(scalaTest)
)

settings.nonService

settings.common

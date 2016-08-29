import sbt._, Keys._
import dependencies._

libraryDependencies ++= Seq(
  scalaz.core, scalaz.streams,
  logback,
  _test(scalaTest)
)

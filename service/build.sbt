import sbt._, Keys._
import dependencies._

libraryDependencies ++= Seq(
  scalaz.core, scalaz.streams,
  kafka.clients,
  logback,
  journal,
  tconfig,
  http4s.dsl, http4s.server, http4s.client,
  _test(scalaTest)
)

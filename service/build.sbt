import sbt._, Keys._
import dependencies._

libraryDependencies ++= Seq(
  scalaz.core, scalaz.streams,
  kafka.clients,
  logback,
  journal,
  tconfig,
  argonaut,
  http4s.dsl, http4s.server, http4s.client, http4s.argo,
  _test(scalaTest)
)

assemblyJarName in assembly := s"${organization.value}.${version.value}.jar"

settings.common
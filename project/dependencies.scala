import sbt._, Keys._

object dependencies {

  val repositories = Seq(
    "Typesafe"           at "http://repo.typesafe.com/typesafe/releases/",
    "Oncue Bintray Repo" at "http://dl.bintray.com/oncue/releases"
  )

  def _provided (m: ModuleID): ModuleID = m % "provided"
  def _test     (m: ModuleID): ModuleID = m % "test"

  object Versions {
    val scalaz    = "7.2.4"
    val streams   = "0.8"
    val kafka     = "0.9.0.0"
    val scalaTest = "2.2.6"
    val logback   = "1.1.2"
    val http4s    = "0.14.5a"
    val typesafe  = "1.2.1"
    val journal   = "2.2.1"
  }

  object scalaz {
    val core    = "org.scalaz"        %% "scalaz-core"    % Versions.scalaz
    val streams = "org.scalaz.stream" %% "scalaz-stream"  % Versions.streams
  }

  object kafka {
    val clients = "org.apache.kafka"   % "kafka-clients"  % Versions.kafka
  }

  val scalaTest = "org.scalatest"     %% "scalatest"       % Versions.scalaTest
  val logback   = "ch.qos.logback"    %  "logback-classic" % Versions.logback
  val tconfig   = "com.typesafe"      %  "config"          % Versions.typesafe
  val journal   = "oncue.journal"     %% "core"            % Versions.journal

  object http4s {
    val dsl     = "org.http4s"        %% "http4s-dsl"          % Versions.http4s
    val server  = "org.http4s"        %% "http4s-blaze-server" % Versions.http4s
    val client  = "org.http4s"        %% "http4s-blaze-client" % Versions.http4s
  }

}

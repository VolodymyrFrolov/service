package kafka.console
package service

import journal.Logger
import org.http4s.server.ServerApp

object Main extends ServerApp {

  import logging._

  implicit val log = Logger[this.type]

  override def server(args: List[String]) = for {
    _  <- info("Starting web-console...")
    cf <- config.load
    _  <- describe(cf)
    sc <- config.service(cf)
    ac <- config.app(cf)
    ct <- Context.container(ac)
    s  <- Server.start(sc, Application.instance, ct)
  } yield s

}

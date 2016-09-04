package kafka.console.service

import journal.Logger
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder
import config.ServiceInfo
import kafka.console.core.Container

object Server {

  val log = Logger[this.type]

  def start(info: ServiceInfo, controller: Controller, container: Container) = {

    val service = HttpService { controller andThen { a => a(container) } }

    BlazeBuilder.bindHttp(host = info.host, port = info.port)
                .mountService(service, "/")
                .start
  }
}

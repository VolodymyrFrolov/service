package kafka.console.service

import com.typesafe.config.{Config, ConfigFactory}

import scalaz.concurrent.Task

package object config {

  def load = Task.delay {
    ConfigFactory.load().getConfig("web-console")
  }

  def service(conf: Config) = Task.delay {
    ServiceInfo(
      host = conf.getString("service.host"),
      port = conf.getInt("service.port")
    )
  }
  def app(conf: Config) = Task.delay {
    AppInfo()
  }


}

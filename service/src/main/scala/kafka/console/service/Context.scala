package kafka.console
package service

import kafka.console.service.app._

import scalaz.concurrent.Task
import config._
import core.Container

object Context {

  def container(info: AppInfo) = Task.now {
    Container(
      topics   = new AppTopicService(info),
      security = new AppSecurityService
    )
  }

}

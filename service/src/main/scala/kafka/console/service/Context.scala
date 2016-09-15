package kafka.console
package service

import kafka.console.model.Token
import kafka.console.service.app.AppTopicService

import scalaz.concurrent.Task
import config._
import core.Container
import core.services.{SecurityService}

object Context {

  def container(info: AppInfo) = Task.now {
    Container(
      topics = new AppTopicService(info),
      security = new SecurityService {
        override def check (token: Token) = Task.delay { token }
      }
    )
  }

}

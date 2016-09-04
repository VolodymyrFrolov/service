package kafka.console
package service

import scalaz.concurrent.Task

import config._
import core.Container
import core.services.TopicService
import model.topics.Topic

object Context {

  def container(info: AppInfo) = Task.delay {
    Container(
      topics = new TopicService {
        override def getTopics: Task[Topic] = ???
      }
    )
  }

}

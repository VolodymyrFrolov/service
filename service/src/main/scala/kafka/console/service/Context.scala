package kafka.console
package service

import scalaz.concurrent.Task
import config._
import core.Container
import core.services.{SecurityService, TopicService}
import kafka.console.model.Token
import model.topics.Topic

import scala.util.Random

object Context {

  def container(info: AppInfo) = Task.delay {
    Container(
      topics = new TopicService {
        private val generator = new Random()
        override def getTopics = Task.delay {
          Stream.continually {
            Topic(
              name = s"topic-${generator.nextInt()}",
              partitions = 1,
              replicationFactor = 1
            )
          }.take(generator.nextInt(100)).toVector
        }
      },
      security = new SecurityService {
        override def check (token: Token) = Task.delay { token }
      }
    )
  }

}

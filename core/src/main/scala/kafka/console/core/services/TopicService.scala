package kafka.console
package core.services

import model.topics._

import scalaz.concurrent.Task

trait TopicService {

  def getTopics: Task[Topic]

}

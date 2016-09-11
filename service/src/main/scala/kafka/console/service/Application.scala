package kafka.console
package service

import journal.Logger

import org.http4s.dsl._

object Application {

  import content._, codecs._
  import syntax._
  import errors._
  import app._

  implicit val logger = Logger[this.type]

  private val status: Controller = raw {
    case GET -> Root / "status" => Ok("works just fine")
  }

  private val topics: Controller = exec {
    case GET -> Root / "topics" => topicService andThen getTopics
  }

  val instance: Controller = status orElse topics
}

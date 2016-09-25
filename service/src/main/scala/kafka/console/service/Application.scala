package kafka.console
package service

import journal.Logger
import kafka.console.core.services.{ListPartitions, GetTopicDetails, ListTopics}
import org.http4s.dsl._

import kafka.console.app._
import org.http4s._

object Application {

  import content._, codecs._
  import syntax._
  import errors._
  import app._

  implicit val logger = Logger[this.type]

  private val status = raw {
    case GET -> Root / "status" => Ok("works just fine")
  }

  private val topics = raw {
    case GET -> Root / "topics" =>
      for {
        _ <- info("Requesting topics list")
        service <- topicService
        t <- service(ListTopics())
        r <- Ok(t)
      } yield r

    case GET -> Root / "topics" / name =>
      for {
        _ <- info(s"Requesting topic '$name' details")
        service <- topicService
        r <- service(GetTopicDetails(name))
        c <- okOrNotFound(r)
      } yield c

    case GET -> Root / "topics" / name / "partitions" =>
      for {
        _ <- info(s"Requesting topic '$name' partitions")
        service <- topicService
        r <- service(ListPartitions(name))
        c <- okOrNotFound(r)
      } yield c
  }

  private val html = raw {
    case GET -> Root / "html" => {
      import scalaz.concurrent.Task

      StaticFile.fromResource("/webpage.html").fold(NotFound())(Task.now)
    }
  }

  private val authenticated = auth {
    case GET -> Root / "auth" / "topics" =>
      token => for {
        _ <- info(s"request to secured resource with token $token")
        service <- topicService
        r <- service(ListTopics())
      } yield r
  }

  val instance: Controller = status orElse topics orElse authenticated orElse html
}

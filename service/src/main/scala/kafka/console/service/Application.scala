package kafka.console
package service

import journal.Logger
import kafka.console.core.services.{GetTopicDetails, ListTopics}
import org.http4s.Response
import org.http4s.dsl._
import scalaz.concurrent._

import kafka.console.app._
import org.http4s.headers.`Content-Type`

object Application {

  import content._, codecs._
  import syntax._
  import errors._
  import app._

  implicit val logger = Logger[this.type]

  private val status = raw {
    case GET -> Root / "status" => Ok("works just fine")
  }

  private val topics = exec {
    case GET -> Root / "topics" =>
      for {
        _ <- info("Requesting topics list")
        service <- topicService
        r <- service(ListTopics())
      } yield r
  }

  private val topicDetails = raw {
    case GET -> Root / "topics" / name =>
      for {
        _ <- info(s"Requesting topic '$name' details")
        service <- topicService
        r <- service(GetTopicDetails(name))
        c <- r.fold(Task.now { Response(NotFound) })(Ok(_))
      } yield c
  }

  private val html = raw {
    case GET -> Root / "html" => {
      import scalaz.stream.io

      Ok(io.linesR(getClass.getResourceAsStream("/webpage.html")))
        .withContentType(Some(`Content-Type`(org.http4s.MediaType.`text/html`)))
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

  val instance: Controller = status orElse topics orElse topicDetails orElse authenticated orElse html
}

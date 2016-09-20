package kafka.console
package service

import journal.Logger
import org.http4s.dsl._

import scalaz.Kleisli
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
       topicService andThen getTopics
  }

  private val html = raw {
    case GET -> Root / "html" =>
      Ok(scalaz.stream.io.linesR(getClass.getClassLoader.getResource("webpage.html").getPath))
        .withContentType(Some(`Content-Type`(org.http4s.MediaType.`text/html`)))
  }

  private val authenticated = auth {
    case GET -> Root / "auth" / "topics" =>
      token => for {
        _ <- info(s"request to secured resource with token $token")
        r <- topicService andThen getTopics
      } yield r
  }

  val instance: Controller = status orElse topics orElse authenticated orElse html
}

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

  private val status = raw {
    case GET -> Root / "status" => Ok("works just fine")
  }

  private val topics = exec {
    case GET -> Root / "topics" => topicService andThen getTopics
  }

  private val authenticated = auth {
    case GET -> Root / "auth" / "topics" =>
      token => for {
        _ <- info(s"request to secured resource with token $token")
        r <- topicService andThen getTopics
      } yield r
  }

  val instance: Controller = status orElse topics orElse authenticated
}

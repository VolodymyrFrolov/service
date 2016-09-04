package kafka.console.service

import journal.Logger

import org.http4s.dsl._

import syntax._
import errors._

object Application {

  implicit val logger = Logger[this.type]

  private val status: Controller = exec {
    case GET -> Root / "status" => Ok("works just fine")
  }

  val instance = status
}

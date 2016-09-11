package kafka.console
package service

import org.http4s.dsl._
import journal.Logger
import org.http4s.{Challenge, Response}

import scalaz.concurrent.Task

object errors {

  import exceptions._
  import logging._

  implicit def default(implicit log: Logger): ErrorHandler = {
    case unexpected =>
      log.warn(s"unexpected exception was thrown during the request execution: ${describe(unexpected)}")
      InternalServerError()
  }

  def authentication: ErrorHandler = {
    case authFail: AuthException =>
      Task.delay { Response(Unauthorized) }
  }

}

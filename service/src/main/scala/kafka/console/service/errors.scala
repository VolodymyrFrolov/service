package kafka.console
package service

import org.http4s.dsl._
import journal.Logger
import logging._

object errors {

  implicit def default(implicit log: Logger): ErrorHandler = {
    case unexpected =>
      log.warn(s"unexpected exception was thrown during the request execution: ${describe(unexpected)}")
      InternalServerError()
  }

}

package kafka.console
package service.app

import core.services._
import kafka.console.exceptions.AuthException
import kafka.console.model.Token

import scalaz.concurrent.Task

final class AppSecurityService extends SecurityService {

  override def apply[A] (fa: SecurityOps[A]) = fa match {
    case CheckToken(token) => for {
      _ <- container
      _ <- check(token)
    } yield ()
  }

  private def check(token: Token) = if (token.length > 10) { Task.now(()) } else {
    Task.fail(AuthException("Token is not correct"))
  }
}

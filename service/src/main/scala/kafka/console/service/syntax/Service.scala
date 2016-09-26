package kafka.console
package service.syntax

import scalaz._
import Scalaz._
import scalaz.concurrent.Task
import org.http4s._
import dsl._
import kafka.console.core.services.CheckToken
import org.http4s.util.CaseInsensitiveString

trait Service {

  import app._
  import service._

  implicit def liftResponse (value: Task[Response]): RuntimeK[Response] = value.liftKleisli

  def raw (body: Controller)(implicit handler: ErrorHandler): Controller = {
    case req if body.isDefinedAt(req) => for {
      c <- container
      r <- body(req)(c).handleWith(handler)
    } yield r
  }

  def exec[A : EntityEncoder] (body: TypedController[A])(implicit H: ErrorHandler): Controller = {
    case req if body.isDefinedAt(req) => for {
      c <- container
      r <- coreExec[A](body,req).apply(c).handleWith(H)
    } yield r
  }

  private def coreExec[A : EntityEncoder](body: TypedController[A], req: Request) = for {
    c <- container
    v <- body(req)(c)
    r <- Ok(v)
  } yield r

  def auth[A : EntityEncoder] (body: AuthController[A])(implicit H: ErrorHandler): Controller = {
    case req if body.isDefinedAt(req) => req.headers.get(CaseInsensitiveString(content.headers.AUTH))
        .fold(unauthorized) {
          header => for {
            c <- container
            v <- authCore[A](body)(req, header).apply(c).handleWith(errors.authentication orElse H)
          } yield v
        }
  }

  def authCore[A : EntityEncoder](body: AuthController[A])(req: Request, header: Header) = for {
    token    <- content.headers.tokenFrom(header)
    service  <- security
    _        <- service(CheckToken(token))
    value    <- body(req)(token)
    result   <- Ok(value)
  } yield result
}

package kafka.console
package service.syntax

import scalaz._
import Scalaz._
import scalaz.concurrent.Task
import org.http4s.{EntityEncoder, Request, Response}
import org.http4s.dsl._

trait Service {

  import app._
  import core._
  import service._

  def raw (body: Controller)(implicit handler: ErrorHandler): Controller = {
    case req if body.isDefinedAt(req) => for {
      c <- container
      r <- body(req)(c).handleWith(handler)
    } yield r
  }

  def exec[A : EntityEncoder] (body: PartialFunction[Request, RuntimeK[A]])(implicit H: ErrorHandler): Controller = {
    case req if body.isDefinedAt(req) => for {
      c <- container
      r <- coreExec[A](body,req).apply(c).handleWith(H)
    } yield r
  }

  private def coreExec[A : EntityEncoder](body: PartialFunction[Request, RuntimeK[A]], req: Request) = for {
    c <- container
    v <- body(req)(c)
    r <- Ok(v)
  } yield r

  implicit def liftResponse (value: Task[Response]): RuntimeK[Response] = value.liftKleisli

}

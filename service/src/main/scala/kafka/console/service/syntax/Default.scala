package kafka.console
package service.syntax

import core._
import service._
import org.http4s._

import scalaz.Kleisli

trait Default {

  def exec(body: PartialFunction[Request, RuntimeK[Response]])(implicit errorHandler: ErrorHandler): Controller = {
    case req if body.isDefinedAt(req) => Kleisli {
      (c: Container) => body(req)(c).handleWith(errorHandler)
    }
  }

}

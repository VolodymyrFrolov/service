package kafka.console

import org.http4s._

import scalaz._, syntax.kleisli._
import scalaz.concurrent.Task

package object service {

  import model._

  type Controller         = PartialFunction[Request, RuntimeK[Response]]

  type TypedController[A] = PartialFunction[Request, RuntimeK[A]]

  type AuthController[A]  = PartialFunction[Request, Token => RuntimeK[A]]

  type ErrorHandler       = PartialFunction[Throwable, Task[Response]]


}

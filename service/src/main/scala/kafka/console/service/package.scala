package kafka.console

import org.http4s.{Request, Response}

import scalaz.concurrent.Task

package object service {

  type Controller = PartialFunction[Request, RuntimeK[Response]]

  type ErrorHandler = PartialFunction[Throwable, Task[Response]]


}

package kafka.console
package service.syntax

import org.http4s._, dsl._

import scalaz.concurrent.Task

trait Http {

  def unauthorized: RuntimeK[Response] = Task.delay { Response(Unauthorized) }

}

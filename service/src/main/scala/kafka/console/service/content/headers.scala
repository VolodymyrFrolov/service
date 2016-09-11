package kafka.console
package service.content

import java.util.UUID

import org.http4s.Header

import scalaz.syntax.kleisli._
import scalaz.concurrent.Task

object headers {

  import model._

  val AUTH: CharSequence = "token"

  def tokenFrom(value: Header): RuntimeK[Token] = Task.delay { UUID.fromString(value.value) }.liftKleisli

}

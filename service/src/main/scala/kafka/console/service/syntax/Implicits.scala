package kafka.console.service.syntax

import kafka.console.RuntimeK
import org.http4s.Response

import scalaz.concurrent.Task

trait Implicits {

  import scalaz._, Scalaz._

  implicit def liftT(value: Task[Response]): RuntimeK[Response] = value.liftKleisli

}

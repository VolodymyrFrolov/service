package kafka

import scalaz.Kleisli, scalaz.syntax.kleisli._
import scalaz.concurrent.Task

package object console {

  import core._, services._

  type RuntimeK[U] = Kleisli[Task, Container, U]

  type TopicsK[U]  = Kleisli[Task, TopicService, U]

  val container: RuntimeK[Container] = Kleisli.ask

  implicit def wrapK[A](value: Task[A]): RuntimeK[A] = value.liftKleisli

}

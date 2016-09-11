package kafka.console
package service
package syntax

import journal.Logger

import scalaz.concurrent.Task

trait Logging {

  import scalaz._, syntax.kleisli._

  private implicit def liftTask(value: Task[Unit]): RuntimeK[Unit] = value.liftKleisli

  def info(message: String)(implicit L: Logger): RuntimeK[Unit] = Task.delay { L.info(message) }

}

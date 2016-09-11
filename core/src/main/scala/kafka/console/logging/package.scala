package kafka.console

import journal.Logger

import scalaz._, syntax.show._
import scalaz.concurrent.Task

package object logging extends LogDescriptors {

  def info(message: String)(implicit log: Logger) = Task.delay {
    log.info (message)
  }

  def warn(message: String)(implicit log: Logger) = Task.delay {
    log.warn (message)
  }

  def describe[A : Show](item: A)(implicit l: Logger) = info(item.shows)
}

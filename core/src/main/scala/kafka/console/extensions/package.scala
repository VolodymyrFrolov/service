package kafka.console

import java.io.Closeable
import javax.management.MBeanServerConnection
import javax.management.remote.{JMXConnectorFactory, JMXServiceURL}

import scalaz.{-\/, \/-}
import scalaz.concurrent.Task

package object extensions extends Conversions with MBeanServerConnectionExtensions {
  implicit final class TaskExtensions(val self: Task.type) extends AnyVal {

    def use[A,B](obj: => A)(close: A => Unit)(body: A => Task[B]) =
      Task.delay(obj).flatMap { body }.attempt.flatMap {
        case \/-(content) => close(obj); Task.now(content)
        case -\/(failure) => close(obj); Task.fail(failure)
      }

    def bracket[A<:Closeable,B](obj: A)(body: A => Task[B]) =
      Task.now(obj).flatMap{body}.attempt.flatMap{
        case \/-(content) => obj.close(); Task.now(content)
        case -\/(failure) => obj.close(); Task.fail(failure)
      }
  }
}

package kafka.console
package app


import scalaz.Kleisli
import scalaz.concurrent.Task

trait Container {

  import core.services._

  val container: RuntimeK[core.Container] = Kleisli.ask

  val topicService: RuntimeK[TopicService] = container.andThenK {
    c => Task.delay { c.topics }
  }

  val monitoringService: RuntimeK[MonitoringService] = container.andThenK {
    c => Task.delay {c.monitoring}
  }

  val security: RuntimeK[SecurityService]  = container.andThenK {
    c => Task.delay { c.security }
  }
}

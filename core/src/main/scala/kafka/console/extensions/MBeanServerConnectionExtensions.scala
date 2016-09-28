package kafka.console.extensions

import javax.management.MBeanServerConnection
import javax.management.remote.{JMXConnectorFactory, JMXServiceURL}

import scalaz.concurrent.Task

trait MBeanServerConnectionExtensions {
  // TODO: [EC] - pass connection parameters from configuration
  def doWithConnection[T](fn: MBeanServerConnection => Task[T]) =
    Task.bracket {
      JMXConnectorFactory.connect(new JMXServiceURL(s"service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi"))
    } { jmxcf => fn(jmxcf.getMBeanServerConnection()) }
}

package kafka.console
package core

import scalaz._

package object services {

  type SecurityService = SecurityOps ~> RuntimeK

  type TopicService = TopicOps ~> RuntimeK

  type MonitoringService = MonitoringOps ~> RuntimeK
}

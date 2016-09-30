package kafka.console
package core.providers.monitoring

import javax.management.{ObjectInstance, ObjectName}

import extensions._
import model.monitoring.MBeanMetricInfo

import scalaz.concurrent.Task
import scalaz._, Scalaz._

object MBeanInfoProvider {
  def getAggregates = {
    doWithConnection { mbcs => Task.delay {
      mbcs.queryMBeans(null, null).toArray()
        .map(_.asInstanceOf[ObjectInstance])
        .map(_.getObjectName())
        .filter(_.getDomain.startsWith("kafka."))
        .map(a => MBeanMetricInfo(a, a.getDomain, getMetricType(a), extractSortedCanonicalKeys(a), mbcs.getMBeanInfo(a)))
    }
    }
  }

  private def extractSortedCanonicalKeys(a: ObjectName) = {
    canonicalNameKeyListToMap(a.getCanonicalKeyPropertyListString)
      .map{case (k, v) => s"$k=$v"}.mkString(",")
  }

  private def getMetricType(a: ObjectName) = canonicalNameKeyListToMap(a.getCanonicalKeyPropertyListString)
    .find{case (k, _) => k === "type"}
    .map{case (_, v) => v}
    .getOrElse("unknown")

  private def canonicalNameKeyListToMap(canonicalName: String) = canonicalName.split(',').sorted.map { a =>
    val Array(left, right) = a.split('=')
    left -> right
  }.toMap
}

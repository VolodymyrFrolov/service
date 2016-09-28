package kafka.console
package core.providers.monitoring

import javax.management.{ObjectInstance, ObjectName}

import extensions._
import model.monitoring.MBeanMetricInfo

import scalaz.concurrent.Task

object MBeanInfoProvider {
  def getAggregates = {
    doWithConnection { mbcs => Task.delay {
      mbcs.queryMBeans(null, null).toArray()
        .map(_.asInstanceOf[ObjectInstance])
        .map(_.getObjectName())
        .filter(_.getDomain.startsWith("kafka."))
        .map(a => MBeanMetricInfo(a, a.getDomain, extractSortedCanonicalKeys(a), mbcs.getMBeanInfo(a)))
    }
    }
  }

  private def extractSortedCanonicalKeys(a: ObjectName) = {
    canonicalNameKeyListToMap(a.getCanonicalKeyPropertyListString)
      .map(a => a._1 + "=" + a._2).mkString(",")
  }

  private def canonicalNameKeyListToMap(canonicalName: String) = canonicalName.split(',').sorted.map { a =>
    val Array(left, right) = a.split('=')
    left -> right
  }.toMap
}

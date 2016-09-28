package kafka.console
package service
package app

import javax.management._

import kafka.console.core.providers.monitoring.MBeanInfoProvider
import kafka.console.core.services.{GetMetrics, MonitoringOps, MonitoringService}
import kafka.console.extensions._
import kafka.console.model.monitoring.{KafkaMetric, MBeanMetricInfo, MetricAttribute}

import scala.math.Ordering._
import scalaz.concurrent.Task

final class AppMonitoringService extends MonitoringService {
  private val aggregates = MBeanInfoProvider.getAggregates

  private def getMetrics(domain: String, mtype: String, keys: Map[String, Option[String]], attributes: List[String]): Task[List[KafkaMetric]] = {
    val fullKeysSet = keys + ("type" -> Some(mtype))
    val canonicalKeysList = fullKeysSet.toList.filter(a => a._2.isDefined).sortBy(a => a._1).map(a => a._1 + "=" + a._2.get).mkString(",")
    val metricSpec = s"$domain:$canonicalKeysList"

    doWithConnection[List[KafkaMetric]](mbcs =>
      for {
        a <- aggregates.map(_.find(a => s"${a.domain}:${a.canonicalName}" == metricSpec))
        l <- convert(a, domain, mtype, mbcs, attributes)
      } yield l
    )
  }

  private def convert(opt: Option[MBeanMetricInfo], domain: String, mtype: String, mbcs: MBeanServerConnection, attributes: List[String]) = Task.delay {
    opt.map {
      a => mbcs.getAttributes(a.name, if (attributes.isEmpty) a.attributesNames else attributes)
        .toArray
        .map(_.asInstanceOf[Attribute])
        .map(a => MetricAttribute(a.getName, a.getValue.toString))
        .toList
    }.map(a => List(KafkaMetric(domain, mtype, a))).getOrElse(Nil)
  }

  override def apply[A](fa: MonitoringOps[A]): RuntimeK[A] = fa match {
    case GetMetrics(domain, mtype, name, attributes) => for {
      _ <- container
      t <- getMetrics(domain, mtype, name, attributes)
    } yield t
  }
}

package kafka.console
package service
package app

import javax.management._

import kafka.console.core.providers.monitoring.MBeanInfoProvider
import kafka.console.core.services._
import kafka.console.extensions._
import kafka.console.model.monitoring.{KafkaMetric, MBeanMetricInfo, MetricAttribute}

import scala.math.Ordering.String
import scalaz.\/
import scalaz.concurrent.Task

final class AppMonitoringService extends MonitoringService {
  private val aggregates = MBeanInfoProvider.getAggregates

  private def getMetrics(domain: String, mtype: Option[String] = None, keys: Map[String, Option[String]] = Map(), attributes: List[String] = Nil): Task[Throwable \/ List[KafkaMetric]] = {
    doWithConnection[List[KafkaMetric]](mbcs =>
      definePotentialMetricsCandidates(domain, mtype, keys).map(l => l.map(b => convertSingle(b, domain, b.mtype, mbcs, attributes)))
    ).attempt
  }

  private def definePotentialMetricsCandidates(domain: String, mtype: Option[String], keys: Map[String, Option[String]]): Task[List[MBeanMetricInfo]] = {
    mtype match {
      case None => aggregates.map(_.filter(a => s"${a.domain}" == domain).toList)
      case Some(t) => aggregates.map(_.filter(a =>
        if (keys.isEmpty) s"${a.domain}" == domain && s"${a.mtype}" == t
        else s"${a.domain}" == domain && s"${a.mtype}" == t && s"${a.canonicalName}" == buildCanonicalKeysList(mtype, keys)).toList
      )
    }
  }

  private def buildCanonicalKeysList(mtype: Option[String], keys: Map[String, Option[String]]) = {
    (keys + ("type" -> mtype))
      .toList.filter(a => a._2.isDefined)
      .sortBy(a => a._1)
      .map(a => a._1 + "=" + a._2.get)
      .mkString(",")
  }

  private def convertSingle(metricInfo: MBeanMetricInfo, domain: String, mtype: String, mbcs: MBeanServerConnection, attributes: List[String]) =
    KafkaMetric(domain, mtype, getMetricAttributes(metricInfo, mbcs, attributes))


  private def getMetricAttributes(metricInfo: MBeanMetricInfo, mbcs: MBeanServerConnection, attributes: List[String]): List[MetricAttribute] = {
    mbcs.getAttributes(metricInfo.name, if (attributes.isEmpty) metricInfo.attributesNames else attributes)
      .toArray
      .map(_.asInstanceOf[Attribute])
      .map(a => MetricAttribute(a.getName, a.getValue.toString))
      .toList
  }

  override def apply[A](fa: MonitoringOps[A]): RuntimeK[A] = fa match {
    case GetMetrics(domain, mtype, name, attributes) => for {
      _ <- container
      t <- getMetrics(domain, Some(mtype), name, attributes)
    } yield t
    case GetMetricsByDomain(domain) => for {
      _ <- container
      t <- getMetrics(domain)
    } yield t
    case GetMetricsByType(domain, mtype) => for {
      _ <- container
      t <- getMetrics(domain, Some(mtype))
    } yield t
  }
}

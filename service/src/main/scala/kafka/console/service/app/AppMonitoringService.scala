package kafka.console.service.app

import javax.management.remote.{JMXConnectorFactory, JMXServiceURL}
import javax.management._

import kafka.console._
import kafka.console.core.services.{GetMetrics, MonitoringOps, MonitoringService}
import kafka.console.model.monitoring.{KafkaMetric, MBeanMetricInfo, MetricAttribute}

import scalaz.concurrent.Task
import scalaz.{-\/, \/-}

// TODO: [EC] - Move out of this file to appropriate namespace
object MBeanInfoHelper {

  // TODO: [EC] - SHALL accept parameters from configuration
  def doWithConnection[T]()(fn: MBeanServerConnection => Task[T]) = {
    for {
      jmxcf <- Task.delay {
        val urlString = s"service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi"
        val url = new JMXServiceURL(urlString)
        JMXConnectorFactory.connect(url)
      }
      p <- Task.delay {
        jmxcf.getMBeanServerConnection()
      }
      r <- fn(p).attempt
      _ <- r.fold(report, _ => Task.delay {
        jmxcf.close()
      })
    } yield r
  }

  private def report(error: Throwable) = {
    Task.delay {
      println(error.toString)
    }
  }

  def getAggregates = {
    doWithConnection()(mbcs => Task.now {
      val beans = mbcs.queryMBeans(null, null).toArray()
      val beansObjNames = beans
        .map(_.asInstanceOf[ObjectInstance])
        .map(_.getObjectName())
        .filter(_.getDomain.startsWith("kafka."))

      beansObjNames
        .map(a =>
          MBeanMetricInfo(a,
            a.getDomain,
            canonicalNameKeyListToMap(a.getCanonicalKeyPropertyListString)
              .map(a => a._1 + "=" + a._2).mkString(","),
            mbcs.getMBeanInfo(a)))
    }).run match {
      case \/-(success) => success
      case -\/(_) => Seq().toArray
    }
  }


  def canonicalNameKeyListToMap(canonicalName: String) = {
    canonicalName.split(',').sortBy(a => a).map(a => {
      val parts = a.split('=')
      val key = parts(0)
      val value = parts(1)
      key -> value
    }).toMap
  }
}

final class AppMonitoringService extends MonitoringService {
  private val aggregates = MBeanInfoHelper.getAggregates

  private def getMetrics(domain: String, mtype: String, keys: Map[String, Option[String]], attributes: List[String]): Task[List[KafkaMetric]] = Task.delay {
    val fullKeysSet = keys + ("type" -> Some(mtype))
    val canonicalKeysList = fullKeysSet.toList.filter(a => a._2.isDefined).sortBy(a => a._1).map(a => a._1 + "=" + a._2.get).mkString(",")
    val metricSpec = s"$domain:$canonicalKeysList"

    MBeanInfoHelper.doWithConnection[List[KafkaMetric]]()(mbcs => Task.now {
      val availableMetricAggregate = aggregates.find(a => s"${a.domain}:${a.canonicalName}" == metricSpec)

      availableMetricAggregate match {
        case Some(aggr) =>
          val attrs = attributes match {
            case Nil => aggr.attributesNames()
            case h :: _ => attributes.toArray
          }
          val attrArr = mbcs.getAttributes(aggr.name, attrs).toArray
          val mappedAttrArr = attrArr.map(a => a.asInstanceOf[Attribute])
          val mappedMetricAttrs = mappedAttrArr.map(a => MetricAttribute(a.getName, a.getValue.toString))
          List(KafkaMetric(domain, mtype, mappedMetricAttrs.toList))
        case _ => Nil
      }

    }).run match {
      case \/-(success) => success
      case -\/(fail) => Nil
    }
  }

  override def apply[A](fa: MonitoringOps[A]): RuntimeK[A] = fa match {
    case GetMetrics(domain, mtype, name, attributes) => for {
      _ <- container
      t <- getMetrics(domain, mtype, name, attributes)
    } yield t
  }
}

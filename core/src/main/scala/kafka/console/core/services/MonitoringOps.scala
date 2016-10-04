package kafka.console.core.services

import kafka.console.model.monitoring.KafkaMetric

import scalaz.\/


sealed trait MonitoringOps[A]

case class GetMetrics(domain: String, mtype: String, keys: Map[String, Option[String]], attributes: List[String]) extends MonitoringOps[Throwable \/ List[KafkaMetric]]

case class GetMetricsByDomain(domain: String) extends MonitoringOps[Throwable \/ List[KafkaMetric]]

case class GetMetricsByType(domain: String, mtype: String) extends MonitoringOps[Throwable \/ List[KafkaMetric]]
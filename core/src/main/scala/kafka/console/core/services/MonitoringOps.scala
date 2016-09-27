package kafka.console.core.services

import kafka.console.model.monitoring.{KafkaMetric, MetricAttribute}


sealed trait MonitoringOps[A]

case class GetMetrics(domain: String, mtype: String, keys: Map[String, Option[String]], attributes: List[String]) extends MonitoringOps[List[KafkaMetric]]

package kafka.console.model.monitoring

case class KafkaMetric(domain:String, mtype: String, attributes: List[MetricAttribute])

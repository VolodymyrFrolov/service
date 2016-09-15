package kafka.console.service.config.kafka.client

sealed trait Value

case class Server(endpoint: String)        extends Value
case class Acknowledgement(value: Ack)     extends Value
case class BatchSize(size: Int)            extends Value
case class RetryLimit(retries: Int)        extends Value
case class Linger(timeout: Int)            extends Value
case class BufferMemory(memory: Long)      extends Value


trait ConfigurationOps {
  implicit def extendConfig(config: KafkaConfiguration): ConfigExtensions = new ConfigExtensions(config)

  implicit def extendValue (config: Value): ConfigExtensions =
    extendConfig(KafkaConfiguration(servers = List())) + config

}

final class ConfigExtensions(val input: KafkaConfiguration) extends AnyVal {

  import scala.concurrent.duration._

  def + (that: Value): KafkaConfiguration = that match {
    case Server(endpoint)       => input.copy(servers = endpoint :: input.servers)
    case Acknowledgement(value) => input.copy(ack = value)
    case BatchSize(size)        => input.copy(batchSize = size)
    case Linger(timeout)        => input.copy(linger = timeout milliseconds)
    case BufferMemory(memory)   => input.copy(bufferMemory = memory)
    case RetryLimit(limit)      => input.copy(retries = limit)
  }

}
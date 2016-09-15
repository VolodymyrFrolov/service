package kafka.console.service.app

import kafka.console.core.services.TopicService
import kafka.console.model.topics.Topic
import kafka.console.service.config.AppInfo
import kafka.console.service.config.kafka.client._
import org.apache.kafka.clients.consumer.KafkaConsumer
import scalaz.concurrent.Task
import kafka.console.converters.JavaImplicitConverters._

final class AppTopicService(info: AppInfo) extends TopicService with ToKafkaConfigurationExtensions {
  
  private val consumer = {
    val kc = new KafkaConfiguration(List(s"${info.kafka.host}:${info.kafka.port}"))
    new KafkaConsumer(kc toProperties)
  }

  override def getTopics = Task.delay {
      for {
        (name, pInfo) <- (consumer listTopics).toVector
      }
      yield Topic(
        name,
        pInfo.length,
        pInfo.map(p => p.replicas().length).foldLeft(0)(Math.max))
  }
}

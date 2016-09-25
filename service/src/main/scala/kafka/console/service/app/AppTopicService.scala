package kafka.console
package service.app

import kafka.console.core.services._
import model.topics.{PartitionReplica, TopicDetails, PartitionInfo, Topic}
import service.config.AppInfo
import service.config.kafka.client._
import org.apache.kafka.clients.consumer.KafkaConsumer
import scalaz.concurrent.Task
import converters.JavaImplicitConverters._

final class AppTopicService(info: AppInfo) extends TopicService with ToKafkaConfigurationExtensions {

  private val consumer = {
    val kc = new KafkaConfiguration(List(s"${info.kafka.host}:${info.kafka.port}"))
    new KafkaConsumer(kc toProperties)
  }

  private def getTopics = Task.delay {
    for {
      (name, pInfo) <- (consumer listTopics).toVector
    }
      yield Topic(
        name, pInfo.length, pInfo.map(p => p.replicas().length).foldLeft(0)(Math.max))
  }

  private def getTopicPartitions(topic: String) = Task.delay {
    val partitions = for {
      (n, pInfo) <- (consumer listTopics).toVector if topic == n
      p <- pInfo
      rs = p.inSyncReplicas() toSet
    }
      yield PartitionInfo(
        partition = p.partition(),
        leader = p.leader().id(),
        replicas = p.replicas().toVector map {
          n => PartitionReplica(n.id(), n == p.leader(), rs contains n)
        })

    if (partitions isEmpty) None else Some(partitions)
  }

  private def getTopicDetails(name: String) = for {
    ps <- getTopicPartitions(name)
    d = ps map (TopicDetails(name, _))
  } yield d

  override def apply[A](fa: TopicOps[A]): RuntimeK[A] = fa match {
    case ListTopics() => for {
      _ <- container
      t <- getTopics
    } yield t
    case GetTopicDetails(name) => for {
      _ <- container
      td <- getTopicDetails(name)
    } yield td
    case ListPartitions(name) => for {
      c <- container
      tp <- getTopicPartitions(name)
    } yield tp
  }
}

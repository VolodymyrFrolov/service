package kafka.console
package core.services

import kafka.console.model.topics.{PartitionInfo, TopicDetails, Topic}

sealed trait TopicOps[A]

case object ListTopics extends TopicOps[Vector[Topic]]

case class GetTopicDetails(name: String) extends TopicOps[Option[TopicDetails]]

case class ListPartitions(topic: String) extends TopicOps[Option[Vector[PartitionInfo]]]

case class GetPartition(topic: String, partitionId: Int) extends TopicOps[Option[PartitionInfo]]


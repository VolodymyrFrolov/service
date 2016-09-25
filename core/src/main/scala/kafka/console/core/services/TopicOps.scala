package kafka.console
package core.services

import kafka.console.model.topics.{PartitionInfo, TopicDetails, Topic}

sealed trait TopicOps[A]

case class ListTopics() extends TopicOps[Vector[Topic]]

case class GetTopicDetails(name: String) extends TopicOps[Option[TopicDetails]]

case class ListPartitions(topic: String) extends TopicOps[Option[Vector[PartitionInfo]]]


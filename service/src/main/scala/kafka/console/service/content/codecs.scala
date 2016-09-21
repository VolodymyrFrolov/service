package kafka.console
package service.content

import argonaut._, Argonaut._
import kafka.console.model.topics.{PartitionReplica, TopicDetails, Topic}

object codecs {

  implicit val topicCodec = casecodec3(Topic.apply, Topic.unapply)("name", "partitions", "replicationFactor")

  implicit val replicaCodec = casecodec3(PartitionReplica.apply,PartitionReplica.unapply)("broker", "leader", "inSync")

  implicit val partitionInfoCodec = casecodec3(model.topics.PartitionInfo.apply, model.topics.PartitionInfo.unapply)("partition", "leader", "replicas")

  implicit val topicDetailsCodec = casecodec2(TopicDetails.apply, TopicDetails.unapply)("name", "partitions")
}

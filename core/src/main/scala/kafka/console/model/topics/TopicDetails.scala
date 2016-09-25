package kafka.console.model.topics

case class TopicDetails(name: String, partitions: Vector[PartitionInfo])

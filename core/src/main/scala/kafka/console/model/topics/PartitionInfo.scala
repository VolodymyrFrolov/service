package kafka.console.model.topics

case class PartitionInfo(partition: Int, leader: Int, replicas: Vector[PartitionReplica])

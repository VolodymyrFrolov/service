package kafka.console.model.topics

case class PartitionReplica(broker: Int, leader: Boolean, inSync: Boolean)

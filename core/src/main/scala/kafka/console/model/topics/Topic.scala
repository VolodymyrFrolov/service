package kafka.console.model
package topics

case class Topic(name: String, partitions: Int, replicationFactor: Int)

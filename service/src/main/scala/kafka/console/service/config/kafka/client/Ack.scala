package kafka.console.service.config.kafka.client

object Ack {

  private val _ALL    = "all"
  private val _WEAK   = "0"
  private val _STRONG = "1"

  def ackToString(value: Ack): String = value match {
    case All    => _ALL
    case Weak   => _WEAK
    case Strong => _STRONG
  }
}

/**
 * The number of acknowledgments the producer requires
 * the leader to have received before considering a request complete.
 * This controls the durability of records that are sent.
 */
sealed trait Ack

/**
 * This means the leader will wait for the full set of in-sync replicas to acknowledge the record.
 * This guarantees that the record will not be lost as long as at least one in-sync replica remains alive.
 * This is the strongest available guarantee.
 */
case object All    extends Ack

/**
 * If set to zero then the producer will not wait for any acknowledgment from the server at all.
 * The record will be immediately added to the socket buffer and considered sent.
 * No guarantee can be made that the server has received
 * the record in this case, and the retries configuration will not take effect
 * (as the client won't generally know of any failures).
 * The offset given back for each record will always be set to -1.
 */
case object Weak   extends Ack

/**
 * This will mean the leader will write the record to its local log
 * but will respond without awaiting full acknowledgement from all followers.
 * In this case should the leader fail immediately after acknowledging the record
 * but before the followers have replicated it then the record will be lost.
 */
case object Strong extends Ack
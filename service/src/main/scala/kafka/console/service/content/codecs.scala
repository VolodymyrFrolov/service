package kafka.console
package service.content

import argonaut._, Argonaut._
import model.topics.Topic

object codecs {

  implicit val topicCodec = casecodec3(Topic.apply, Topic.unapply)("name", "partitions", "replicationFactor")

}

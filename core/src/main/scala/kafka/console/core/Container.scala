package kafka.console.core

import services._

final case class Container(topics: TopicService, security: SecurityService)

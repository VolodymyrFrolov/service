package kafka

import scalaz.Kleisli
import scalaz.concurrent.Task

package object console {

  import core._, services._

  type RuntimeK[U] = Kleisli[Task, Container, U]

  type TopicsK[U]  = Kleisli[Task, TopicService, U]

}

package kafka

import scalaz.Kleisli
import scalaz.concurrent.Task

package object console {

  import core._

  type RuntimeK[U] = Kleisli[Task, Container, U]

}

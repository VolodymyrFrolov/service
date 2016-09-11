package kafka.console
package core.services


import scalaz.concurrent.Task

import model._

trait SecurityService {

  def check(token: Token): Task[Token]

}

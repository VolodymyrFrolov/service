package kafka.console
package core.services

import model._

sealed trait SecurityOps[A]

case class CheckToken(value: Token) extends SecurityOps[Unit]

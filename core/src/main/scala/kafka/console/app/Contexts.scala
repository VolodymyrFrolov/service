package kafka.console
package app

import core.Container

import scalaz.Kleisli

trait Contexts {

  val container: RuntimeK[Container] = Kleisli.ask
}

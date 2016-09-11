package kafka.console.exceptions

final case class AuthException(message: String) extends Exception(message)

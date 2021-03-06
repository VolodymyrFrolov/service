package kafka.console.logging

import com.typesafe.config._
import kafka.console.exceptions.AuthException

import scala.compat.Platform.EOL
import scalaz.Show

trait LogDescriptors {

  implicit val showConfig = new Show[Config] {
    override def shows(c: Config) = s"Configuration: ${c.root().render(ConfigRenderOptions.defaults())}"
  }

  implicit val showThrowable = new Show[Throwable] {
    override def shows(t: Throwable) = s"${t.getMessage} \n${t.getStackTrace.mkString("", EOL, EOL)}"
  }

  implicit val showAuthThrowable = new Show[AuthException] {
    override def shows (t: AuthException) = t.getMessage
  }

}

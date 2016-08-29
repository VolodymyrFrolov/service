import sbt._, Keys._

object console {

  import scala.Console._
  import systemprops._

  private val prompt = { (s: State) => s"[${green(Project.extract(s).currentProject.id)}] λ " }

  def green (prefix: String): String = color(GREEN)(prefix)

  def red   (prefix: String): String = color(RED)(prefix)

  def settings = Seq(
    shellPrompt := prompt
  )

  private def color(color: String)(value: String): String = if (ansiSupported) color + value + RESET else value
}

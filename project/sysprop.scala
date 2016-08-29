import sbt._, Keys._

object systemprops {

  private val WINDOWS = "windows"
  private val TRUE    = "true"
  private val FALSE   = "false"

  def isNoFormat  = Option(System.getProperty("sbt.log.noformat")).map(_ != TRUE)

  def isWindows   = os.map(_.toLowerCase).filter(_.contains(WINDOWS)).map(_ => false)

  def os          = Option(System.getProperty("os.name"))

  def ansiSupported = isNoFormat orElse isWindows getOrElse true

}

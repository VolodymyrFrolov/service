import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._

object settings {

  private val compilerFlags = Seq(
    "-deprecation",
    "-Xfuture",
    "-encoding", "UTF-8",
    "-Ywarn-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-inaccessible",
    "-unchecked",
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-Xfatal-warnings"
  )

  private val lintingFlags = Seq(
    "-Xlint:adapted-args",
    "-Xlint:nullary-override",
    "-Xlint:package-object-classes",
    "-Xlint:unsound-match",
    "-Xlint:missing-interpolator",
    "-Xlint:doc-detached",
    "-Xlint:private-shadow",
    "-Xlint:poly-implicit-overload",
    "-Xlint:option-implicit",
    "-Xlint:type-parameter-shadow",
    "-Xlint:delayedinit-select",
    "-Xlint:by-name-right-associative",
    "-Xlint:stars-align",
    "-Xlint:nullary-unit",
    "-Xlint:inaccessible"
  )

  def compiler = Seq(
    scalacOptions in Compile ++= compilerFlags ++ lintingFlags
  )

  def resolution = Seq(
    resolvers ++= dependencies.repositories
  )

  def build = Seq(
    assemblyJarName in assembly := s"${organization.value}.${version.value}.jar"
  )

  def common = console.settings ++ compiler ++ resolution

  def service = build

  def nonService = Seq(assembly := null)
}

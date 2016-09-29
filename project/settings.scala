import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._
import sbtdocker.DockerPlugin.autoImport._
import sbtdocker.mutable.Dockerfile

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

  def dockerSettings = Seq(
      dockerfile in docker := {
        val artifact: File = assembly.value
        val artifactTargetPath = s"/bin/${artifact.name}"
        new Dockerfile {
          from("tcnksm/centos-java")
          maintainer(organization.value)
          add(artifact, artifactTargetPath)
          expose(8080)
          entryPoint("java", "-jar", artifactTargetPath)
        }
      },
    imageNames in docker := Seq(
      ImageName(
        namespace = Some(organization.value.filterNot(_ == '.')),
        repository = organization.value.filterNot(_ == '.'),
        tag = Some("latest")
      )
    ),
    buildOptions in docker := BuildOptions(
      removeIntermediateContainers = BuildOptions.Remove.Always
    )
  )

  def publishing = Seq(
    publishTo := bintray.getPublishResolver(System.getenv("BINTRAY_USER"), System.getenv("BINTRAY_API_KEY"), moduleName.value, false)
  )

  def common = console.settings ++ compiler ++ resolution ++ publishing

  def deploy = dockerSettings
}

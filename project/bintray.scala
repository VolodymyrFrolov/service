import java.net.URL

import bintry.Client
import dispatch.{FunctionHandler, Http}
import org.apache.ivy.core.module.descriptor.Artifact
import org.apache.ivy.plugins.repository.{AbstractRepository, Repository}
import org.apache.ivy.plugins.resolver.IBiblioResolver
import sbt.{RawRepository, Resolver, _}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object bintray {
  case class BintrayMavenRepository(
                                     underlying: Repository,
                                     bty: Client#Repo#Package,
                                     release: Boolean)
    extends AbstractRepository {

    def asStatusAndBody =
      new FunctionHandler({ r => (r.getStatusCode, r.getResponseBody)})

    override def put(artifact: Artifact, src: File, dest: String, overwrite: Boolean): Unit =
      Await.result(
        bty.mvnUpload(transform(dest), src).publish(release)(asStatusAndBody),
        Duration.Inf) match {
        case (201, _) =>
        case (_, fail) =>
          println(fail)
          throw new RuntimeException(s"error uploading to $dest: $fail")
      }

    def getResource(src: String) = underlying.getResource(src)

    def get(src: String, dest: File) = underlying.get(src, dest)

    def list(parent: String) = underlying.list(parent)

    private def transform(dest: String) =
    new URL(dest).getPath.split('/').drop(5).mkString("/")
  }

  case class BintrayMavenResolver(
                                   name: String,
                                   rootURL: String,
                                   bty: Client#Repo#Package,
                                   release: Boolean)
    extends IBiblioResolver {
    setName(name)
    setM2compatible(true)
    setRoot(rootURL)
    override def setRepository(repository: Repository): Unit =
      super.setRepository(BintrayMavenRepository(repository, bty, release))
  }

  def getPublishResolver(user: String, apiKey: String, packageName: String, isRelease: Boolean): Option[Resolver] =
  {
    val repo: Client#Repo = Client(user, apiKey, new Http()).repo(user, "maven")
    val pkg = repo.get(packageName)
    Some(new RawRepository(
      BintrayMavenResolver(s"Bintray-Maven-Publish-${repo.subject}-${repo.repo}-${pkg.name}",
        s"https://api.bintray.com/maven/${repo.subject}/${repo.repo}/${repo.repo}", pkg, isRelease)))
  }
}
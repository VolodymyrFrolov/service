package kafka.console
package service

import journal.Logger
import kafka.console.core.services._
import org.http4s.Response
import org.http4s.dsl._

import scalaz.concurrent._
import kafka.console.app._
import org.http4s._

import scala.collection.immutable.{List, Nil}
import scalaz.concurrent.Task

object Application {

  import content._, codecs._
  import syntax._
  import errors._
  import app._

  implicit val logger = Logger[this.type]

  private val status = raw {
    case GET -> Root / "status" => Ok("works just fine")
  }

  private val topics = raw {
    case GET -> Root / "topics" =>
      for {
        _ <- info("Requesting topics list")
        service <- topicService
        t <- service(ListTopics)
        r <- Ok(t)
      } yield r
  }

  private val monitoring = raw {
    case GET -> Root / "monitoring" / domain => {
      for {
        _ <- info(s"Requesting monitoring by domain: '$domain'")
        service <- monitoringService
        r <- service(GetMetricsByDomain(domain))
        c <- r.fold(err => Task.now(Response(NotFound)), s => Ok(s))
      } yield c
    }

    case GET -> Root / "monitoring" / domain / mtype :? params => {
      val attributes = params.get("attributes").map(s => s.head) match {
        case Some(a) => a.split(",").toList
        case _ => Nil
      }
      val keys = List("name" -> params.get("name").map(s => s.head),
        "client-id" -> params.get("clientId").map(s => s.head)
      ).filter(_._2.isDefined).toMap

      for {
        _ <- info(s"Requesting monitoring: '$domain' -> '$mtype'")
        service <- monitoringService
        r <- service(if (keys.isEmpty) GetMetricsByType(domain, mtype) else GetMetrics(domain, mtype, keys, attributes))
        c <- r.fold(err => Task.now(Response(NotFound)), s => Ok(s))
      } yield c
    }
  }

  private val topicDetails = raw {
    case GET -> Root / "topics" / name =>
      for {
        _ <- info(s"Requesting topic '$name' details")
        service <- topicService
        r <- service(GetTopicDetails(name))
        c <- r.fold(Task.now {
          Response(NotFound)
        })(Ok(_))
      } yield c

    case GET -> Root / "topics" / name / "partitions" =>
      for {
        _ <- info(s"Requesting topic '$name' partitions")
        service <- topicService
        r <- service(ListPartitions(name))
        c <- r.fold(Task.now { Response(NotFound) })(Ok(_))
      } yield c

    case GET -> Root / "topics" / name / "partitions"/ IntVar(partitionId) =>
      for {
        _ <- info(s"Requesting topic '$name' partition '$partitionId'")
        service <- topicService
        r <- service(GetPartition(name, partitionId))
        c <- r.fold(Task.now { Response(NotFound) })(Ok(_))
      } yield c
  }

  private val html = raw {
    case GET -> Root / "html" => {
      import scalaz.concurrent.Task

      StaticFile.fromResource("/webpage.html").fold(NotFound())(Task.now)
    }
  }

  private val authenticated = auth {
    case GET -> Root / "auth" / "topics" =>
      token => for {
        _ <- info(s"request to secured resource with token $token")
        service <- topicService
        r <- service(ListTopics)
      } yield r
  }

  val instance: Controller = status orElse topics orElse topicDetails orElse monitoring orElse authenticated orElse html
}

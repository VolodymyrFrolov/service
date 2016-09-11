package kafka.console.service


import org.http4s.EntityEncoder
import org.http4s.Charset._
import org.http4s.MediaType._
import argonaut._, Argonaut._

package object content {

  private val formatter = PrettyParams.nospace.copy(dropNullKeys = true)

  private val encoder   = EntityEncoder
    .stringEncoder(charset = `UTF-8`)
    .contramap[Json] { c => formatter.pretty(c) }
    .withContentType(`application/json`)

  implicit def jsonEncoderFor[A : EncodeJson]: EntityEncoder[A] = encoder.contramap[A](a => a.jencode)


}

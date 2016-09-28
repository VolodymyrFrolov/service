package kafka.console.service.syntax

import org.http4s.dsl.OptionalQueryParamDecoderMatcher

trait Parameters {
  object NameMatcher extends OptionalQueryParamDecoderMatcher[String]("name")
  object ClientIdMatcher extends OptionalQueryParamDecoderMatcher[String]("clientId")
  object AttributesMatcher extends OptionalQueryParamDecoderMatcher[String]("attributes")
}

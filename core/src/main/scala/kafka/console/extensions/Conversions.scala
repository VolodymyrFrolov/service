package kafka.console.extensions

import scala.reflect.ClassTag

trait Conversions {
  implicit def _toArray[A:ClassTag](value: List[A]) : Array[A] = value.toArray
}

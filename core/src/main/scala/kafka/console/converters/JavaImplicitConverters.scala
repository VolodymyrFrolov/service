package kafka.console.converters

import collection.JavaConverters._
import java.util.{Map => JMap, List => JList}

object JavaImplicitConverters {

  implicit def asScalaMap[K, V](m: JMap[K, V]): Map[K, V] = m.asScala.toMap

  implicit def asScalaList[A](l: JList[A]): List[A] = l.asScala.toList

  implicit def asJavaMap[K, V](m: Map[K, V]): JMap[K, V] = m.asJava

}

package kafka.console.model.monitoring

import javax.management.{MBeanInfo, ObjectName}

case class MBeanMetricInfo(name: ObjectName, domain: String, canonicalName: String, info: MBeanInfo) {
  def attributes = info.getAttributes

  def attributesNames = attributes.map(_.getName)
}

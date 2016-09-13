package kafka.console.service.config.kafka.client

import java.util.Properties

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.config.types.Password
import org.apache.kafka.common.serialization.Serializer
import Compression._
import kafka.console.service.config.kafka.client.Ack._
import scala.concurrent.duration._

object KafkaConfiguration {

  val StringSerializer = "org.apache.kafka.common.serialization.StringSerializer"
  val StringDeserializer = "org.apache.kafka.common.serialization.StringDeserializer"
  val ByteSerializer   = "org.apache.kafka.common.serialization.ByteArraySerializer"

  private[config] val DefaultBufferMemory          = 33554432L
  private[config] val DefaultRetriesNumber         = 0
  private[config] val DefaultBatchSize             = 16384
  private[config] val DefaultIdleConnectionTimeout = 540 seconds
  private[config] val DefaultLinger                = 0 seconds
  private[config] val DefaultBlockingTimeout       = 60 seconds
  private[config] val DefaultMaxRequestSize        = 1048576
  private[config] val DefaultPartitioner           = "org.apache.kafka.clients.producer.internals.DefaultPartitioner"
  private[config] val DefaultRequestTimeout        = 60 seconds
  private[config] val DefaultAckTimeout            = 30 seconds
  private[config] val DefaultShouldBlockOnFullBuf  = false
  private[config] val DefaultInterceptors          = Seq()
  private[config] val DefaultMaxInFlightReqsPerCon = 5
  private[config] val DefaultMetadataReqsTimeout   = 60 seconds
  private[config] val DefaultMetadataMaxAge        = 300 seconds
  private[config] val DefaultMetricReporters       = Seq()
  private[config] val DefaultMetricSampleNumber    = 2
  private[config] val DefaultMetricSampleWindow    = 30 seconds
  private[config] val DefaultReconnectBackoff      = 50 milliseconds
  private[config] val DefaultRetryBackoff          = 100 milliseconds

  private[config] val TcpSendBufferSize            = 131072
  private[config] val TcpReceiveBufferSize         = 65536

  private[config] val SslDefaultProtocol           = "TLS"
  private[config] val SslDefaultStoreType          = "JKS"
  private[config] val SslDefaultKeyManagerAlgo     = "SunX509"
  private[config] val SslDefaultTrustManagerAlgo   = "PKIX"
  private[config] val SslDefaultCipherSuites       = Seq()

  private[config] val TLSv1                        = "TLSv1"
  private[config] val TLSv11                       = "TLSv1.1"
  private[config] val TLSv12                       = "TLSv1.2"


  val SERVERS                 = "bootstrap.servers"
  val KEY_SERIALIZER          = "key.serializer"
  val KEY_DESERIALIZER        = "key.deserializer"
  val VALUE_SERIALIZER        = "value.serializer"
  val VALUE_DESERIALIZER      = "value.deserializer"
  val ACKS                    = "acks"
  val BUFFER_MEMORY           = "buffer.memory"
  val COMPRESSION_TYPE        = "compression.type"
  val RETRIES                 = "retries"
  val SSL_KEY_PASSWORD        = "ssl.key.password"
  val SSL_KEYSTORE_LOCATION   = "ssl.keystore.location"
  val SSL_KEYSTORE_PASSWORD   = "ssl.keystore.password"
  val SSL_TRUSTSTORE_LOCATION = "ssl.truststore.location"
  val SSL_TRUSTSTORE_PASSWORD = "ssl.truststore.password"
  val BATCH_SIZE              = "batch.size"
  val CLIENT_ID               = "client.id"
  val COLLECTION_MAX_IDLE     = "connections.max.idle.ms"
  val LINGER                  = "linger.ms"
  val MAX_BLOCK               = "max.block.ms"
  val MAX_REQUEST_SIZE        = "max.request.size"
  val PARTITIONER             = "partitioner.class"
  val RECEIVE_BUFFER_SIZE     = "receive.buffer.bytes"
  val REQUEST_TIMEOUT         = "request.timeout.ms"
  val SECURITY_PROTOCOL       = "security.protocol"
  val SEND_BUFFER_BYTES       = "send.buffer.bytes"
  val SSL_ENABLED_PROTOCOLS   = "ssl.enabled.protocols"
  val SSL_KEYSTORE_TYPE       = "ssl.keystore.type"
  val SSL_PROTOCOLS           = "ssl.protocol"
  val SSL_PROVIDER            = "ssl.provider"
  val SSL_TRUSTSTORE_TYPE     = "ssl.truststore.type"
  val TIMEOUT                 = "timeout.ms"
  val INTERCEPTOR             = "interceptor.classes"
  val MAX_IN_FLIGHT_REQUEST   = "max.in.flight.requests.per.connection"
  val METADATA_FETCH_TIMEOUT  = "metadata.fetch.timeout.ms"
  val METADATA_MAX_AGE        = "metadata.max.age.ms"
  val METRICS_REPORTER        = "metric.reporters"
  val METRICS_NUM_SAMPLES     = "metrics.num.samples"
  val METRICS_SAMPLE_WINDOW   = "metrics.sample.window.ms"
  val RECONNECT_BACKOFF       = "reconnect.backoff.ms"
  val RETRY_BACKOFF           = "retry.backoff.ms"
  val SSL_CIPHER_SUITES       = "ssl.cipher.suites"
  val SSL_EP_IDENT_ALGO       = "ssl.endpoint.identification.algorithm"
  val SSL_KEYMANAGER_ALGO     = "ssl.keymanager.algorithm"
  val SSL_TRUSTMANAGER_ALGO   = "ssl.keymanager.algorithm"

  private[config] def _integer(value: Int):  AnyRef = value.asInstanceOf[java.lang.Integer]
  private[config] def _long   (value: Long): AnyRef = value.asInstanceOf[java.lang.Long]
}

object SecurityProtocols {
  val PlainText = "PLAINTEXT"
  val SSL       = "SSL"
  val SASL      = "SASL_PLAINTEXT"
  val SASLSSL   = "SASL_SSL"
}

/**
 * Kafka producer configuration
 *
 * @param servers A list of host/port pairs to use for establishing the initial connection to the Kafka cluster.
 *
 * @param ack The number of acknowledgments the producer requires the leader to have received
 *            before considering a request complete.
 *
 * @param bufferMemory The total bytes of memory the producer can use to buffer records
 *                     waiting to be sent to the server.
 *
 * @param compression The compression type for all data generated by the producer. The default is none
 *
 * @param retries Number of attempts to send a record that previously failed with an error
 *
 * @param batchSize size of the batch space. The batch space is used by producer to put records
 *                  together and send them within fewer requests whenever multiple records are being sent to
 *                  the same partition
 *
 * @param clientId An id string to pass to the server when making requests.
 *
 * @param idleConnectionTimeout Close idle connections after the specified timeout
 *
 * @param linger An artificial delay before sending aimed to fill the request buffer
 *
 * @param blockingTimeout identifies how long KafkaProducer.send() and KafkaProducer.partitionsFor() will block.
 *
 * @param maxRequestSize The maximum size of a request in bytes.
 *                       This is also effectively a cap on the maximum record size.
 *
 * @param partitioner identifies partitioner
 *
 * @param requestTimeout identifies the maximum amount of time the client will wait for the response of a request.
 *
 * @param acknowledgeTimeout identifies the maximum amount of time the server will wait for acknowledgments
 *                           from followers to meet the acknowledgment requirements
 *                           the producer has specified with the acks configuration.
 *
 * @param interceptors a list of interceptors that allow you to intercept (and possibly mutate)
 *                     the records received by the producer before they are published
 *                     to the Kafka cluster.
 *
 * @param inflightRequestsMax The maximum number of unacknowledged requests
 *                            the client will send on a single connection before blocking.
 *
 * @param metadataRequestTimeout The first time data is sent to a topic we must fetch metadata about
 *                               that topic to know which servers host the topic's partitions.
 *
 * @param metadataMaxAge The period of time in milliseconds after which we force a refresh of metadata
 *                       even if we haven't seen any partition leadership changes to proactively
 *                       discover any new brokers or partitions.
 *
 * @param metricReporters A list of classes to use as metrics reporters.
 *                        Implementing the MetricReporter interface allows plugging in classes
 *                        that will be notified of new metric creation.
 *
 * @param metricSampleNum The number of samples maintained to compute metrics.
 *
 * @param metricSampleWnd The window of time a metrics sample is computed over.
 *
 * @param reconnectBackoff The amount of time to wait before attempting to reconnect to a given host.
 *
 * @param retryBackoff The amount of time to wait before attempting to retry
 *                     a failed request to a given topic partition.
 *
 * @param ssl ssl-related configuration
 */
case class KafkaConfiguration(servers: List[String],
                         ack: Ack                         = All,
                         bufferMemory: Long               = KafkaConfiguration.DefaultBufferMemory,
                         compression: Compression         = NoComp,
                         retries: Int                     = KafkaConfiguration.DefaultRetriesNumber,
                         batchSize: Int                   = KafkaConfiguration.DefaultBatchSize,
                         clientId: Option[String]         = None,
                         idleConnectionTimeout: Duration  = KafkaConfiguration.DefaultIdleConnectionTimeout,
                         linger: Duration                 = KafkaConfiguration.DefaultLinger,
                         blockingTimeout: Duration        = KafkaConfiguration.DefaultBlockingTimeout,
                         maxRequestSize: Int              = KafkaConfiguration.DefaultMaxRequestSize,
                         partitioner: String              = KafkaConfiguration.DefaultPartitioner,
                         requestTimeout: Duration         = KafkaConfiguration.DefaultRequestTimeout,
                         acknowledgeTimeout: Duration     = KafkaConfiguration.DefaultAckTimeout,
                         interceptors: Seq[String]        = KafkaConfiguration.DefaultInterceptors,
                         inflightRequestsMax: Int         = KafkaConfiguration.DefaultMaxInFlightReqsPerCon,
                         metadataRequestTimeout: Duration = KafkaConfiguration.DefaultMetadataReqsTimeout,
                         metadataMaxAge: Duration         = KafkaConfiguration.DefaultMetadataMaxAge,
                         metricReporters: Seq[String]     = KafkaConfiguration.DefaultMetricReporters,
                         metricSampleNum: Int             = KafkaConfiguration.DefaultMetricSampleNumber,
                         metricSampleWnd: Duration        = KafkaConfiguration.DefaultMetricSampleWindow,
                         reconnectBackoff: Duration       = KafkaConfiguration.DefaultReconnectBackoff,
                         retryBackoff: Duration           = KafkaConfiguration.DefaultRetryBackoff,
                         keySerializer: String            = KafkaConfiguration.StringSerializer,
                         keyDeserializer: String          = KafkaConfiguration.StringDeserializer,
                         valueSerializer: String          = KafkaConfiguration.StringSerializer,
                         valueDeserializer: String        = KafkaConfiguration.StringDeserializer,
                         securityProtocol: String         = SecurityProtocols.PlainText,
                         tcp: TcpConfig                   = TcpConfig(),
                         ssl: SslConfig                   = SslConfig()
                          )

/**
 *
 * @param sendBufferSize the size of the TCP send buffer (SO_SNDBUF) to use when sending data.
 * @param receiveBufferSize the size of the TCP receive buffer (SO_RCVBUF) to use when reading data.
 */
case class TcpConfig(sendBufferSize: Int    = KafkaConfiguration.TcpSendBufferSize,
                     receiveBufferSize: Int = KafkaConfiguration.TcpReceiveBufferSize)

/**
 * SSL-specific configuration
 *
 * @param keyPassword the password of the private key in the key store file. This is optional for client.
 *
 * @param keystoreLocation The location of the key store file.
 *                         This is optional for client and can be used for two-way authentication for client.
 *
 * @param keystorePassword The store password for the key store file.
 *                         This is optional for client and only needed if ssl.keystore.location is configured.
 *
 * @param truststoreLocation The location of the trust store file.
 *
 * @param truststorePassword The password for the trust store file.
 *
 * @param availableProtocols The list of protocols enabled for SSL connections.
 *
 * @param keystoreType The file format of the key store file. This is optional for client.
 *
 * @param protocol The SSL protocol used to generate the SSLContext. Default setting is TLS
 *
 * @param provider The name of the security provider used for SSL connections.
 *
 * @param truststoreType The file format of the trust store file.
 *
 * @param epIdentificationAlgorithm The endpoint identification algorithm to validate
 *                                  server hostname using server certificate.
 *
 * @param keyManagerAlgorithm The algorithm used by key manager factory for SSL connections.
 *                            Default value is the key manager factory algorithm configured
 *                            for JVM.
 *
 * @param trustManagerAlgorithm The algorithm used by trust manager factory for SSL connections.
 *                              Default value is the trust manager factory algorithm configured
 *                              for JVM.
 *
 * @param cipherSuites A list of cipher suites. This is a named combination of authentication,
 *                     encryption, MAC and key exchange algorithm used to negotiate
 *                     the security settings for a network connection using TLS or SSL network protocol.
 *                     By default all the available cipher suites are supported.
 */
case class SslConfig(keyPassword: Option[Password]             = None,
                     keystoreLocation: Option[String]          = None,
                     keystorePassword: Option[Password]        = None,
                     truststoreLocation: Option[String]        = None,
                     truststorePassword: Option[Password]      = None,
                     keystoreType: Option[String]              = Some(KafkaConfiguration.SslDefaultStoreType),
                     availableProtocols: Seq[String]           = Seq (KafkaConfiguration.TLSv1, KafkaConfiguration.TLSv11,  KafkaConfiguration.TLSv12),
                     protocol: String                          = KafkaConfiguration.SslDefaultProtocol,
                     provider: Option[String]                  = None,
                     truststoreType: String                    = KafkaConfiguration.SslDefaultStoreType,
                     epIdentificationAlgorithm: Option[String] = None,
                     keyManagerAlgorithm: String               = KafkaConfiguration.SslDefaultKeyManagerAlgo,
                     trustManagerAlgorithm: String             = KafkaConfiguration.SslDefaultTrustManagerAlgo,
                     cipherSuites: Seq[String]                 = KafkaConfiguration.SslDefaultCipherSuites
                      )

trait ToKafkaConfigurationExtensions {

  implicit def extendKafkaConfiguration(value: KafkaConfiguration): KafkaConfigurationExtensions =
    new KafkaConfigurationExtensions(value)

  implicit def convertToProperties(value: KafkaConfiguration): Properties = value.toProperties

}

final class KafkaConfigurationExtensions(val value: KafkaConfiguration) extends AnyVal {

  import scala.reflect._

  /**
   * Convert to standard java properties collection
   * @return
   */
  def toProperties: Properties = {

    import KafkaConfiguration._

    import scala.collection.JavaConversions._
    //import Ack._
    //import Compression._

    import value._

    val EMPTY = ""

    val content = Map(
      SERVERS                 -> servers.mkString(","),
      KEY_SERIALIZER          -> keySerializer,
      VALUE_SERIALIZER        -> valueSerializer,
      KEY_DESERIALIZER        -> keyDeserializer,
      VALUE_DESERIALIZER      -> valueDeserializer,
      ACKS                    -> ackToString(ack),
      BUFFER_MEMORY           -> _long(bufferMemory),
      COMPRESSION_TYPE        -> compressionToString(compression),
      RETRIES                 -> _integer(retries),
      SSL_KEY_PASSWORD        -> ssl.keyPassword.getOrElse(EMPTY),
      SSL_KEYSTORE_LOCATION   -> ssl.keystoreLocation.getOrElse(EMPTY),
      SSL_KEYSTORE_PASSWORD   -> ssl.keyPassword.getOrElse(EMPTY),
      SSL_TRUSTSTORE_LOCATION -> ssl.truststoreLocation.getOrElse(EMPTY),
      SSL_TRUSTSTORE_PASSWORD -> ssl.truststorePassword.getOrElse(EMPTY),
      BATCH_SIZE              -> _integer(batchSize),
      CLIENT_ID               -> clientId.getOrElse(EMPTY),
      COLLECTION_MAX_IDLE     -> _long(idleConnectionTimeout.toMillis),
      LINGER                  -> _long(linger.toMillis),
      MAX_BLOCK               -> _long(blockingTimeout.toMillis),
      MAX_REQUEST_SIZE        -> _integer(maxRequestSize),
      PARTITIONER             -> partitioner,
      RECEIVE_BUFFER_SIZE     -> _integer(tcp.receiveBufferSize),
      REQUEST_TIMEOUT         -> _integer(requestTimeout.toMillis.toInt),
      SECURITY_PROTOCOL       -> securityProtocol,
      SEND_BUFFER_BYTES       -> _integer(tcp.sendBufferSize),
      SSL_ENABLED_PROTOCOLS   -> ssl.availableProtocols.mkString(","),
      SSL_KEYSTORE_TYPE       -> ssl.keystoreType.getOrElse(EMPTY),
      SSL_PROTOCOLS           -> ssl.protocol,
      SSL_PROVIDER            -> ssl.provider.getOrElse(EMPTY),
      SSL_TRUSTSTORE_TYPE     -> ssl.truststoreType,
      SSL_CIPHER_SUITES       -> ssl.cipherSuites.mkString(","),
      SSL_EP_IDENT_ALGO       -> ssl.epIdentificationAlgorithm.getOrElse(EMPTY),
      SSL_KEYMANAGER_ALGO     -> ssl.keyManagerAlgorithm,
      SSL_TRUSTMANAGER_ALGO   -> ssl.trustManagerAlgorithm,
      TIMEOUT                 -> _integer(acknowledgeTimeout.toMillis.toInt),
      INTERCEPTOR             -> interceptors.mkString(","),
      MAX_IN_FLIGHT_REQUEST   -> _integer(inflightRequestsMax),
      METADATA_FETCH_TIMEOUT  -> _long(metadataRequestTimeout.toMillis),
      METADATA_MAX_AGE        -> _long(metadataMaxAge.toMillis),
      METRICS_REPORTER        -> metricReporters.mkString(","),
      METRICS_NUM_SAMPLES     -> _integer(metricSampleNum),
      METRICS_SAMPLE_WINDOW   -> _long(metricSampleWnd.toMillis),
      RECONNECT_BACKOFF       -> _long(reconnectBackoff.toMillis),
      RETRY_BACKOFF           -> _long(retryBackoff.toMillis)
    )

    val properties = new Properties()

    properties.putAll(content)

    properties
  }

}

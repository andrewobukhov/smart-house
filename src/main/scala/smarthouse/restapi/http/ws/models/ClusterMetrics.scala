package smarthouse.restapi.http.ws.models

import akka.cluster.metrics.NodeMetrics

case class ClusterMetrics(metrics: Set[NodeMetrics], messageType: String) extends ApiMessage

object ClusterMetrics {
  def apply(metrics: Set[NodeMetrics]): ClusterMetrics = new ClusterMetrics(metrics, "ClusterMetrics")
}

package smarthouse.restapi.http.ws.models

case class ClusterMetricsSubscription(action: String, messageType: String) extends ApiMessage

object ClusterMetricsSubscription {
  def apply(action: String): ClusterMetricsSubscription = new ClusterMetricsSubscription(action, "ClusterMetricsSubscription")
}

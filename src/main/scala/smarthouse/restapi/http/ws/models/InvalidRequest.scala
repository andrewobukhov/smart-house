package smarthouse.restapi.http.ws.models

case class InvalidRequest(originalRequest: String, messageType: String) extends ApiMessage

object InvalidRequest {
  def apply(originalRequest: String): InvalidRequest = new InvalidRequest(originalRequest, "InvalidRequest")
}

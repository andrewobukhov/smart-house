package smarthouse.restapi.http.ws.models

case class UnsupportedRequest(request: ApiMessage, messageType: String) extends ApiMessage

object UnsupportedRequest {
  def apply(request: ApiMessage): UnsupportedRequest = new UnsupportedRequest(request, "UnsupportedRequest")
}

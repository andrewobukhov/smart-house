package smarthouse.restapi.http.ws.models

case class Accepted(message: ApiMessage, messageType: String) extends ApiMessage

object Accepted {
  def apply(message: ApiMessage): Accepted = new Accepted(message, "Accepted")
}

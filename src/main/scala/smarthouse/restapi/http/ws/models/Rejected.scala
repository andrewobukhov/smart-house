package smarthouse.restapi.http.ws.models

case class Rejected(message: ApiMessage, reason: String, messageType: String) extends ApiMessage

object Rejected {
  def apply(message: ApiMessage, reason: RejectReason): Rejected = new Rejected(message, reason.name, "Rejected")
}

sealed trait RejectReason {
  def name: String
}

object DuplicateRequest extends RejectReason {
  val name = "DuplicateRequest"
}

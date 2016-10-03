package smarthouse.restapi.http.ws.models

case class UserNotification(`type`: String, text: String, operation: Option[String], entityName: Option[String], entityId: Option[String], messageType: String) extends ApiMessage

object UserNotification{
  def apply(`type`: String, text: String, operation: Option[String], entityName: Option[String],  entityId: Option[String]): UserNotification
  = new UserNotification(`type`, text, operation, entityName, entityId, "UserNotification")

  def apply(`type`: String, text: String): UserNotification
  = new UserNotification(`type`, text, None, None, None, "UserNotification")
}
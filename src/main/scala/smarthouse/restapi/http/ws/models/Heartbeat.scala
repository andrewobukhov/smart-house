package smarthouse.restapi.http.ws.models

import smarthouse.restapi.http.ws.services.JiraServiceMessage

case class Heartbeat(messageType: String) extends ApiMessage with JiraServiceMessage

object Heartbeat {
  def apply(): Heartbeat = new Heartbeat("Heartbeat")
}
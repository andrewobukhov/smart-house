package smarthouse.restapi.http.ws.services

import akka.actor.{Actor, ActorRef, Props}
import smarthouse.restapi.http.ws.models.{Heartbeat, UserNotification}

class JiraService(jiraRouterActor: ActorRef, val publisherActor: ActorRef) extends Actor with PublisherReply {

  val OperationInProgress = "operationInProgress"

  override def receive: Receive = {
    case msg: Heartbeat =>
      reply(UserNotification("Heartbeat", "1.0"))
  }
}

object JiraService {
  def props(jiraRouterActor: ActorRef, publisherActor: ActorRef): Props =
    Props(classOf[JiraService], jiraRouterActor, publisherActor)

  case object InvalidToken

}

trait JiraServiceMessage
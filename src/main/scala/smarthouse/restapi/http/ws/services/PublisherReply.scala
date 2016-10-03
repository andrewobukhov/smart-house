package smarthouse.restapi.http.ws.services

import akka.actor.ActorRef
import smarthouse.restapi.http.ws.models.ApiMessage
import smarthouse.restapi.http.ws.ApiDecoder._

trait PublisherReply {
  def publisherActor: ActorRef

  def reply(message: ApiMessage): Unit = {
    publisherActor ! encodeRequest(message)
  }

  def replyCustom(message: AnyRef): Unit = {
    publisherActor ! encodeCustom(message)
  }
}

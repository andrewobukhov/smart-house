package smarthouse.restapi.http.ws.services

import akka.actor.{Actor, ActorRef, Props}
import smarthouse.restapi.http.ws.services.DataFabricService.UpdateFabric

class DataFabricService(dataFabricRouter: ActorRef, val publisherActor: ActorRef) extends Actor with PublisherReply {
  dataFabricRouter ! UpdateFabric(self)

  val format = new java.text.SimpleDateFormat("yyyy-MM-dd")

  override def receive: Receive = {

    case msg: String =>
      //dataFabricRouter ! com.ringcentral.sunset.data.GetReleases
  }
}

object DataFabricService {
  def props(dataFabricRouter: ActorRef, publisherActor: ActorRef): Props = Props(classOf[DataFabricService], dataFabricRouter, publisherActor)

  case class UpdateFabric(ref: ActorRef)

}

trait DataFabricMessage

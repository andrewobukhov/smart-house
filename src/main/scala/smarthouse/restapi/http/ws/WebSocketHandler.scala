package smarthouse.restapi.http.ws

import akka.actor._
import smarthouse.restapi.http.ws.ApiDecoder._
import smarthouse.restapi.http.ws.WebSocketHandler._
import smarthouse.restapi.http.ws.models._
import smarthouse.restapi.http.ws.services._

import scala.util.{Failure, Success, Try}

class WebSocketHandler(dataRouterActor: ActorRef, jiraRouterActor: ActorRef)
  extends Actor with ActorLogging with PublisherReply {
  implicit var publisherActor: ActorRef = _

  override def receive: Receive = {
    case PublisherCreated =>
      publisherActor = sender()

      // Create long living service actors
      context.actorOf(DataFabricService.props(dataRouterActor, publisherActor), "dataFabricService")
      context.actorOf(JiraService.props(jiraRouterActor, publisherActor), "jiraService")

    case StreamComplete =>
      context stop self

    case msg: String =>
      decodeRequest(msg) match {
        case Some(message) =>
          handleRequest(message)
        case None =>
          reply(InvalidRequest(msg))
      }
  }

  private def handleRequest(implicit message: ApiMessage) = {
    message match {
      case ClusterMetricsSubscription("subscribe", name) =>
        createService(ClusterMetricsService.props, name)
      case ClusterMetricsSubscription("unsubscribe", name) =>
        removeService(name)
      case request: DataFabricMessage =>
        reply(Accepted(message))
        context.actorSelection("dataFabricService").tell(request, self)
      case request: JiraServiceMessage =>
        reply(Accepted(message))
        context.actorSelection("jiraService").tell(request, self)
      case api => reply(UnsupportedRequest(api))
    }
  }

  private def createService(props: Props, name: String)(implicit message: ApiMessage) = {
    Try(context.actorOf(props, name)) match {
      case Success(_) => reply(Accepted(message))
      case Failure(_) => reply(Rejected(message, DuplicateRequest))
    }
  }

  private def removeService(name: String)(implicit message: ApiMessage) = {
    context.actorSelection(name).tell(PoisonPill, self)
    reply(Accepted(message))
  }
}

object WebSocketHandler {

  case object StreamComplete

  case object PublisherCreated

  def props(dataRouterActor: ActorRef, jiraRouterActor: ActorRef): Props = Props(classOf[WebSocketHandler], dataRouterActor, jiraRouterActor)
}

package smarthouse.restapi.http

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, FlowShape}
import smarthouse.restapi.http.ws.{WebSocketActorPublisher, WebSocketHandler}

/**
 * Web Socket Route, used to provide ws access via bi-direction API.
 *
 * Web socket access at following up: ws://[hostname]:[port]/ws
 */
trait WebSocketRoutes {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val wsHandlerProps: Props

  val PublisherBufferSize = 1000

  private val wsPath = path("ws")

  val webSocketRoute = wsPath {
    get {
      handleWebSocketMessages(wsService)
    }
  }

  /**
   * Service handling Web Socket flow, creates actors for message flow
   * @return complete flow to materialize
   */
  private def wsService: Flow[Message, Message, akka.NotUsed] = {
    Flow.fromGraph {
      GraphDSL.create() { implicit b =>
        import GraphDSL.Implicits._

        val collect = b.add(Flow[Message].collect[String]({
          case TextMessage.Strict(txt) => txt
        }))
        val handler = system.actorOf(wsHandlerProps)
        val source = Source.actorPublisher[String](WebSocketActorPublisher.props(handler, PublisherBufferSize))
        val sink = Sink.actorRef(handler, WebSocketHandler.StreamComplete)

        val mapToMessage = b.add(Flow[String].map[TextMessage](TextMessage.Strict))
        val actorSource = b.add(source)
        val actorSink = b.add(sink)

        collect ~> actorSink
        actorSource ~> mapToMessage

        FlowShape(collect.in, mapToMessage.out)
      }
    }
  }
}

package smarthouse.restapi.http.ws

import akka.actor.{ActorLogging, ActorRef, Props, Terminated}
import akka.stream.actor.ActorPublisher

import scala.annotation.tailrec

/**
 * Actor Publisher for WebSocket communication, controls messages flow via backpressure and buffers
 *
 * @param handler handler which will handle messages on open web socket
 */
class WebSocketActorPublisher(handler: ActorRef, bufferSize: Int) extends ActorPublisher[String] with ActorLogging {

  case object Updated

  import akka.stream.actor.ActorPublisherMessage._

  import scala.collection.mutable

  val queue = mutable.Queue[String]()

  var updated = false

  override def preStart() {
    handler ! WebSocketHandler.PublisherCreated
    context.watch(handler)
  }

  def receive: Receive = {
    case stats: String =>
      if (queue.size == bufferSize) queue.dequeue()
      queue += stats
      if (!updated) {
        updated = true
        self ! Updated
      }
    case Updated =>
      deliver()
    case Request(amount) =>
      deliver()
    case Cancel =>
      context.stop(self)
    case Terminated(`handler`) =>
      context.stop(self)
  }

  @tailrec final def deliver(): Unit = {
    if (queue.isEmpty && totalDemand != 0) {
      updated = false
    } else if (totalDemand > 0 && queue.nonEmpty) {
      onNext(queue.dequeue())
      deliver()
    }
  }
}

object WebSocketActorPublisher {
  def props(handler: ActorRef, bufferSize: Int): Props = Props(classOf[WebSocketActorPublisher], handler, bufferSize)
}


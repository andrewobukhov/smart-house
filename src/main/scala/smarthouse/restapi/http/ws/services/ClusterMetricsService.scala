package smarthouse.restapi.http.ws.services

import akka.actor.{Actor, ActorRef, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.cluster.metrics.{ClusterMetricsChanged, ClusterMetricsExtension}
import smarthouse.restapi.http.ws.models.{ClusterMetrics, ClusterState}

class ClusterMetricsService(val publisherActor: ActorRef) extends Actor with PublisherReply {
  val cluster = Cluster(context.system)
  val extension = ClusterMetricsExtension(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsSnapshot,
      classOf[MemberEvent], classOf[UnreachableMember], classOf[LeaderChanged])
    extension.subscribe(self)
  }

  override def postStop(): Unit = {
    extension.unsubscribe(self)
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {
    case ClusterMetricsChanged(clusterMetrics) =>
      reply(ClusterMetrics(clusterMetrics))
    case state: CurrentClusterState =>
      reply(ClusterState(state.members, state.leader))
    case event: MemberUp =>
      reply(ClusterState(Set(event.member)))
    case event: MemberRemoved =>
      reply(ClusterState(Set(event.member)))
    case event: MemberExited =>
      reply(ClusterState(Set(event.member)))
    case event: UnreachableMember =>
      reply(ClusterState(Set(event.member)))
    case event: LeaderChanged =>
      reply(ClusterState(Set(), event.leader))
  }
}

object ClusterMetricsService {
  def props(implicit publisherActor: ActorRef): Props = Props(classOf[ClusterMetricsService], publisherActor)
}

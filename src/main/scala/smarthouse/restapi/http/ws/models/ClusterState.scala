package smarthouse.restapi.http.ws.models

import akka.actor.Address
import akka.cluster.Member

case class ClusterState(members: Set[Member], leader: Option[Address], messageType: String) extends ApiMessage

object ClusterState {
  def apply(members: Set[Member], leader: Option[Address] = None): ClusterState = new ClusterState(members, leader, "ClusterState")
}


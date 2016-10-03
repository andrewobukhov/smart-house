package smarthouse.restapi.http.ws

import java.net.URI
import java.time.OffsetDateTime

import akka.cluster.MemberStatus
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

trait JsonSerializers {
  val memberStatusSerializer = new CustomSerializer[MemberStatus](format => ( {
    case JString("Exiting") => MemberStatus.Exiting
    case JString("Up") => MemberStatus.Up
    case JString("Joining") => MemberStatus.Joining
    case JString("Leaving") => MemberStatus.Leaving
    case JString("Removed") => MemberStatus.Removed
    case JString("Down") => MemberStatus.Down
  }, {
    case x: MemberStatus =>
      JString(x.toString)
  }
    ))

  val uriSerializer = new CustomSerializer[URI](format => ( {
    case JString(string) => new URI(string)
  }, {
    case x: URI =>
      JString(x.toString)
  }
    ))

  val offsetDateTime = new CustomSerializer[OffsetDateTime](format => ( {
    case JString(string) => OffsetDateTime.parse(string)
  }, {
    case x: OffsetDateTime =>
      JString(x.toLocalDateTime.toString)
  }
    ))
}

package smarthouse.restapi.http.ws

import org.json4s.JsonAST.JString
import org.json4s.NoTypeHints
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization._
import smarthouse.restapi.http.ws.models.{ApiMessage, Heartbeat}

import scala.util.{Failure, Success, Try}

object ApiDecoder extends JsonSerializers {
  implicit val formats = Serialization.formats(NoTypeHints) + memberStatusSerializer + uriSerializer + offsetDateTime

  def decodeRequest(requestString: String): Option[ApiMessage] = {
    Try {
      parse(requestString) \ "messageType" match {
        case JString("Heartbeat") => read[Heartbeat](requestString)
        case _ => throw InvalidRequestException
      }
    } match {
      case Success(x) => Some(x)
      case Failure(_) => None
    }
  }


  def encodeRequest(apiMessage: ApiMessage): String = {
    write(apiMessage)
  }

  def encodeCustom(msg: AnyRef): String = {
    write(msg)
  }

  case object InvalidRequestException extends RuntimeException

}

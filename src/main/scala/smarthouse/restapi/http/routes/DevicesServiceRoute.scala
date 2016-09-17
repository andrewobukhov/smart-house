package smarthouse.restapi.http.routes

import java.util.Date

import akka.http.scaladsl.model.{StatusCodes}
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.CirceSupport
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import io.circe.syntax._
import smarthouse.restapi.http.SecurityDirectives
import smarthouse.restapi.models.DeviceEntity
import smarthouse.restapi.services.{AuthService, DevicesService}

import scala.concurrent.ExecutionContext

class DevicesServiceRoute(val authService: AuthService,
                          val eventService: DevicesService)
                         (implicit executionContext: ExecutionContext) extends CirceSupport with SecurityDirectives {

  import StatusCodes._
  import eventService._

  implicit val dateTimeEncoder: Encoder[Date] = Encoder.instance(a => a.getTime.asJson)
  implicit val dateTimeDecoder: Decoder[Date] = Decoder.instance(a => a.as[Long].map(new Date(_)))

  val route = pathPrefix("devices") {

    pathEndOrSingleSlash {
      get {
        complete(getDevices().map(_.asJson))
      } ~ post {
        entity(as[DeviceEntity]) { item =>
          complete(Created -> createDevice(item).map(_.asJson))
        }
      }
    }
  }
}

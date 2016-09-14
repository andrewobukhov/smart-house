package smarthouse.restapi.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.CirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import smarthouse.restapi.http.SecurityDirectives
import smarthouse.restapi.models.EventEntity
import smarthouse.restapi.services.{AuthService, EventsService}

import scala.concurrent.ExecutionContext

class EventsServiceRoute(val authService: AuthService,
                         val eventService: EventsService)
                        (implicit executionContext: ExecutionContext) extends CirceSupport with SecurityDirectives {

  import StatusCodes._
  import authService._
  import eventService._

  val route = pathPrefix("events") {
    path("all") {
      pathEndOrSingleSlash {
        get {
          complete(getEvents().map(_.asJson))
        }
      }
    } ~
      path("add") {
        pathEndOrSingleSlash {
          post {
            entity(as[EventEntity]) { entity =>
              complete(Created -> createEvent(entity).map(_.asJson))
            }
          }
        }
      }
  }
}

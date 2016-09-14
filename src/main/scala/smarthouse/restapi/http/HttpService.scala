package smarthouse.restapi.http

import akka.http.scaladsl.server.Directives._
import smarthouse.restapi.http.routes.{AuthServiceRoute, EventsServiceRoute, UsersServiceRoute}
import smarthouse.restapi.services.{AuthService, EventsService, UsersService}
import smarthouse.restapi.utils.CorsSupport

import scala.concurrent.ExecutionContext

class HttpService(usersService: UsersService, authService: AuthService, eventsService: EventsService)
                 (implicit executionContext: ExecutionContext) extends CorsSupport {

  val usersRouter = new UsersServiceRoute(authService, usersService)
  val authRouter = new AuthServiceRoute(authService)
  val eventRouter = new EventsServiceRoute(authService, eventsService)

  val routes = corsHandler {
    usersRouter.route ~ authRouter.route ~ eventRouter.route
  }
}

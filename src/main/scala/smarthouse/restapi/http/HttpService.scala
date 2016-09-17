package smarthouse.restapi.http

import akka.http.scaladsl.server.Directives._
import smarthouse.restapi.http.routes.{AuthServiceRoute, DevicesServiceRoute, EventsServiceRoute, UsersServiceRoute}
import smarthouse.restapi.services.{AuthService, DevicesService, EventsService, UsersService}
import smarthouse.restapi.utils.CorsSupport

import scala.concurrent.ExecutionContext

class HttpService(usersService: UsersService, authService: AuthService,
                  eventsService: EventsService, devicesService: DevicesService)
                 (implicit executionContext: ExecutionContext) extends CorsSupport {

  val usersRouter = new UsersServiceRoute(authService, usersService)
  val authRouter = new AuthServiceRoute(authService)
  val eventRouter = new EventsServiceRoute(authService, eventsService)
  val deviceRouter = new DevicesServiceRoute(authService, devicesService)

  val routes = corsHandler {
    usersRouter.route ~ authRouter.route ~
      eventRouter.route ~ deviceRouter.route
  }
}

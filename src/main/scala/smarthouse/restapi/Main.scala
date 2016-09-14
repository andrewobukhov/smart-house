package smarthouse.restapi

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import smarthouse.restapi.http.HttpService
import smarthouse.restapi.services.{AuthService, EventsService, UsersService}
import smarthouse.restapi.utils.{Config, DatabaseService, FlywayService}

import scala.concurrent.ExecutionContext

object Main extends App with Config {
  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val flywayService = new FlywayService(jdbcUrl, dbUser, dbPassword)
  flywayService.dropDatabase
  flywayService.migrateDatabaseSchema

  val databaseService = new DatabaseService(jdbcUrl, dbUser, dbPassword)

  val usersService = new UsersService(databaseService)
  val authService = new AuthService(databaseService)(usersService)
  val eventsService = new EventsService(databaseService)

  val httpService = new HttpService(usersService, authService, eventsService)

  Http().bindAndHandle(httpService.routes, httpHost, httpPort)
}

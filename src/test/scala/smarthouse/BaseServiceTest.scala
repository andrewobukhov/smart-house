package smarthouse

import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.CirceSupport
import smarthouse.restapi.http.HttpService
import smarthouse.restapi.models.UserEntity
import smarthouse.restapi.services.{AuthService, EventsService, UsersService}
import smarthouse.restapi.utils.DatabaseService
import smarthouse.utils.InMemoryPostgresStorage._
import org.scalatest._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest with CirceSupport {

  dbProcess.getProcessId

  private val databaseService = new DatabaseService(jdbcUrl, dbUser, dbPassword)

  val usersService = new UsersService(databaseService)
  val authService = new AuthService(databaseService)(usersService)
  val eventsService = new EventsService(databaseService)

  val httpService = new HttpService(usersService, authService, eventsService)

  def provisionUsersList(size: Int): Seq[UserEntity] = {
    val savedUsers = (1 to size).map { _ =>
      UserEntity(Some(Random.nextLong()), Random.nextString(10), Random.nextString(10))
    }.map(usersService.createUser)

    Await.result(Future.sequence(savedUsers), 10.seconds)
  }

  def provisionTokensForUsers(usersList: Seq[UserEntity]) = {
    val savedTokens = usersList.map(authService.createToken)
    Await.result(Future.sequence(savedTokens), 10.seconds)
  }

}

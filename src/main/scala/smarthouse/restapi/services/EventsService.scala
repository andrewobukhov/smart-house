package smarthouse.restapi.services

import smarthouse.restapi.models.EventEntity
import smarthouse.restapi.models.db.EventEntityTable
import smarthouse.restapi.utils.DatabaseService

import scala.concurrent.{ExecutionContext, Future}

class EventsService(val databaseService: DatabaseService)
                   (implicit executionContext: ExecutionContext) extends EventEntityTable {

  import databaseService._
  import databaseService.driver.api._

  def getEvents(): Future[Seq[EventEntity]] = db.run(events.result)

  def createEvent(event: EventEntity): Future[EventEntity] = db.run(events returning events += event)
}
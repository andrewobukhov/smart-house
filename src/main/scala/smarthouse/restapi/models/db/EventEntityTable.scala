package smarthouse.restapi.models.db

import smarthouse.restapi.models.EventEntity
import smarthouse.restapi.utils.DatabaseService

trait EventEntityTable {

  protected val databaseService: DatabaseService
  import databaseService.driver.api._

  class Events(tag: Tag) extends Table[EventEntity](tag, "events") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def device = column[String]("device")
    def value = column[String]("value")

    def * = (id, device, value) <> ((EventEntity.apply _).tupled, EventEntity.unapply)
  }

  protected val events = TableQuery[Events]

}

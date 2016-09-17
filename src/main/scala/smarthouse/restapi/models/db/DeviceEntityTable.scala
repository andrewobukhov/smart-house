package smarthouse.restapi.models.db

import java.util.Date

import smarthouse.restapi.models.DeviceEntity
import smarthouse.restapi.utils.DatabaseService

trait DeviceEntityTable {

  protected val databaseService: DatabaseService

  import databaseService.driver.api._

  implicit val JavaUtilDateMapper =
    MappedColumnType.base[java.util.Date, java.sql.Timestamp](
      d => new java.sql.Timestamp(d.getTime),
      d => new java.util.Date(d.getTime))

  class Devices(tag: Tag) extends Table[DeviceEntity](tag, "devices") {

    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def identifier = column[String]("identifier")

    def created = column[Date]("created")

    def * = (id, name, identifier, created) <> ((DeviceEntity.apply _).tupled, DeviceEntity.unapply)
  }

  protected val devices = TableQuery[Devices]

}



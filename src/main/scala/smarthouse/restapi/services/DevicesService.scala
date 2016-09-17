package smarthouse.restapi.services

import java.util.Date

import smarthouse.restapi.models.DeviceEntity
import smarthouse.restapi.models.db.DeviceEntityTable
import smarthouse.restapi.utils.DatabaseService

import scala.concurrent.{ExecutionContext, Future}

class DevicesService(val databaseService: DatabaseService)
                    (implicit executionContext: ExecutionContext) extends DeviceEntityTable {

  import databaseService._
  import databaseService.driver.api._

  def getDevices(): Future[Seq[DeviceEntity]] = db.run(devices.result)

  def createDevice(entity: DeviceEntity): Future[DeviceEntity] = {
    db.run(devices returning devices += entity.copy(created = new Date()))
  }
}
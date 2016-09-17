package smarthouse.restapi.models

import java.util.Date

case class DeviceEntity(id: Option[Long] = None, name: String, identifier: String, created: Date) {
  require(!name.isEmpty, "name.empty")
  require(!identifier.isEmpty, "identifier.empty")
}
package smarthouse.restapi.models

case class EventEntity(id: Option[Long] = None, device: String, value: String) {
  require(!device.isEmpty, "device.empty")
  require(!value.isEmpty, "value.empty")
}
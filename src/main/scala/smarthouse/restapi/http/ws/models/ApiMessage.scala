package smarthouse.restapi.http.ws.models

trait ApiMessage {
  def messageType: String
}

trait ApiMessageSecure extends ApiMessage
{
  def token: String
}

trait CallbackApiMessage extends ApiMessage
{
  def requestId: String
}
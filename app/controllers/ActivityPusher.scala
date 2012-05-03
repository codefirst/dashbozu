package controllers
import lib.pusher.Pusher
import play.api._
import play.api.Play.current
import dispatch.json._
import models.{ActivityDB, Activity}

object ActivityPusher {
  if(Play.configuration.getBoolean("pusher.enable") getOrElse { false } ) {
    for {
      id <- Play.configuration.getString("pusher.id")
      key <- Play.configuration.getString("pusher.key")
      secret <- Play.configuration.getString("pusher.secret")
    } {
      Pusher.init(id, key, secret)
    }
  }

  def publish(activity : Activity) = {
    if(Play.configuration.getBoolean("pusher.enable") getOrElse { false } ) {
      val html =
        views.html.activity(activity)

      val json =
        JsObject(Map(
          JsString("html") -> JsString(html)
        ))
      println(Pusher.triggerPush("activity", "new", json.toString))
    }
  }
}

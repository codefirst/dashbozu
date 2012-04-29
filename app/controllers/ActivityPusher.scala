package controllers
import lib.pusher.Pusher
import play.api._
import play.api.mvc._
import play.api.Play.current
import models.Activity
import java.net.URI
import dispatch.json._


object ActivityPusher {
  for {
    id <- Play.configuration.getString("pusher.id")
    key <- Play.configuration.getString("pusher.key")
    secret <- Play.configuration.getString("pusher.secret")
  } Pusher.init(id, key, secret)

  def publish(activity : Activity) = {
    val html =
      views.html.activity(activity)

    val json =
      JsObject(Map(
        JsString("html") -> JsString(html)
      ))
    println(Pusher.triggerPush("activity", "new", json.toString))
  }
}

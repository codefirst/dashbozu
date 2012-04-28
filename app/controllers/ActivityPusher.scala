package controllers
import lib.pusher.Pusher
import play.api._
import play.api.mvc._
import play.api.Play.current

class ActivityPusher {
  for {
    id <- Play.configuration.getString("pusher.id")
    key <- Play.configuration.getString("pusher.key")
    secret <- Play.configuration.getString("pusher.secret")
  } Pusher.init(id, key, secret)

  println(Pusher.triggerPush("activity", "new", "42"))
}

package controllers
import play.api._
import play.api.mvc._
import play.api.Play.current
import java.net.URI
import dispatch._
import models.{ActivityDB, Activity}

object Boxcar {
  def publish(activity : Activity) = {
    for {
      key <- Play.configuration.getString("boxcar.key")
      secret <- Play.configuration.getString("boxcar.secret")
    } {
      val url =
        :/("boxcar.io") / "devices" / "providers" / key / "notifications" / "broadcast"
      val params = Map(
        "secret" -> "JzJcqzNFqt81JTK5MXYpDiVYBRNXjoKTKkX1Hk3k",
        "notification[from_screen_name]" -> activity.project,
        "notification[message]" -> activity.title,
        "notification[source_url]" -> activity.url.map{ _.toString }.getOrElse{ "" } )
      Http( url << params >|)
    }
  }
}

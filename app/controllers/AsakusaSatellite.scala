package controllers
import play.api._
import play.api.mvc._
import play.api.Play.current
import java.net.URI
import dispatch._
import models.{ActivityDB, Activity}

object AsakusaSatellite {
  val config =
    Play.configuration.getConfig("asakusa_satellite") getOrElse {
      Configuration.empty
    }

  def isEnable =
    config.getBoolean("enable") getOrElse {
      false
    }

  def publish(activity : Activity) = {
    for {
      url     <- config.getString("url")
      api_key <- config.getString("api_key")
      room_id <- config.getString("room_id")
    } {
      val entryPoint =
        dispatch.url(url) / "api" / "v1" /"message"
      val params = Map(
        "message" -> "Project: %s\n%s\n\n%s".format(
          activity.project,
          activity.title,
          activity.url.map{ _.toString }.getOrElse{ "" }
        ),
        "room_id" -> room_id,
        "api_key" -> api_key
      )
      println(entryPoint)
      Http( entryPoint << params >|)
    }
  }
}

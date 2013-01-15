package controllers

import play.api._
import play.api.mvc._
import models._
import bz._
import java.net.URI

object Application extends Controller {

  def index = Action {
    val activities =
      ActivityDB.findAll()
    Ok(views.html.index(activities.toList))
  }

  def about = Action {
    Ok(views.html.about())
  }

  def hook(name : String, params : Map[String, Seq[String]]) =
    Bozu(name) match {
      case Some(bz) =>
        ActivityDB.addAll(bz.get(params))
      case None =>
        throw new RuntimeException("no such bozu: " + name)
    }

  def hookPost(name : String) = Action { request =>
    request.body.asFormUrlEncoded match {
      case Some(body) =>
        hook(name, body)
        Ok("ok")
      case x =>
        BadRequest("invalid request: " + request)
    }
  }

  def hookGet(name : String) = Action { request =>
    hook(name, request.queryString)
    Ok("ok")
  }

  def show(id : String) = Action {
    ActivityDB.find(id) match {
      case None =>
        NotFound("no such activity: " + id)
      case Some(activity) =>
        Ok(views.html.show(activity))
    }
  }

  def embed(id : String) = Action { implicit request =>
    ActivityDB.find(id) match {
      case None =>
        NotFound("no such activity: " + id)
      case Some(activity) =>
        val color = activity.status.toString.toLowerCase match {
          case "success" => "#dff0d8"
          case "error"  =>  "#f2dede"
          case "warn" => "#fcf8e3"
          case _ => "#ffffff"
        }
        Ok(views.html.embed(request.host, activity, color))
    }
  }

  ActivityDB.subscribe { x =>
    ActivityPusher.publish(x)
    Boxcar.publish(x)
    WebSocketNotifier.publish(x)

    if(AsakusaSatellite.isEnable)
      AsakusaSatellite.publish(x)
  }
}

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

  ActivityDB.subscribe { x =>
    ActivityPusher.publish(x)
    Boxcar.publish(x)
  }
}

package controllers

import play.api._
import play.api.mvc._
import models._
import bz._
import java.net.URI

object Application extends Controller {

  def index = Action {
    val activities =
      ActivityDB.findAll
    Ok(views.html.index(activities.toList))
  }

  ActivityDB.subscribe { x =>
    ActivityPusher.publish(x)
  }

  def hookPost(name : String) = Action { request =>
    Bozu(name) map { bz =>
      request.body match {
        case AnyContentAsFormUrlEncoded(body) => Bozu(name) map { bz =>
          ActivityDB.addAll(bz.get(body))
        }
        case _ => {}
      }
      Ok("ok")
    } getOrElse {
      BadRequest("no such hook: " + name)
    }
  }

  def hookGet(name : String) = Action { request =>
    Bozu(name) map { bz =>
      println(bz)
      println(bz.get(request.queryString))
      ActivityDB.addAll(bz.get(request.queryString))
      Ok("ok")
    } getOrElse {
      BadRequest("no such hook: " + name)
    }
  }
}

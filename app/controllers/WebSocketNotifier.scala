package controllers
import java.net.URI
import dispatch.json._
import akka.event._
import akka.actor._
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.concurrent._
import play.api.libs.iteratee._
import dispatch._

import models.{ActivityDB, Activity}

object WebSocketNotifier extends Controller {
  case class Notify(activity : Activity)

  def system =
    Akka.system(Play.current)

  def publish(activity : Activity) = {
    if( Play.configuration.getBoolean("websocket.enable").getOrElse(false) )
      system.eventStream.publish(Notify(activity))
  }

  def ws = WebSocket.using[String] { request =>
    println("on ws")

    var listener : ActorRef = null

    val in = Iteratee.consume[String]()

    val out = Enumerator.pushee[String](
      onStart = (pushee => {
        listener = system.actorOf(Props(new Actor {
          def receive = {
            case event: Notify =>
              println(event.activity)
              val html =
                views.html.activity(event.activity)

              val json =
                JsObject(Map(
                  JsString("html") -> JsString(html)
                ))
              pushee.push(json.toString)
          }
        }))
        system.eventStream.subscribe(listener, classOf[Notify])
      }),
      onComplete = {
        if(listener != null) {
          println("close")
          system.eventStream.unsubscribe(listener)
        }
      },
      onError = { (_,_) =>
        if(listener != null) {
          println("close(error)")
          system.eventStream.unsubscribe(listener)
        }
      }
    )

    (in, out)
  }
}

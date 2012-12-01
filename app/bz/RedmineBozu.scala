package bz

import scala.xml._
import dispatch._
import models._
import java.net.URI
import scala.util.parsing.json.JSON

class RedmineBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : Seq[Activity] = {
    params.get("json") match {
      case None =>
        Seq()
      case Some(jsons) =>
        val json:Option[Any] = JSON.parseFull(jsons.head)
        val map:Map[String,Any] = json.get.asInstanceOf[Map[String, Any]]
        val id:String = map.get("id").get.asInstanceOf[String]
        val subject:String = map.get("subject").get.asInstanceOf[String]
        val description:String = map.get("description").get.asInstanceOf[String]
        val project:String = map.get("project").get.asInstanceOf[String]
        val url:String = map.get("url").get.asInstanceOf[String]
        val iconUrl:String = map.get("iconUrl").get.asInstanceOf[String]
        val author:String = map.get("author").get.asInstanceOf[String]
        Seq(Activity(
          id        = "redmine-" + id,
          title     = subject,
          body      = description,
          createdAt = new java.util.Date(),
          source    = "redmine",
          project   = project,
          url       = Some(new URI(url)),
          iconUrl   = Some(new URI(iconUrl)),
          status    = Info,
          author    = Some(author)
        ))
    }
  }
}

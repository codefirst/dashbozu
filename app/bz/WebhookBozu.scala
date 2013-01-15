package bz
import java.net.URI
import models._

class WebhookBozu extends Bozu {
  def head(map : Map[String, Seq[String]], key : String) : Option[String] =
    for {
      values <- map.get(key)
      value  <- values.headOption
    } yield value


  def headOr(map : Map[String, Seq[String]], key : String, default : String) : String =
    head(map, key).getOrElse(default)

  def get(params : Map[String, Seq[String]]) : Seq[Activity] = {
    Seq(Activity(
      id        = headOr(params, "project", "project") + "-" + headOr(params, "id","id"),
      title     = headOr(params, "title", "some title"),
      body      = headOr(params, "body" , "some body"),
      createdAt = new java.util.Date(),
      source    = headOr(params, "source", "some source"),
      project   = headOr(params, "project", "some proejct"),
      url       = head(params, "url")map(new URI(_)),
      iconUrl   = head(params, "user").map((s : String) => new URI(Bozu.getIcon(s))),
      author    = head(params, "user"),
      status    = Status(headOr(params,"status", "Info"))
    ))
  }
}

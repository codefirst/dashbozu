package bz
import java.net.URI
import models._
import scala.util.parsing.json.JSON

class NewRelicBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : Seq[Activity] = {

    if (!params.contains("alert")) {
      return Seq();
    }

    val jsonString:String = params("alert").head
    val json:Option[Any] = JSON.parseFull(jsonString)
    val map:Map[String,Any] = json.get.asInstanceOf[Map[String, Any]]

    val projectName:String = map.get("application_name").get.asInstanceOf[String]
    var url = map.get("alert_url").get.asInstanceOf[String]
    val title:String = map.get("short_description").get.asInstanceOf[String]
    val message:String = map.get("long_description").get.asInstanceOf[String]

    Seq(Activity(
      id        = "newrelic-" + url,
      title     = title,
      body      = message,
      createdAt = new java.util.Date(),
      source    = "newrelic",
      project   = projectName,
      url       = Some(new URI(url)),
      iconUrl   = None,
      author    = None,
      status    = Error
    ))
  }
}

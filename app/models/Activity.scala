package models
import java.net.URI
import java.util.Date
import sjson.json._

sealed case class Activity(
  id        : String,
  title     : String,
  body      : String,
  createdAt : Date,
  source    : String,
  project   : String,
  url       : Option[URI],
  iconUrl   : Option[URI]
)

object ActivityProtocol extends DefaultProtocol {
  implicit val dateFormat : Format[Date] =
    wrap("date")(_.getTime(), new Date((_ : Long)))

  implicit val uriFormat : Format[URI] =
    wrap("uri")(_.toString(), new URI(_ : String))

  implicit val format : Format[Activity] =
    asProduct8("id","title","body","createdAt","source", "project", "url", "iconUrl")(Activity.apply _)(Activity.unapply(_).get)
}

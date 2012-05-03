package models


import java.net.URI
import java.util.Date

sealed abstract class Status
object Status {
  def apply(name : String) =
    name match {
      case "Success" =>
        Success
      case "Info" =>
        Info
      case "Warn" =>
        Warn
      case "Error" =>
        Error
      case _ =>
        Info
    }
}
case object Success extends Status
case object Info    extends Status
case object Warn    extends Status
case object Error   extends Status

sealed case class Activity (
  id        : String,
  title     : String,
  body      : String,
  createdAt : Date,
  source    : String,
  project   : String,
  url       : Option[URI],
  iconUrl   : Option[URI],
  status    : Status = Info,
  author    : Option[String]
)


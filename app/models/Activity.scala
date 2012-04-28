package models
import java.net.URI
import java.util.Date
import sjo

sealed case class Activity(
  id        : String,
  title     : String,
  body      : String,
  createdAt : Date,
  source    : String,
  project   : String,
  url       : Option[URI],
  icon_url  : Option[URI]
)

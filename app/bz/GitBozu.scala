package bz

import scala.xml._
import dispatch._
import models._
import java.text.SimpleDateFormat
import java.net.URI

class GitBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : List[Activity] = {
     for {
      commit <- (XML.loadString(params("commits").head) \ "commit").toList
     } yield Activity(
      id      = (commit \ "id").text,
      title   = "commit: %s to %s".format((commit \ "id").text.take(8),
                                          (commit \ "refname").text),
      body    = (commit \ "body").text,
      createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((commit \ "date").text),
      source  = "git",
      project = (commit \ "project").text,
      url     = None,
      iconUrl = Some(new URI(Bozu.getIcon((commit \ "email").text))),
      status  = Info,
      author  = Some((commit \ "email").text)
      )
  }
}

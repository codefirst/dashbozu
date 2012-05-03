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
    } yield Activity((commit \ "id").text,
                     "commit: %s to %s by %s".format((commit \ "id").text.take(8),
                                               (commit \ "refname").text,
                                               (commit \ "email").text),
                     (commit \ "body").text,
                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((commit \ "date").text),
                     "git",
                     (commit \ "project").text,
                     None,
                     Some(new URI(Bozu.getIcon((commit \ "email").text))))
  }
}

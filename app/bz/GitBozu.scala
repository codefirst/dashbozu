package bz

import scala.xml._
import dispatch._
import models._
import java.text.SimpleDateFormat
import java.net.URI

class GitBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : List[Activity] = {
    println(params("commits").head)
    for {
      commit <- (XML.loadString(params("commits").head) \ "commit").toList
    } yield Activity((commit \ "id").text,
                     "commit: "+((commit \ "id").text).take(8),
                     (commit \ "body").text,
                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((commit \ "date").text),
                     "git",
                     (commit \ "project").text,
                     None,
                     None)
  }
}

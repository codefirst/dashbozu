package bz

import scala.xml._
import dispatch._
import models._
import java.text.SimpleDateFormat
import java.net.URI

class JenkinsBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : List[Activity] = {
    params("url").toList.flatMap(urlstring =>
      Http( url(urlstring) <> {
        elem => (elem \\ "build").map(build => {
            val id        = (build \ "url").text
            val title     = "Job #%s (%s)".format((build \ "number").text,
                                                  (build \ "result").text)
            val body      = (build \ "changeSet" \ "item" \ "comment").text
            val createdAt = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").parse(
                              (build \ "id").text)
            val source    = "jenkins"
            val project   = (elem \\ "displayName").text
            val url       = Some(new URI(id))
            val iconUrl   = Some(new URI("/assets/images/icons/jenkins/%s.png".format(
                              (build \ "result").text.toLowerCase)))
            Activity(id, title, body, createdAt, source, project, url, iconUrl)
        }).toList
      }))
  }
}

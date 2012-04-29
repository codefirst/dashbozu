package bz

import scala.xml._
import dispatch._
import models._
import java.text.SimpleDateFormat
import java.net.URI

class JenkinsBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : Seq[Activity] = {
    for {
      domain   <- params.get("url").toSeq.flatMap(_.headOption)
      project  <- params.get("project").toSeq.flatMap(_.headOption)
      apiurl   =  url("%s/job/%s/api/xml?depth=2".format(domain, project))
      activity <- Http( apiurl <> { elem => {
        (elem \\ "build").map(build => {
          val projectName = (elem \\ "displayName").text

          val id        = (build \ "url").text
          val title     = "%s #%s (%s)".format(projectName,
                            (build \ "number").text,
                            (build \ "result").text)
          val body      = (build \ "changeSet" \ "item" \ "comment").text
          val createdAt = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").parse(
                            (build \ "id").text)
          val source    = "jenkins"
          val project   = projectName
          val url       = Some(new URI(id))
          val iconUrl   = Some(new URI("/assets/images/icons/jenkins/%s.png".format(
                            (build \ "result").text.toLowerCase)))
          Activity(id, title, body, createdAt, source, project, url, iconUrl)
        })
      }})
    } yield activity
  }
}

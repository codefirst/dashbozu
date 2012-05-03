package bz

import scala.xml._
import dispatch._
import models._
import java.text.SimpleDateFormat
import java.net.URI
import play.api.Play
import play.api.Play.current

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
          val result =
            (build \ "result").text.toLowerCase
          val iconUrl   =
            Some(new URI(controllers.routes.Assets.at("images/icons/jenkins/%s.png".format(result)).url))
          val status =
            result match {
              case "aborted" | "failure" =>
                Error
              case "success" =>
                Success
              case "unstable" =>
                Warn
              case _ =>
                Info
            }
          Activity(
            id      = id,
            title   = title,
            body    = body,
            createdAt = createdAt,
            source  = source,
            project = project,
            url     = url,
            iconUrl = iconUrl,
            author  = None,
            status  = status)
        })
      }})
    } yield activity
  }
}

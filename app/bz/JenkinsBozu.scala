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
        elem => (elem \\ "entry").flatMap(entry => {
          for {
            id        <- (entry \ "link" \ "@href").headOption
            title     = (entry \ "title").text
            body      = ""
            createdAt = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").
                          parse(((entry \ "id").text.split(":"))(3))
            source    = "jenkins"
            project   = (entry \ "id").text.split(":")(2)
            url       = Some(new URI(id.text))
            iconUrl   = None
          } yield Activity(id.text, title, body, createdAt, source, project, url, iconUrl)
        }).toList
      }))
  }
}

package bz

import scala.xml._
import dispatch._
import models._
import java.text.SimpleDateFormat
import java.net.URI

class RedmineBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : Seq[Activity] = {
    for {
      urlstr <- params.get("url").toSeq.flatMap(_.headOption)
      activity <- Http( url(urlstr) <> {
        elem => (elem \\ "entry").map(entry => {
          val id        = (entry \ "id").text
          val title     = (entry \ "title").text
          val body      = (entry \ "body").text
          val createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00").
                            parse((entry \ "updated").text)
          val source    = "redmine"
          val project   = (entry \ "title").text.split(" - ")(0)
          val url       = Some(new URI((entry \ "link" \ "@href").text))
          val iconUrl   = Some(new URI(Bozu.getIcon((entry \ "author" \ "email").text)))
          Activity(
            id        = id,
            title     = title,
            body      = body,
            createdAt = createdAt,
            source    = source,
            project   = project,
            url       = url,
            iconUrl   = iconUrl,
            status    = Info,
            author    = Some((entry \ "author" \ "email").text)
          )
        })
      })
    } yield activity
  }
}
